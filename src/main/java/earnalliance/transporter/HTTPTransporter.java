package earnalliance.transporter;

import com.google.gson.Gson;
import earnalliance.Utils;
import earnalliance.configuration.GsonConfiguration;
import earnalliance.model.NodeOptions;
import earnalliance.model.Payload;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class HTTPTransporter {

  private final NodeOptions options;
  private final Long maxRetryAttempts;
  private final Gson gson;
  private final Supplier<HttpClient> httpClientProvider;
  private final SecretKeySpec secretKey;

  public HTTPTransporter(NodeOptions options,
                         Supplier<HttpClient> httpClientProvider,
                         GsonConfiguration gsonConfiguration) {
    this.options = options;
    this.maxRetryAttempts = options.getMaxRetryAttempts();
    this.gson = gsonConfiguration.getGson();
    this.httpClientProvider = httpClientProvider;
    this.secretKey = new SecretKeySpec(this.options.getClientSecret().getBytes(), "HmacSHA256");
  }

  /**
   * Sends a payload asynchronously with the provided headers and returns a CompletableFuture that resolves to the HTTP response.
   *
   * @param payload The payload to be sent.
   * @param attempt The current attempt number (starting from 0).
   * @return A CompletableFuture that resolves to an HttpResponse<String> object if the request is successfully sent.
   */
  public CompletableFuture<HttpResponse<String>> send(Payload payload, int attempt) {
    try {
      String clientId = this.options.getClientId();
      long timestamp = Instant.now().toEpochMilli();
      String signature = sign(payload, timestamp);

      Map<String, String> headers = new HashMap<>();
      headers.put("Accept", "application/json");
      headers.put("Content-Type", "application/json");
      headers.put("x-client-id", clientId);
      headers.put("x-timestamp", String.valueOf(timestamp));
      headers.put("x-signature", signature);

      return sendHttpRequest(payload, headers);
    } catch (Exception e) {
      if (attempt < this.maxRetryAttempts) {
        return retry(payload, attempt + 1);
      }

      return CompletableFuture.failedFuture(e);
    }
  }

  /**
   * Signs the payload and returns the signature as a string.
   *
   * @param payload   The payload to be signed
   * @param timestamp The timestamp in milliseconds
   * @return The signature as a string
   * @throws NoSuchAlgorithmException If the HmacSHA256 algorithm is not available
   * @throws InvalidKeyException      If the client secret is an invalid key for HmacSHA256 algorithm
   */
  private String sign(Payload payload, long timestamp) throws NoSuchAlgorithmException, InvalidKeyException {
    String body = this.gson.toJson(payload);
    String message = this.options.getClientId() + timestamp + body;
    Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
    hmacSHA256.init(this.secretKey);

    return bytesToHex(hmacSHA256.doFinal(message.getBytes(StandardCharsets.UTF_8)));
  }

  /**
   * Convert the specified array of bytes to the hex string
   *
   * @param bytes input bytes array
   * @return string, representing the concatenated hex value of the input array
   */
  private static String bytesToHex(byte[] bytes) {
    final var hexString = new StringBuilder(2 * bytes.length);
    for (byte currentByte : bytes) {
      hexString.append(String.format("%02x", currentByte));
    }

    return hexString.toString();
  }

  /**
   * Retries sending a payload with exponential backoff.
   *
   * @param payload The payload to be sent
   * @param attempt The current attempt number (starting from 1)
   * @return A CompletableFuture that resolves to an HttpResponse<String> if the payload is successfully sent, or a failed future if an exception occurs
   */
  private CompletableFuture<HttpResponse<String>> retry(Payload payload, int attempt) {
    try {
      return Utils.delayFuture(
        () -> this.send(payload, attempt),
        (long) Math.pow(2, attempt) * 1000, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      return CompletableFuture.failedFuture(e);
    }
  }

  /**
   * Sends an HTTP request asynchronously and returns a CompletableFuture that resolves to the HTTP response.
   *
   * @param payload The payload to be sent in the request.
   * @param headers The headers to be included in the request.
   * @return A CompletableFuture that resolves to an HttpResponse<String> object if the request is successfully sent.
   */
  private CompletableFuture<HttpResponse<String>> sendHttpRequest(Payload payload, Map<String, String> headers) {
    HttpClient client = this.httpClientProvider.get();
    String json = this.gson.toJson(payload);
    final var request = HttpRequest.newBuilder()
      .uri(URI.create(this.options.getDsn()))
      .headers(headers.entrySet().stream().flatMap(e -> Stream.of(e.getKey(), e.getValue())).toArray(String[]::new))
      .POST(HttpRequest.BodyPublishers.ofString(json))
      .build();

    return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
  }
}
