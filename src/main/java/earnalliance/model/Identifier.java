package earnalliance.model;

import java.util.Objects;

public final class Identifier implements QueueItem {

  private String userId;
  private String appleId;
  private String discordId;
  private String email;
  private String epicGamesId;
  private String steamId;
  private String twitterId;
  private String walletAddress;

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAppleId() {
    return this.appleId;
  }

  public void setAppleId(String appleId) {
    this.appleId = appleId;
  }

  public String getDiscordId() {
    return this.discordId;
  }

  public void setDiscordId(String discordId) {
    this.discordId = discordId;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEpicGamesId() {
    return this.epicGamesId;
  }

  public void setEpicGamesId(String epicGamesId) {
    this.epicGamesId = epicGamesId;
  }

  public String getSteamId() {
    return this.steamId;
  }

  public void setSteamId(String steamId) {
    this.steamId = steamId;
  }

  public String getTwitterId() {
    return this.twitterId;
  }

  public void setTwitterId(String twitterId) {
    this.twitterId = twitterId;
  }

  public String getWalletAddress() {
    return this.walletAddress;
  }

  public void setWalletAddress(String walletAddress) {
    this.walletAddress = walletAddress;
  }

  public Identifier removeProperties(IdentifierPropNames... propNames) {
    for (IdentifierPropNames propName : propNames) {
      switch (propName) {
        case APPLE_ID:
          this.appleId = null;
          break;
        case DISCORD_ID:
          this.discordId = null;
          break;
        case EMAIL:
          this.email = null;
          break;
        case EPIC_GAMES_ID:
          this.epicGamesId = null;
          break;
        case STEAM_ID:
          this.steamId = null;
          break;
        case TWITTER_ID:
          this.twitterId = null;
          break;
        case WALLET_ADDRESS:
          this.walletAddress = null;
          break;
      }
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Identifier that = (Identifier) o;
    return Objects.equals(this.userId, that.userId)
      && Objects.equals(this.appleId, that.appleId)
      && Objects.equals(this.discordId, that.discordId)
      && Objects.equals(this.email, that.email)
      && Objects.equals(this.epicGamesId, that.epicGamesId)
      && Objects.equals(this.steamId, that.steamId)
      && Objects.equals(this.twitterId, that.twitterId)
      && Objects.equals(this.walletAddress, that.walletAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      this.userId,
      this.appleId,
      this.discordId,
      this.email,
      this.epicGamesId,
      this.steamId,
      this.twitterId,
      this.walletAddress);
  }

  @Override
  public String toString() {
    return "Identifier{" +
      "userId='" + userId + '\'' +
      ", appleId='" + appleId + '\'' +
      ", discordId='" + discordId + '\'' +
      ", email='" + email + '\'' +
      ", epicGamesId='" + epicGamesId + '\'' +
      ", steamId='" + steamId + '\'' +
      ", twitterId='" + twitterId + '\'' +
      ", walletAddress='" + walletAddress + '\'' +
      '}';
  }
}
