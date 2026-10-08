package stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.LoginPage;

/** Generic steps that are not tied to a single feature. */
public class CommonSteps {

    @Given("I open the application")
    public void iOpenTheApplication() {
        new LoginPage().open();
    }

    @Then("the login page should be displayed")
    public void theLoginPageShouldBeDisplayed() {
        assertTrue(new LoginPage().isLoaded(), "Login page was not displayed");
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        assertEquals(expectedTitle, new LoginPage().getPageTitle());
    }
}
