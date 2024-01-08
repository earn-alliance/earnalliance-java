package earnalliance.processor;

import earnalliance.Utils;
import earnalliance.model.*;
import earnalliance.model.result.*;
import earnalliance.transporter.HTTPTransporter;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static earnalliance.Constants.FLUSH_COOLDOWN;

public class BatchProcessor {

  private final NodeOptions options;
  private List<QueueItem> queueItems = new ArrayList<>();
  private long lastFlush;
  private final HTTPTransporter transporter;
  private final AtomicBoolean batchRunning;
  private final AtomicBoolean flushRunning;

  public BatchProcessor(HTTPTransporter transporter, NodeOptions options) {
    this.transporter = transporter;
    this.options = options;
    this.lastFlush = 0;
    this.batchRunning = new AtomicBoolean(false);
    this.flushRunning = new AtomicBoolean(false);
  }

  /**
   * Adds an event to the processing list and returns a CompletableFuture that represents the completion of the operation.
   *
   * @param event the Event to be added to the processing list
   * @return a CompletableFuture that represents the completion of the operation.
   * The CompletableFuture will contain an ExecutionResult that may contain a result or an error message
   */
  public CompletableFuture<ExecutionResult<?>> addEvent(Event event) {
    this.queueItems.add(event);

    final var flushEvents = this.options.getFlushEvents();
    if (flushEvents != null && flushEvents.contains(event.getEvent())) {
      return this.flush();
    }

    return this.scheduleBatch();
  }

  /**
   * Adds an Identifier to the processing list and returns a CompletableFuture that represents the completion of the operation.
   *
   * @param identifier the Identifier to be added to the processing list
   * @return a CompletableFuture that represents the completion of the operation.
   * The CompletableFuture will contain an ExecutionResult that may contain a result or an error message
   */
  public CompletableFuture<ExecutionResult<?>> addIdentifier(Identifier identifier) {
    this.queueItems.add(identifier);
    return this.flush();
  }

  /**
   * Schedules a batch processing based on the current state of the batch queue.
   *
   * @return a CompletableFuture that represents the completion of the scheduling process.
   * The CompletableFuture will contain an ExecutionResult that may contain a result or an error message.
   */
  public CompletableFuture<ExecutionResult<?>> scheduleBatch() {
    if (this.queueItems.size() >= this.options.getBatchSize()) {
      return this.process();
    } else if (batchRunning.compareAndSet(false, true)) {
      return Utils.delayFuture(this::process, this.options.getInterval(), TimeUnit.MILLISECONDS);
    }

    return CompletableFuture.completedFuture(new SuccessResult("Batch running"));
  }

  /**
   * Flushes the batch processing by scheduling a process or delaying the process based on the cooldown time.
   *
   * @return a CompletableFuture that represents the completion of the flush operation.
   * The CompletableFuture will contain an ExecutionResult that may contain a result or an error message.
   */
  public CompletableFuture<ExecutionResult<?>> flush() {
    if (!this.flushRunning.compareAndSet(false, true)) {
      return CompletableFuture.completedFuture(new FailureResult("Flush running"));
    }

    long now = System.currentTimeMillis();
    long timeSinceLastFlush = now - this.lastFlush;

    if (timeSinceLastFlush >= FLUSH_COOLDOWN) {
      this.lastFlush = now;
      return this.process();
    }

    long delay = FLUSH_COOLDOWN - timeSinceLastFlush;
    return Utils.delayFuture(this::process, delay, TimeUnit.MILLISECONDS);
  }

  /**
   * Processes a batch of {@link QueueItem} by sending them to the transporter for execution.
   *
   * @return a CompletableFuture that represents the completion of the processing.
   * The CompletableFuture will contain an ExecutionResult that may contain a result or an error message.
   */
  public CompletableFuture<ExecutionResult<?>> process(){
    this.resetProcessing();

    final int batchSize = Math.toIntExact(this.options.getBatchSize());

    List<QueueItem> batch = Utils.take(this.queueItems, batchSize);
    if (batch.isEmpty()) {
      return CompletableFuture.completedFuture(new SuccessResult("batch already processed"));
    }

    this.queueItems = Utils.drop(this.queueItems, batchSize);

    List<Event> events = new ArrayList<>();
    List<Identifier> identifiers = new ArrayList<>();

    for (QueueItem item : batch) {
      if (item instanceof Event) {
        events.add((Event) item);
      } else if (item instanceof Identifier) {
        identifiers.add((Identifier)item);
      }
    }

    var payload = new Payload(this.options.getGameId(), events, identifiers);

    try {
      return this.transporter.send(payload, 0).thenApply(BatchProcessor::getExecutionResult);
    } catch (Exception e) {
      return CompletableFuture.completedFuture(new ExceptionResult(e));
    }
  }

  /**
   * Returns an ExecutionResult based on the provided HttpResponse.
   *
   * @param response the HttpResponse to process
   * @return the ExecutionResult containing the result of the processing
   */
  private static ExecutionResult<String> getExecutionResult(HttpResponse<String> response) {
    if (response.statusCode() == 200) {
      return new SuccessResponseResult<>(response.body(), "Processing successful!");
    }

    return new FailureResponseResult<>(response.statusCode(), response.body(), "Processing failure!");
  }

  /**
   * Resets the processing state by setting the batchRunning and flushRunning flags to false.
   *
   * @see BatchProcessor#process()
   */
  public void resetProcessing() {
    this.batchRunning.compareAndSet(true, false);
    this.flushRunning.compareAndSet(true, false);
  }
}
