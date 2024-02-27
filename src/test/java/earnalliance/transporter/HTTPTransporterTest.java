package earnalliance.transporter;

import earnalliance.configuration.GsonConfiguration;
import earnalliance.model.Event;
import earnalliance.model.NodeOptions;
import earnalliance.model.Payload;
import earnalliance.model.trait.Traits;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
class HTTPTransporterTest {

  private static final String CLIENT_ID = "ID";
  private static final String CLIENT_SECRET = "SECRET";
  private static final String GAME_ID = "GAME";

  private HTTPTransporter subject;

  @Mock
  private HttpClient client;

  @BeforeEach
  void setUp() {
    this.subject = new HTTPTransporter(createNodeOptions(), () -> client, new GsonConfiguration());
  }

  private NodeOptions createNodeOptions() {
    return NodeOptions.newBuilder()
      .withClientId(CLIENT_ID)
      .withClientSecret(CLIENT_SECRET)
      .withGameId(GAME_ID)
      .withDsn("http://example.com")
      .withMaxRetryAttempts(2L)
      .build();
  }

  @Test
  void testSend() {
    final var response = Mockito.mock(HttpResponse.class);
    Mockito.when(response.statusCode()).thenReturn(200);
    Mockito.when(client.sendAsync(Mockito.any(), Mockito.any()))
      .thenReturn(CompletableFuture.completedFuture(response));

    Traits traits = Traits.fromEntries(
      Traits.string("string", "value"),
      Traits.bool("bool", false),
      Traits.numeric("byte", (byte) 5),
      Traits.numeric("short", (short) 5),
      Traits.numeric("integer", 5),
      Traits.numeric("long", 5L),
      Traits.numeric("double", 5.5D),
      Traits.numeric("float", 10.1F)
    );

    Event event = new Event();
    event.setEvent("KILL_ZOMBIE");
    event.setUserId("2031");
    event.setTraits(traits);

    event.setTime(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));

    final var payload = new Payload(GAME_ID, List.of(event), Collections.emptyList());
    final var actual = subject.send(payload, 1).join();
    Assertions.assertEquals(200, actual.statusCode());
  }
}