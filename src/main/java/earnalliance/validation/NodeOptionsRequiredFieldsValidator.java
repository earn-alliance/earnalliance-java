package earnalliance.validation;

import earnalliance.model.NodeOptions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * NodeOptionsRequiredFieldsValidator is a class that implements the Validator interface and provides
 * a method to validate the required fields of a NodeOptions object.
 */
public final class NodeOptionsRequiredFieldsValidator implements Validator<NodeOptions> {

  @Override
  public ValidationResult validate(final NodeOptions subject) {
    final var errors = Arrays.stream(subject.getClass().getDeclaredFields())
      .filter(field -> field.isAnnotationPresent(Required.class))
      .flatMap(field -> {
        if (fieldIsNotNull(subject, field)) {
          return Stream.empty();
        }

        return Stream.of(field.getName());
      }).collect(Collectors.toList());

    return errors.isEmpty() ? new ValidationSuccess() : new ValidationFailure(errors);
  }

  private boolean fieldIsNotNull(NodeOptions subject, Field field) {
    try {
      final var accessor = subject.getClass().getMethod(synthesizeAccessorName(field.getName()));
      return accessor.invoke(subject) != null;
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      return false;
    }
  }

  private String synthesizeAccessorName(String name) {
    final var suffix = Character.toUpperCase(name.charAt(0)) + name.substring(1);
    return "get" + suffix;
  }
}
