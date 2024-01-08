package earnalliance.model.trait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Traits {

  private final List<TraitEntry<?>> traits;

  public Traits() {
    this.traits = new ArrayList<>();
  }

  public void addTrait(final TraitEntry<?> entry) {
    this.traits.add(entry);
  }

  public static Traits fromEntries(final TraitEntry<?>... entries) {
    var traits = new Traits();
    Arrays.stream(entries).forEach(traits::addTrait);
    return traits;
  }

  public static TraitEntry<String> string(final String key, final String value) {
    return new StringTrait(key, value);
  }

  public static TraitEntry<Boolean> bool(final String key, final Boolean value) {
    return new BooleanTrait(key, value);
  }

  public static TraitEntry<Long> numeric(final String key, final Long value) {
    return new NumericTrait(key, value);
  }

  public List<TraitEntry<?>> getTraits() {
    return traits;
  }
}
