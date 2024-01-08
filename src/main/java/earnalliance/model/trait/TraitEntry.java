package earnalliance.model.trait;

public abstract class TraitEntry<T> {
  private final String key;
  private final T value;
  private transient final Class<T> type;

  protected TraitEntry(String key, T value, Class<T> type) {
    this.key = key;
    this.value = value;
    this.type = type;
  }

  public String getKey() {
    return key;
  }

  public T getValue() {
    return value;
  }

  public Class<T> getType() {
    return type;
  }

  @Override
  public String toString() {
    return "TraitEntry{" +
      "key='" + key + '\'' +
      ", value=" + value +
      ", type=" + type.getName() +
      '}';
  }
}
