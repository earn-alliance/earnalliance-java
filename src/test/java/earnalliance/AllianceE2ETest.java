package earnalliance;

import earnalliance.model.IdentifyingProperties;
import earnalliance.model.NodeOptions;
import earnalliance.model.result.ExecutionResult;
import earnalliance.model.result.FailureResponseResult;
import earnalliance.model.result.FailureResult;
import earnalliance.model.result.SuccessResponseResult;
import earnalliance.model.trait.Traits;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static earnalliance.model.IdentifierPropNames.DISCORD_ID;
import static earnalliance.model.IdentifierPropNames.EMAIL;

@Disabled
class AllianceE2ETest {

    private static String clientId;
    private static String clientSecret;
    private static String gameId;
    private NodeClient alliance;

    @BeforeEach
    void setUp() {
        clientId = System.getenv("CLIENT_ID");
        clientSecret = System.getenv("CLIENT_SECRET");
        gameId = System.getenv("GAME_ID");
        this.alliance = Alliance.init(getNodeOptions(10L, 1000L));
    }

    @Test
    void testStartGame() {
        try {
            var startGame = this.alliance.startGame("2031");
            var result = startGame.get(5, TimeUnit.SECONDS);
            Assertions.assertInstanceOf(SuccessResponseResult.class, result);
        } catch (Exception ex) {
            Assertions.fail(ex.getMessage());
        }
    }

    private static NodeOptions getNodeOptions(long batchSize, long interval) {
        return NodeOptions.newBuilder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withGameId(gameId)
                .withMaxRetryAttempts(5L)
                .withBatchSize(batchSize)
                .withInterval(interval)
                .build();
    }

    @Test
    void testTracking() {
        var futures = IntStream.iterate(0, i -> i + 1)
                .limit(20)
                .mapToObj(i -> {
                    var weaponTrait = Traits.string("weapon", "knife");
                    var mobTrait = Traits.string("mob", "zombie");
                    return alliance.track("2031", "KILL", weaponTrait, mobTrait);
                })
                .collect(Collectors.toList());

        futures.stream().map(f -> {
            try {
                return f.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }).forEach(result -> {
            try {
                Assertions.assertTrue(resultIsSuccessful(result));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testRounds() {
        var round = alliance.startRound("2031");
        var futures = IntStream.iterate(0, i -> i + 1)
                .limit(20)
                .mapToObj(i -> round.track("2031", "KILL_ZOMBIE", Long.valueOf(i)))
                .collect(Collectors.toList());

        futures.forEach(future -> {
            try {
                var result = future.get(20, TimeUnit.SECONDS);
                Assertions.assertTrue(resultIsSuccessful(result));
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testSetUserIdentifiers() throws ExecutionException, InterruptedException, TimeoutException {
        var identifyingProperties = new IdentifyingProperties();
        identifyingProperties.setEmail("test@test.com");
        identifyingProperties.setDiscordId("123456");
        final var result = alliance.setUserIdentifiers("2031", identifyingProperties).get(5, TimeUnit.SECONDS);

        Assertions.assertTrue(resultIsSuccessful(result));
    }

    @Test
    void testRemoveUserIdentifiers() throws ExecutionException, InterruptedException, TimeoutException {
        var identifyingProperties = new IdentifyingProperties();
        identifyingProperties.setEmail("test@test.com");
        identifyingProperties.setDiscordId("123456");
        identifyingProperties.setEpicGamesId("12344");
        var identifier = Utils.identify("userId", identifyingProperties);
        final var result = alliance
                .removeUserIdentifiers(identifier, EMAIL, DISCORD_ID)
                .get(5, TimeUnit.SECONDS);

        Assertions.assertTrue(resultIsSuccessful(result));
    }

    private static boolean resultIsSuccessful(ExecutionResult<?> result) {
        return !(result instanceof FailureResult || result instanceof FailureResponseResult<?>);
    }
}