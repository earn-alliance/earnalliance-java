package earnalliance.model;

import java.util.List;
import java.util.Objects;

public class Payload {
  private String gameId;
  private List<Event> events;
  private List<Identifier> identifiers;

  public Payload(String gameId, List<Event> events, List<Identifier> identifiers) {
    this.gameId = gameId;
    this.events = events;
    this.identifiers = identifiers;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public List<Event> getEvents() {
    return events;
  }

  public void setEvents(List<Event> events) {
    this.events = events;
  }

  public List<Identifier> getIdentifiers() {
    return identifiers;
  }

  public void setIdentifiers(List<Identifier> identifiers) {
    this.identifiers = identifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Payload payload = (Payload) o;
    return Objects.equals(this.gameId, payload.gameId)
      && Objects.equals(this.events, payload.events)
      && Objects.equals(this.identifiers, payload.identifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.gameId, this.events, this.identifiers);
  }

  @Override
  public String toString() {
    return "Payload{" +
      "gameId='" + gameId + '\'' +
      ", events=" + events +
      ", identifiers=" + identifiers +
      '}';
  }
}
