package earnalliance.configuration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import earnalliance.model.trait.Traits;

import java.lang.reflect.Type;

public class TraitsSerializer implements JsonSerializer<Traits> {

  @Override
  public JsonElement serialize(Traits src, Type typeOfSrc, JsonSerializationContext context) {
    final var json = new JsonObject();
    src.getTraits().forEach(entry -> json.add(entry.getKey(), context.serialize(entry.getValue())));
    return json;
  }
}
