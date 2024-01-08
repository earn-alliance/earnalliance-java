package earnalliance.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import earnalliance.model.trait.Traits;

public class GsonConfiguration {

  public Gson getGson() {
    return new GsonBuilder()
      .registerTypeAdapter(Traits.class, new TraitsSerializer())
      .serializeNulls()
      .create();
  }
}
