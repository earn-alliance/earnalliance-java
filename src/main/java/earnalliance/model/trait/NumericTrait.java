package earnalliance.model.trait;

public final class NumericTrait<T extends Number> extends TraitEntry<T> {
  public NumericTrait(String key, T value, Class<T> type) {
    super(key, value, type);
  }
}
