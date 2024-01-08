package earnalliance.validation;

public interface Validator<T> {
  ValidationResult validate(final T subject);
}



