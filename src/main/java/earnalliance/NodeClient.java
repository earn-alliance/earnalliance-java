package earnalliance;

import earnalliance.configuration.GsonConfiguration;
import earnalliance.model.*;
import earnalliance.model.result.ExecutionResult;
import earnalliance.model.trait.TraitEntry;
import earnalliance.model.trait.Traits;
import earnalliance.processor.BatchProcessor;
import earnalliance.transporter.HTTPTransporter;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;

import static earnalliance.Constants.START_GAME;

/**
 * The Earn Alliance Node SDK Client.
 */
public class NodeClient {

  protected final NodeOptions options;
  protected final BatchProcessor processor;
  protected final HTTPTransporter transporter;
  protected final GsonConfiguration gsonConfiguration;

  /**
   * Creates a new Node SDK instance.
   *
   * @param options Configuration options for this SDK.
   */
  public NodeClient(NodeOptions options) {
    this.gsonConfiguration = new GsonConfiguration();
    this.options = options;
    this.transporter = new HTTPTransporter(options, HttpClient::newHttpClient, this.gsonConfiguration);
    this.processor = new BatchProcessor(this.transporter, options);
  }

  public CompletableFuture<ExecutionResult<?>> startGame(String userId) {
    Event event = Utils.track(userId, START_GAME);
    return this.processor.addEvent(event);
  }

  public Round startRound(String groupId) {
    return new Round(this.processor, groupId);
  }

  public Round startRound() {
    return new Round(this.processor, null);
  }

  public CompletableFuture<ExecutionResult<?>> track(String userId, String eventName, TraitEntry<?>... traits) {
    Event event = Utils.track(userId, eventName, Traits.fromEntries(traits));
    return this.processor.addEvent(event);
  }

  public CompletableFuture<ExecutionResult<?>> track(String userId, String eventName, Long value) {
    Event event = Utils.track(userId, eventName, value);
    return this.processor.addEvent(event);
  }

  public CompletableFuture<ExecutionResult<?>> flush() {
    return this.processor.flush();
  }

  public CompletableFuture<ExecutionResult<?>>
  setUserIdentifiers(String userId, IdentifyingProperties identifyingProperties) {
    Identifier identifier = Utils.identify(userId, identifyingProperties);
    return this.processor.addIdentifier(identifier);
  }

  public CompletableFuture<ExecutionResult<?>>
  removeUserIdentifiers(Identifier identifier, IdentifierPropNames... propertyNames) {
    return this.processor.addIdentifier(identifier.removeProperties(propertyNames));
  }
}
