package stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.ProjectsListPage;
import pages.UsersListPage;

/** Generic steps that are not tied to a single feature. */
public class CommonSteps {

    @Given("I open the users page")
    public void iOpenTheUsersPage() {
        new UsersListPage().open();
    }

    @Given("I open the projects page")
    public void iOpenTheProjectsPage() {
        new ProjectsListPage().open();
    }

    @Then("the users page should be displayed")
    public void theUsersPageShouldBeDisplayed() {
        assertTrue(new UsersListPage().isLoaded(), "Users page was not displayed");
    }

    @Then("the projects page should be displayed")
    public void theProjectsPageShouldBeDisplayed() {
        assertTrue(new ProjectsListPage().isLoaded(), "Projects page was not displayed");
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        assertEquals(expectedTitle, new UsersListPage().getPageTitle());
    }
}
