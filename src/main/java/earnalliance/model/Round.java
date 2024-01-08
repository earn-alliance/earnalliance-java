package earnalliance.model;

import earnalliance.Utils;
import earnalliance.model.result.ExecutionResult;
import earnalliance.model.trait.Traits;
import earnalliance.processor.BatchProcessor;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class Round {
  private final String groupId;
  private final BatchProcessor processor;

  public Round(BatchProcessor processor, String groupId) {
    this.processor = processor;
    this.groupId = Optional.ofNullable(groupId).orElse(UUID.randomUUID().toString());
  }

  public CompletableFuture<ExecutionResult<?>> track(String userId, String eventName) {
    return addEvent(Utils.track(userId, eventName));
  }

  public CompletableFuture<ExecutionResult<?>> track(String userId, String eventName, Traits traits) {
    return addEvent(Utils.track(userId, eventName, traits));
  }

  public CompletableFuture<ExecutionResult<?>> track(String userId, String eventName, Long value) {
    return addEvent(Utils.track(userId, eventName, value));
  }

  private CompletableFuture<ExecutionResult<?>> addEvent(Event event) {
    event.setGroupId(this.groupId);
    return this.processor.addEvent(event);
  }
}
