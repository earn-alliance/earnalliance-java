package earnalliance.model;

import java.util.List;

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
}
