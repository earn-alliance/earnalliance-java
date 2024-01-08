package earnalliance.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The Required annotation is used to mark fields that are required and should not be null.
 * This annotation can be applied to fields in a class.
 *
 * <p>Usage example:
 * <pre>{@code
 * @Required
 * private String fieldName;
 * }</pre>
 *
 * <p>When validating an object that contains fields annotated with {@literal @}Required,
 * any null value in those fields will result in a validation failure.
 *
 * <p>Note that the presence of the {@literal @}Required annotation does not guarantee
 * that the field will be non-null at runtime. It is the responsibility of the validation logic
 * to handle null values appropriately.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {
}
