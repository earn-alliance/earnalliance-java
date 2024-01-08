package earnalliance.model.trait;

public final class NumericTrait extends TraitEntry<Long> {
  public NumericTrait(String key, Long value) {
    super(key, value, Long.class);
  }
}
