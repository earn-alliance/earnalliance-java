package earnalliance.validation;

import java.util.List;

public class ValidationFailure implements ValidationResult {

  public List<String> errors;

  public ValidationFailure(List<String> errors) {
    this.errors = errors;
  }

  public List<String> getErrors() {
    return this.errors;
  }
}
