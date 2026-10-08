package stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.HomePage;
import pages.LoginPage;

public class NavigationSteps {

    private final HomePage homePage = new HomePage();

    @Given("I am viewing my shopping cart")
    public void iAmViewingMyShoppingCart() {
        homePage.openCart();
    }

    @When("I navigate to the dashboard")
    public void iNavigateToTheDashboard() {
        homePage.navigateToDashboard();
    }

    @Then("the dashboard should be displayed")
    public void theDashboardShouldBeDisplayed() {
        assertTrue(homePage.isLoaded(), "Dashboard was not displayed");
        assertEquals("Products", homePage.getHeaderTitle());
    }

    @When("I log out")
    public void iLogOut() {
        homePage.logout();
    }

    @Then("I should be returned to the login page")
    public void iShouldBeReturnedToTheLoginPage() {
        assertTrue(new LoginPage().isLoaded(), "Login page was not displayed after logout");
    }
}
