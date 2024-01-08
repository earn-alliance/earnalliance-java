package earnalliance.model;

import earnalliance.validation.Required;

import java.util.Objects;
import java.util.Set;

public final class NodeOptions {

  @Required
  private String clientId;

  @Required
  private String clientSecret;

  @Required
  private String gameId;

  @Required
  private String dsn = "https://events.earnalliance.com/v2/custom-events";

  private Long batchSize;
  private Set<String> flushEvents;
  private Long interval;
  private Long maxRetryAttempts;

  private NodeOptions() {
    // it is expected to be instantiated via builder
  }

  public Long getBatchSize() {
    return this.batchSize;
  }

  public void setBatchSize(Long batchSize) {
    this.batchSize = batchSize;
  }

  public String getClientId() {
    return this.clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return this.clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getDsn() {
    return this.dsn;
  }

  public void setDsn(String dsn) {
    this.dsn = dsn;
  }

  public Set<String> getFlushEvents() {
    return this.flushEvents;
  }

  public void setFlushEvents(Set<String> flushEvents) {
    this.flushEvents = flushEvents;
  }

  public String getGameId() {
    return this.gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public Long getInterval() {
    return this.interval;
  }

  public void setInterval(Long interval) {
    this.interval = interval;
  }

  public Long getMaxRetryAttempts() {
    return this.maxRetryAttempts;
  }

  public void setMaxRetryAttempts(Long maxRetryAttempts) {
    this.maxRetryAttempts = maxRetryAttempts;
  }

  public static Builder newBuilder() {
    return new Builder(new NodeOptions());
  }

  public static final class Builder {

    private final NodeOptions options;

    private Builder(NodeOptions options) {
      this.options = options;
    }

    public Builder withClientId(String clientId) {
      this.options.setClientId(clientId);
      return this;
    }

    public Builder withClientSecret(String clientSecret) {
      this.options.setClientSecret(clientSecret);
      return this;
    }

    public Builder withGameId(String gameId) {
      this.options.setGameId(gameId);
      return this;
    }

    public Builder withBatchSize(Long batchSize) {
      this.options.setBatchSize(batchSize);
      return this;
    }

    public Builder withDsn(String dsn) {
      this.options.setDsn(dsn);
      return this;
    }

    public Builder withFlushEvents(Set<String> events) {
      this.options.setFlushEvents(events);
      return this;
    }

    public Builder withInterval(Long interval) {
      this.options.setInterval(interval);
      return this;
    }

    public Builder withMaxRetryAttempts(Long maxRetryAttempts) {
      this.options.setMaxRetryAttempts(maxRetryAttempts);
      return this;
    }

    public NodeOptions build() {
      return this.options;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NodeOptions that = (NodeOptions) o;
    return Objects.equals(this.batchSize, that.batchSize)
      && Objects.equals(this.clientId, that.clientId)
      && Objects.equals(this.clientSecret, that.clientSecret)
      && Objects.equals(this.dsn, that.dsn)
      && Objects.equals(this.flushEvents, that.flushEvents)
      && Objects.equals(this.gameId, that.gameId)
      && Objects.equals(this.interval, that.interval)
      && Objects.equals(this.maxRetryAttempts, that.maxRetryAttempts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
      this.batchSize,
      this.clientId,
      this.clientSecret,
      this.dsn,
      this.flushEvents,
      this.gameId,
      this.interval,
      this.maxRetryAttempts);
  }

  @Override
  public String toString() {
    return "NodeOptions{" +
      "batchSize=" + batchSize +
      ", clientId='" + clientId + '\'' +
      ", clientSecret='" + clientSecret + '\'' +
      ", dsn='" + dsn + '\'' +
      ", flushEvents=" + flushEvents +
      ", gameId='" + gameId + '\'' +
      ", interval=" + interval +
      ", maxRetryAttempts=" + maxRetryAttempts +
      '}';
  }
}
