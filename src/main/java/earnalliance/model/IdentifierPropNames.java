package earnalliance.model;

import java.util.Arrays;
import java.util.Optional;

public enum IdentifierPropNames {
  APPLE_ID("appleId"),
  DISCORD_ID("discordId"),
  EMAIL("email"),
  EPIC_GAMES_ID("epicGamesId"),
  STEAM_ID("steamId"),
  TWITTER_ID("twitterId"),
  WALLET_ADDRESS("walletAddress");

  private final String value;

  IdentifierPropNames(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static Optional<IdentifierPropNames> fromString(final String input) {
    return Arrays.stream(values()).filter(item -> item.value.equalsIgnoreCase(input)).findFirst();
  }
}
