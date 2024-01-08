package earnalliance.model.result;

public abstract class ExecutionResult<T> {

  protected final T content;
  protected final String message;

  protected ExecutionResult(T content, String message) {
    this.content = content;
    this.message = message;
  }

  public T getContent() {
    return content;
  }

  public String getMessage() {
    return message;
  }
}
