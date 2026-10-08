package stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import api.UsersApiClient;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.UpdateUserPage;
import pages.UsersListPage;
import pages.UsersListPage.UserRow;
import pages.ViewUserPage;
import support.UserSearchData;

/** UI steps for searching and filtering the Test Application users list. */
public class UserSearchSteps {

    private static final Logger log = LogManager.getLogger(UserSearchSteps.class);

    private final UsersListPage usersList = new UsersListPage();
    private final UsersApiClient api = new UsersApiClient();

    private String token;
    private String firstTokenEmail;
    private String secondTokenEmail;
    private final List<Long> createdUserIds = new ArrayList<>();
    private ViewUserPage viewUserPage;
    private UpdateUserPage updateUserPage;

    @Given("I am on the users list")
    public void iAmOnTheUsersList() {
        usersList.open();
        assertTrue(usersList.isLoaded(), "Users list did not load any users");
    }

    @Given("two users sharing a unique search token exist with position {string}")
    public void twoUsersSharingAUniqueSearchTokenExist(String position) {
        token = UserSearchData.CREATED_EMAIL_PREFIX + UUID.randomUUID().toString().substring(0, 8);
        firstTokenEmail = token + ".a@test.com";
        secondTokenEmail = token + ".b@test.com";
        createdUserIds.add(api.createUser("Marta", "Lis", firstTokenEmail, position));
        createdUserIds.add(api.createUser("Ola", "Lis", secondTokenEmail, position));
    }

    @Given("I have filtered the users list to the shared token and position {string}")
    public void iHaveFilteredTheUsersListToTheSharedTokenAndPosition(String position) {
        iAmOnTheUsersList();
        usersList.search(token);
        usersList.selectPosition(position);
        Set<String> expected = Set.of(firstTokenEmail, secondTokenEmail);
        assertTrue(usersList.waitForRows(rows -> emailsOf(rows).equals(expected)),
                "Expected exactly the two token users " + expected + " but saw " + usersList.getListedEmails());
    }

    @Given("I have searched the users list for {string} and filtered by position {string}")
    public void iHaveSearchedAndFiltered(String search, String position) {
        iAmOnTheUsersList();
        usersList.search(search);
        usersList.selectPosition(position);
        everyListedUserShouldMatchTheSearchAndHavePosition(search, position);
    }

    @When("I search the users list for {string}")
    public void iSearchTheUsersListFor(String text) {
        usersList.search(text);
    }

    @When("I filter the users list by position {string}")
    public void iFilterTheUsersListByPosition(String position) {
        usersList.selectPosition(position);
    }

    @When("I clear the users search")
    public void iClearTheUsersSearch() {
        usersList.clearFilters();
    }

    @When("I view the first token user from the filtered list")
    public void iViewTheFirstTokenUser() {
        viewUserPage = usersList.viewUser(firstTokenEmail);
    }

    @When("I open the update form for the first token user from the filtered list")
    public void iOpenTheUpdateFormForTheFirstTokenUser() {
        updateUserPage = usersList.updateUser(firstTokenEmail);
    }

    @When("I remove the first token user from the filtered list")
    public void iRemoveTheFirstTokenUser() {
        usersList.removeUser(firstTokenEmail);
    }

    // The "every listed user" checks wait for the condition, so they never pass on the stale
    // table shown before the debounced request returns. Follow-up include/exclude checks rely on that.
    @Then("every listed user should match the search {string}")
    public void everyListedUserShouldMatchTheSearch(String search) {
        assertTrue(usersList.waitForRows(rows -> !rows.isEmpty()
                        && rows.stream().allMatch(r -> UserSearchData.rowMatchesSearch(r.fullName(), r.email(), search))),
                "Expected a non-empty list where every user matches '" + search + "' but saw " + usersList.getUserRows());
    }

    @Then("every listed user should have position {string}")
    public void everyListedUserShouldHavePosition(String position) {
        assertTrue(usersList.waitForRows(rows -> !rows.isEmpty()
                        && rows.stream().allMatch(r -> position.equals(r.position()))),
                "Expected a non-empty list where every user has position '" + position + "' but saw "
                        + usersList.getUserRows());
    }

    @Then("every listed user should match the search {string} and have position {string}")
    public void everyListedUserShouldMatchTheSearchAndHavePosition(String search, String position) {
        assertTrue(usersList.waitForRows(rows -> !rows.isEmpty() && rows.stream().allMatch(r ->
                        UserSearchData.rowMatchesSearch(r.fullName(), r.email(), search) && position.equals(r.position()))),
                "Expected every user to match '" + search + "' with position '" + position + "' but saw "
                        + usersList.getUserRows());
    }

    @Then("the users list should include {string}")
    public void theUsersListShouldInclude(String email) {
        assertTrue(usersList.waitForRows(rows -> emailsOf(rows).contains(email)),
                "Expected " + email + " to be listed but saw " + usersList.getListedEmails());
    }

    @Then("the users list should not include {string}")
    public void theUsersListShouldNotInclude(String email) {
        assertFalse(usersList.getListedEmails().contains(email),
                "Expected " + email + " not to be listed but saw " + usersList.getListedEmails());
    }

    @Then("the users list should include all seeded users")
    public void theUsersListShouldIncludeAllSeededUsers() {
        assertTrue(usersList.waitForRows(rows -> emailsOf(rows).containsAll(UserSearchData.SEED_EMAILS)),
                "Expected all seeded users " + UserSearchData.SEED_EMAILS + " but saw " + usersList.getListedEmails());
    }

    @Then("the listed users should match the API results for search {string}")
    public void theListedUsersShouldMatchTheApiResultsForSearch(String search) {
        UsersApiClient.ApiResponse response = api.getUsers(search, null);
        assertEquals(200, response.status(), "GET /users?search=" + search + " status");
        Set<String> apiEmails = emailsOfUsers(response.asList());
        assertTrue(usersList.waitForRows(rows -> new TreeSet<>(emailsOf(rows)).equals(apiEmails)),
                "Expected the listed emails to equal the API result " + apiEmails + " but saw "
                        + usersList.getListedEmails());
    }

    @Then("the position filter should show {string}")
    public void thePositionFilterShouldShow(String position) {
        assertEquals(position, usersList.getSelectedPosition(), "Selected position");
    }

    @Then("the position filter should list each existing position once in alphabetical order")
    public void thePositionFilterShouldListEachExistingPositionOnce() {
        assertTrue(usersList.waitForPositionOptionsLoaded(), "Position options did not load");
        List<String> options = usersList.getPositionOptions();
        assertEquals(UserSearchData.ALL_POSITIONS, options.get(0), "First position option");

        List<String> positions = options.subList(1, options.size());
        assertEquals(new LinkedHashSet<>(positions).size(), positions.size(), "Duplicate positions in " + positions);
        List<String> sorted = positions.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
        assertEquals(sorted, positions, "Position options should be alphabetical");

        UsersApiClient.ApiResponse response = api.getAllUsers();
        assertEquals(200, response.status(), "GET /users status");
        Set<String> existing = new TreeSet<>();
        response.asList().forEach(user -> existing.add(UserSearchData.field(user, "position")));
        assertEquals(existing, new TreeSet<>(positions), "Position options should equal the existing positions");
    }

    @Then("the users search box should be empty")
    public void theUsersSearchBoxShouldBeEmpty() {
        assertEquals("", usersList.getSearchText(), "Search box text");
    }

    @Then("the no users match message should be displayed")
    public void theNoUsersMatchMessageShouldBeDisplayed() {
        assertTrue(usersList.isNoResultsMessageShown(), "No-results message was not shown");
        assertEquals(UserSearchData.NO_RESULTS_MESSAGE, usersList.getNoResultsMessage(), "No-results message");
    }

    @Then("no user rows should be listed")
    public void noUserRowsShouldBeListed() {
        assertTrue(usersList.waitForRows(List::isEmpty),
                "Expected no user rows but saw " + usersList.getUserRows());
    }

    @Then("the user details should show the first token user's email")
    public void theUserDetailsShouldShowTheFirstTokenUsersEmail() {
        assertTrue(viewUserPage.detailsContain(firstTokenEmail),
                "User details did not show " + firstTokenEmail);
    }

    @Then("the update form should be pre-filled with the first token user's email")
    public void theUpdateFormShouldBePrefilled() {
        assertTrue(updateUserPage.isPrefilledWithEmail(firstTokenEmail),
                "Update form was not pre-filled with " + firstTokenEmail);
    }

    @Then("only the second token user should be listed")
    public void onlyTheSecondTokenUserShouldBeListed() {
        assertTrue(usersList.waitForRows(rows -> emailsOf(rows).equals(Set.of(secondTokenEmail))),
                "Expected only " + secondTokenEmail + " but saw " + usersList.getListedEmails());
    }

    @Then("the users search box should still contain the shared token")
    public void theUsersSearchBoxShouldStillContainTheSharedToken() {
        assertEquals(token, usersList.getSearchText(), "Search box text after remove");
    }

    /** Runs before the browser hook (higher order runs first) and only uses the unfiltered API. */
    @After(value = "@data-write", order = 20000)
    public void removeCreatedUsers() {
        if (token == null) {
            return;
        }
        try {
            createdUserIds.forEach(api::deleteUser);
            api.getAllUsers().asList().stream()
                    .filter(user -> UserSearchData.field(user, "email").startsWith(token))
                    .map(user -> ((Number) user.get("id")).longValue())
                    .forEach(api::deleteUser);
        } catch (RuntimeException e) {
            log.warn("Cleanup of users with token {} failed: {}", token, e.getMessage());
        }
    }

    private static Set<String> emailsOf(List<UserRow> rows) {
        Set<String> emails = new TreeSet<>();
        rows.forEach(row -> emails.add(row.email()));
        return emails;
    }

    private static Set<String> emailsOfUsers(List<Map<String, Object>> users) {
        Set<String> emails = new TreeSet<>();
        users.forEach(user -> emails.add(UserSearchData.field(user, "email")));
        return emails;
    }
}
