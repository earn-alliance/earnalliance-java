package earnalliance.model.result;

public final class SuccessResponseResult<T> extends ExecutionResult<T> {

  private final int responseCode;

  public SuccessResponseResult(T responseBody, String message) {
    this(200, responseBody, message);
  }

  public SuccessResponseResult(int responseCode, T responseBody, String message) {
    super(responseBody, message);
    this.responseCode = responseCode;
  }

  public int getResponseCode() {
    return responseCode;
  }

  @Override
  public String toString() {
    return "SuccessResult{" +
      "responseCode=" + responseCode +
      ", content=" + content +
      ", message='" + message + '\'' +
      '}';
  }
}
