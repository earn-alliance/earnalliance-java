package earnalliance.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierTest {

  private Identifier subject;

  @BeforeEach
  void setUp() {
    this.subject = new Identifier();
  }

  @Test
  void testRemoveProperties() {
    subject.setUserId("test");
    subject.setAppleId("test");
    subject.setDiscordId("test");
    subject.setEmail("test");
    subject.setEpicGamesId("test");
    subject.setSteamId("test");
    subject.setTwitterId("test");
    subject.setWalletAddress("test");

    subject.removeProperties(IdentifierPropNames.APPLE_ID, IdentifierPropNames.EPIC_GAMES_ID);

    assertNull(subject.getAppleId());
    assertNull(subject.getEpicGamesId());

    assertNotNull(subject.getUserId());
    assertNotNull(subject.getDiscordId());
    assertNotNull(subject.getEmail());
    assertNotNull(subject.getSteamId());
    assertNotNull(subject.getTwitterId());
    assertNotNull(subject.getWalletAddress());
  }
}