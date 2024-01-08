package earnalliance;

import earnalliance.model.Event;
import earnalliance.model.Identifier;
import earnalliance.model.IdentifierPropNames;
import earnalliance.model.IdentifyingProperties;
import earnalliance.model.trait.TraitEntry;
import earnalliance.model.trait.Traits;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Utils {

  public static Event track(String userId, String eventName) {
    return createEvent(userId, eventName);
  }

  public static Event track(String userId, String eventName, Long value) {
    final var event = createEvent(userId, eventName);
    event.setValue(value);
    return event;
  }

  public static Event track(String userId, String eventName, TraitEntry<?> trait) {
    final var event = createEvent(userId, eventName);
    event.setTraits(Traits.fromEntries(trait));
    return event;
  }

  public static Event track(String userId, String eventName, Traits traits) {
    var event = createEvent(userId, eventName);

    if (traits != null) {
      event.setTraits(traits);
    }

    return event;
  }

  private static Event createEvent(String userId, String eventName) {
    var time = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
    var event = new Event();
    event.setUserId(userId);
    event.setTime(time);
    event.setEvent(eventName);
    return event;
  }


  public static Identifier identify(String userId, IdentifyingProperties identifyingProperties) {
    Identifier identifiers = new Identifier();
    identifiers.setUserId(userId);

    identifiers.setAppleId(identifyingProperties.getAppleId());
    identifiers.setDiscordId(identifyingProperties.getDiscordId());
    identifiers.setEmail(identifyingProperties.getEmail());
    identifiers.setEpicGamesId(identifyingProperties.getEpicGamesId());
    identifiers.setSteamId(identifyingProperties.getSteamId());
    identifiers.setTwitterId(identifyingProperties.getTwitterId());
    identifiers.setWalletAddress(identifyingProperties.getWalletAddress());

    return identifiers;
  }

  public static Identifier clearIdentifiers(Identifier identifier, IdentifierPropNames... propertyNames) {
    return identifier.removeProperties(propertyNames);
  }

  public static <T> List<T> drop(List<T> input, int amount) {
    if (amount >= input.size()) {
      return new ArrayList<>();
    }

    return new ArrayList<>(input.subList(amount, input.size()));
  }

  public static <T> List<T> take(List<T> input, int amount) {
    if (amount >= input.size()) {
      return new ArrayList<>(input);
    }

    return new ArrayList<>(input.subList(0, amount));
  }

  /**
   * Delays the execution of a CompletableFuture by the specified interval.
   *
   * @param future   The CompletableFuture to delay.
   * @param interval The duration of the delay.
   * @param timeUnit The time unit of the delay duration.
   * @param <T>      The type of the CompletableFuture's result.
   * @return A new CompletableFuture that represents the delayed execution of the input CompletableFuture.
   * The result of the CompletableFuture will be the same as the result of the input CompletableFuture.
   * @throws IllegalStateException If the delay is interrupted.
   */
  public static <T> CompletableFuture<T>
  delayFuture(Supplier<CompletableFuture<T>> future, long interval, TimeUnit timeUnit) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        timeUnit.sleep(interval);
      } catch (InterruptedException e) {
        throw new IllegalStateException("error delaying future execution!", e);
      }
      return null;
    }).thenCompose(ignored -> future.get());
  }
}
