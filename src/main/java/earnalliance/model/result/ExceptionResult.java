package earnalliance.model.result;

public final class ExceptionResult extends ExecutionResult<Throwable> {
  public ExceptionResult(Throwable responseBody) {
    super(responseBody, responseBody.getMessage());
  }

  @Override
  public String toString() {
    return "ExceptionResult{" +
      "content=" + content +
      ", message='" + message + '\'' +
      '}';
  }
}
