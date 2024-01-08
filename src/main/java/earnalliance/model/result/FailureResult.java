package earnalliance.model.result;

public final class FailureResult extends ExecutionResult<String> {
  public FailureResult(String message) {
    super("Error occurred", message);
  }

  @Override
  public String toString() {
    return "ErrorResult{" + this.content + ": " + message + "}";
  }
}
