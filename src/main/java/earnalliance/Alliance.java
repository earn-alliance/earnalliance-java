package earnalliance;

import earnalliance.model.NodeOptions;
import earnalliance.validation.NodeOptionsRequiredFieldsValidator;
import earnalliance.validation.ValidationFailure;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Main entry point into the Earn Alliance SDK
 */
public final class Alliance {

  /**
   * Retrieves the value of the specified environment variable.
   *
   * @param name the name of the environment variable
   * @return an Optional containing the value of the environment variable, or empty if the variable is not defined
   */
  public static Optional<String> getEnvVariable(final String name) {
    return Optional.ofNullable(System.getenv(name));
  }

  /**
   * Sets the property from the environment variable if it is not already set.
   *
   * @param propertySupplier the supplier for getting the property value
   * @param propertyConsumer the consumer for setting the property value
   * @param propertyName     the name of the environment variable
   */
  private static void setFromEnv(Supplier<String> propertySupplier,
                                 Consumer<String> propertyConsumer,
                                 String propertyName) {
    if (propertySupplier.get() == null) {
      getEnvVariable(propertyName).ifPresent(propertyConsumer);
    }
  }

  /**
   * Sets the property from the default value if it is not already set.
   *
   * @param <T>              the type of the property
   * @param propertySupplier the supplier for getting the property value
   * @param propertyConsumer the consumer for setting the property value
   * @param defaultValue     the default value of the property
   */
  private static <T> void setFromDefault(Supplier<T> propertySupplier,
                                         Consumer<T> propertyConsumer,
                                         T defaultValue) {
    if (propertySupplier.get() == null) {
      propertyConsumer.accept(defaultValue);
    }
  }

  /**
   * Initializes a NodeClient with the provided options.
   *
   * @param options the configuration options for the NodeClient
   * @return a new NodeClient instance
   */
  public static NodeClient init(final NodeOptions options) {
    setFromEnv(options::getClientId, options::setClientId, Constants.ENV_ALLIANCE_CLIENT_ID);
    setFromEnv(options::getClientSecret, options::setClientSecret, Constants.ENV_ALLIANCE_CLIENT_SECRET);
    setFromEnv(options::getDsn, options::setDsn, Constants.ENV_ALLIANCE_DSN);
    setFromEnv(options::getGameId, options::setGameId, Constants.ENV_ALLIANCE_GAME_ID);

    setFromDefault(options::getBatchSize, options::setBatchSize, Constants.DEFAULT_BATCH_SIZE);
    setFromDefault(options::getInterval, options::setInterval, Constants.DEFAULT_INTERVAL);
    setFromDefault(options::getMaxRetryAttempts, options::setMaxRetryAttempts, Constants.DEFAULT_MAX_RETRY_ATTEMPTS);

    validateNodeOptions(options);

    return new NodeClient(options);
  }

  private static void validateNodeOptions(final NodeOptions options) {
    final var validator = new NodeOptionsRequiredFieldsValidator();
    var result = validator.validate(options);
    if (result instanceof ValidationFailure) {
      final var errors = String.join(", ", ((ValidationFailure) result).getErrors());
      throw new IllegalArgumentException("Missing required client options: " + errors);
    }
  }
}
