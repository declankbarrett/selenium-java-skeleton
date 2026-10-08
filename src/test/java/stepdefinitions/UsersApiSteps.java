package stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import api.UsersApiClient;
import api.UsersApiClient.ApiResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.openqa.selenium.json.JsonException;
import support.UserSearchData;

/** API steps for the optional search/position parameters of GET /users and GET /users/positions. */
public class UsersApiSteps {

    private final UsersApiClient api = new UsersApiClient();

    private ApiResponse response;
    private String request;

    @When("I request the users API without parameters")
    public void iRequestTheUsersApiWithoutParameters() {
        request = "GET /users";
        response = api.getAllUsers();
    }

    /**
     * An empty search or position is not sent, so each parameter can be exercised alone. Regex (not {string})
     * so a backslash in the value is passed through literally instead of being read as an escape.
     */
    @When("^I request the users API with search \"(.*)\" and position \"(.*)\"$")
    public void iRequestTheUsersApiWithSearchAndPosition(String search, String position) {
        request = "GET /users search='" + search + "' position='" + position + "'";
        response = api.getUsers(search, position);
    }

    @When("I request the user positions API")
    public void iRequestTheUserPositionsApi() {
        request = "GET /users/positions";
        response = api.getPositions();
    }

    @When("I request the seeded user {string} by id from the users API")
    public void iRequestTheSeededUserById(String email) {
        long id = idOf(email);
        request = "GET /users/" + id;
        response = api.getUser(id);
    }

    @When("I request the users of project {long} from the users API")
    public void iRequestTheUsersOfProject(long projectId) {
        request = "GET /users/projects/" + projectId;
        response = api.getUsersInProject(projectId);
    }

    @Then("the API response status should be {int}")
    public void theApiResponseStatusShouldBe(int status) {
        assertEquals(status, response.status(), request + " status; body: " + response.body());
    }

    @Then("the API response should be a JSON list")
    public void theApiResponseShouldBeAJsonList() {
        Object json;
        try {
            json = response.asJson();
        } catch (JsonException e) {
            throw new AssertionError(request + " did not return JSON: " + response.body(), e);
        }
        assertInstanceOf(List.class, json, request + " should return a JSON array: " + response.body());
    }

    @Then("the API response should be an empty list")
    public void theApiResponseShouldBeAnEmptyList() {
        theApiResponseShouldBeAJsonList();
        assertTrue(response.asList().isEmpty(), request + " should return []: " + response.body());
    }

    @Then("the API response should include all seeded users")
    public void theApiResponseShouldIncludeAllSeededUsers() {
        assertTrue(emails().containsAll(UserSearchData.SEED_EMAILS),
                request + " should include " + UserSearchData.SEED_EMAILS + " but returned " + emails());
    }

    @Then("the API response should include {string}")
    public void theApiResponseShouldInclude(String email) {
        assertTrue(emails().contains(email), request + " should include " + email + " but returned " + emails());
    }

    @Then("the API response should not include {string}")
    public void theApiResponseShouldNotInclude(String email) {
        assertFalse(emails().contains(email), request + " should not include " + email + " but returned " + emails());
    }

    /** Empty criteria are ignored; a vacuous pass on [] is intended for the no-match/injection cases. */
    @Then("^every user in the API response should match search \"(.*)\" and position \"(.*)\"$")
    public void everyUserShouldMatchSearchAndPosition(String search, String position) {
        theApiResponseShouldBeAJsonList();
        for (Map<String, Object> user : response.asList()) {
            if (!search.isEmpty()) {
                assertTrue(UserSearchData.userMatchesSearch(user, search),
                        request + " returned a user not matching the search: " + user);
            }
            if (!position.isEmpty()) {
                assertEquals(position, UserSearchData.field(user, "position"),
                        request + " returned a user with another position: " + user);
            }
        }
    }

    @Then("the API response should be the user {string}")
    public void theApiResponseShouldBeTheUser(String email) {
        Map<String, Object> user = response.asMap();
        assertEquals(email, UserSearchData.field(user, "email"), request + " returned " + user);
        assertEquals(idOf(email), ((Number) user.get("id")).longValue(), request + " id");
    }

    @Then("the users API without parameters should still return all seeded users")
    public void theUsersApiShouldStillReturnAllSeededUsers() {
        iRequestTheUsersApiWithoutParameters();
        theApiResponseStatusShouldBe(200);
        theApiResponseShouldIncludeAllSeededUsers();
    }

    @Then("the positions response should list each existing user position exactly once")
    public void thePositionsResponseShouldListEachExistingPositionOnce() {
        theApiResponseShouldBeAJsonList();
        List<String> positions = response.asRawList().stream().map(UsersApiSteps::positionOf).toList();
        assertEquals(new LinkedHashSet<>(positions).size(), positions.size(), "Duplicate positions: " + positions);

        ApiResponse all = api.getAllUsers();
        assertEquals(200, all.status(), "GET /users status");
        Set<String> existing = new TreeSet<>();
        all.asList().forEach(user -> existing.add(UserSearchData.field(user, "position")));
        assertEquals(existing, new TreeSet<>(positions), "Positions should equal the distinct positions of all users");
    }

    private Set<String> emails() {
        theApiResponseShouldBeAJsonList();
        Set<String> emails = new TreeSet<>();
        response.asList().forEach(user -> emails.add(UserSearchData.field(user, "email")));
        return emails;
    }

    private long idOf(String email) {
        return api.getAllUsers().asList().stream()
                .filter(user -> email.equals(UserSearchData.field(user, "email")))
                .map(user -> ((Number) user.get("id")).longValue())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Seeded user not found: " + email));
    }

    // The ticket does not fix the response shape: accept ["Test Engineer", ...] or [{"position": "Test Engineer"}, ...].
    @SuppressWarnings("unchecked")
    private static String positionOf(Object item) {
        if (item instanceof Map<?, ?> map) {
            return UserSearchData.field((Map<String, Object>) map, "position");
        }
        return String.valueOf(item);
    }
}
