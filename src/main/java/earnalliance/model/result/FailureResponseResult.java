package earnalliance.model.result;

public final class FailureResponseResult<T> extends ExecutionResult<T> {

  private final int responseCode;

  public FailureResponseResult(int responseCode, T responseBody, String message) {
    super(responseBody, message);
    this.responseCode = responseCode;
  }

  public int getResponseCode() {
    return responseCode;
  }

  @Override
  public String toString() {
    return "FailureResult{" +
      "responseCode=" + responseCode +
      ", content=" + content +
      ", message='" + message + '\'' +
      '}';
  }
}
