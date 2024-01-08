package earnalliance.model;


import earnalliance.model.trait.Traits;

import java.util.Objects;

public final class Event implements QueueItem {
  private String userId;
  private String time;
  private String event;
  private String groupId;
  private Traits traits;
  private Long value;

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getTime() {
    return this.time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getEvent() {
    return this.event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getGroupId() {
    return this.groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public Traits getTraits() {
    return this.traits;
  }

  public void setTraits(Traits traits) {
    this.traits = traits;
  }

  public Long getValue() {
    return this.value;
  }

  public void setValue(Long value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Event event1 = (Event) o;
    return Objects.equals(this.userId, event1.userId)
      && Objects.equals(this.time, event1.time)
      && Objects.equals(this.event, event1.event)
      && Objects.equals(this.groupId, event1.groupId)
      && Objects.equals(this.traits, event1.traits)
      && Objects.equals(this.value, event1.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.userId, this.time, this.event, this.groupId, this.traits, this.value);
  }

  @Override
  public String toString() {
    return "Event{" +
      "userId='" + userId + '\'' +
      ", time='" + time + '\'' +
      ", event='" + event + '\'' +
      ", groupId='" + groupId + '\'' +
      ", traits=" + traits +
      ", value=" + value +
      '}';
  }
}
