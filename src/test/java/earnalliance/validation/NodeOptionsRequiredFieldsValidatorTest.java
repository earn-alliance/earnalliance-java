package earnalliance.validation;

import earnalliance.model.NodeOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class NodeOptionsRequiredFieldsValidatorTest {

  private NodeOptionsRequiredFieldsValidator subject;

  @BeforeEach
  void setUp() {
    this.subject = new NodeOptionsRequiredFieldsValidator();
  }

  @Test
  void testValidationOfNodeOptionsFailure() {
    final var options = NodeOptions.newBuilder()
      .withClientId("clientId")
      .withClientSecret("clientSecret")
      .withGameId("gameId")
      .build();

    final var actual = subject.validate(options);
    Assertions.assertInstanceOf(ValidationFailure.class, actual);
    Assertions.assertEquals(List.of("dsn"), ((ValidationFailure) actual).getErrors());
  }

  @Test
  void testValidationOfNodeOptionsSuccess() {
    final var options = NodeOptions.newBuilder()
      .withClientId("clientId")
      .withClientSecret("clientSecret")
      .withGameId("gameId")
      .withDsn("dsn")
      .build();

    final var actual = subject.validate(options);
    Assertions.assertInstanceOf(ValidationSuccess.class, actual);
  }
}