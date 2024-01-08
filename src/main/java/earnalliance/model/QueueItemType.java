package earnalliance.model;


import java.util.Arrays;
import java.util.Optional;

public enum QueueItemType {
  EVENT("event"),
  IDENTIFIER("identifier");

  private final String value;

  QueueItemType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static Optional<QueueItemType> fromString(final String input) {
    return Arrays.stream(values()).filter(item -> item.value.equalsIgnoreCase(input)).findFirst();
  }
}
