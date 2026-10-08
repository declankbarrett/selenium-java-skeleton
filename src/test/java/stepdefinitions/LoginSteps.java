package stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import config.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.HomePage;
import pages.LoginPage;

public class LoginSteps {

    private final LoginPage loginPage = new LoginPage();

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage.open();
    }

    @Given("I am logged into the application")
    public void iAmLoggedIntoTheApplication() {
        loginPage.open();
        iEnterValidCredentials();
        iShouldBeLoggedInSuccessfully();
    }

    @When("I enter valid credentials")
    public void iEnterValidCredentials() {
        loginPage.loginAs(ConfigReader.get("username"), ConfigReader.get("password"));
    }

    @When("I log in with username {string} and password {string}")
    public void iLogInWith(String username, String password) {
        loginPage.loginAs(username, password);
    }

    @Then("I should be logged in successfully")
    public void iShouldBeLoggedInSuccessfully() {
        assertTrue(new HomePage().isLoaded(), "User was not logged in - dashboard not displayed");
    }

    @Then("I should see the login error {string}")
    public void iShouldSeeTheLoginError(String expectedError) {
        String actualError = loginPage.getErrorMessage();
        assertTrue(actualError.contains(expectedError),
                "Expected error containing '" + expectedError + "' but was '" + actualError + "'");
    }
}
