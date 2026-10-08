package stepdefinitions;

import io.cucumber.java.en.When;
import pages.ProjectsListPage;
import pages.UsersListPage;

public class NavigationSteps {

    @When("I navigate to the projects page")
    public void iNavigateToTheProjectsPage() {
        new UsersListPage().openProjects();
    }

    @When("I navigate back to the users page")
    public void iNavigateBackToTheUsersPage() {
        new ProjectsListPage().openUsers();
    }
}
