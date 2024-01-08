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
    Event event = Utils.track(userId, eventName);
    return addEvent(event);
  }

  public CompletableFuture<ExecutionResult<?>> track(String userId, String eventName, Traits traits) {
    Event event = Utils.track(userId, eventName, traits);
    return addEvent(event);
  }

  public CompletableFuture<ExecutionResult<?>> track(String userId, String eventName, Long value) {
    return this.track(userId, eventName, Traits.fromEntries(Traits.numeric("", value)));
  }

  private CompletableFuture<ExecutionResult<?>> addEvent(Event event) {
    event.setGroupId(this.groupId);
    return this.processor.addEvent(event);
  }
}
