package stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AddUserPage;
import pages.UsersListPage;
import pages.ViewUserPage;
import utils.WaitUtils;
import driver.DriverFactory;

public class UserSteps {

    private static final String DEVELOPER_NAME = "Anna Nowak";
    private static final String DEVELOPER_EMAIL = "developer@test.com";

    private final String newUserName = "Selenium";
    private final String newUserSurname = "Automation";
    private final String newUserEmail = "selenium." + UUID.randomUUID() + "@test.com";

    @Then("the users list should contain seeded users")
    public void theUsersListShouldContainSeededUsers() {
        UsersListPage usersListPage = new UsersListPage();
        assertTrue(usersListPage.getUserCount() > 0, "No users were displayed");
        assertTrue(usersListPage.isUserListed(DEVELOPER_NAME), "The seeded developer was not displayed");
    }

    @When("I view the seeded developer")
    public void iViewTheSeededDeveloper() {
        new UsersListPage().viewUser(DEVELOPER_NAME);
    }

    @Then("the developer details should include their email")
    public void theDeveloperDetailsShouldIncludeTheirEmail() {
        ViewUserPage viewUserPage = new ViewUserPage();
        assertTrue(viewUserPage.isLoaded(), "User details were not displayed");
        assertTrue(viewUserPage.getDetails(DEVELOPER_EMAIL).contains(DEVELOPER_EMAIL),
                "The seeded developer email was missing from their details");
    }

    @When("I create a new user")
    public void iCreateANewUser() {
        new UsersListPage().openAddUser()
                .createUser(newUserName, newUserSurname, newUserEmail, "Test Engineer");
        WaitUtils.waitForUrlContains(DriverFactory.getDriver(), "index.html");
    }

    @Then("the new user should appear in the users list")
    public void theNewUserShouldAppearInTheUsersList() {
        assertTrue(new UsersListPage().isUserListed(newUserName + " " + newUserSurname),
                "The newly created user was not listed");
    }

    @Given("I open the new user form")
    public void iOpenTheNewUserForm() {
        new AddUserPage().open();
    }

    @When("I submit the empty user form")
    public void iSubmitTheEmptyUserForm() {
        new AddUserPage().submit();
    }

    @Then("the user form validation error should be displayed")
    public void theUserFormValidationErrorShouldBeDisplayed() {
        assertTrue(new AddUserPage().isValidationErrorVisible(),
                "The empty user form did not display a validation error");
    }
}
