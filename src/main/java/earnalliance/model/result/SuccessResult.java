package earnalliance.model.result;

public final class SuccessResult extends ExecutionResult<String> {
  public SuccessResult(String message) {
    super("Operation successful", message);
  }

  @Override
  public String toString() {
    return "SuccessResult{" + this.content + ": " + message + "}";
  }
}
