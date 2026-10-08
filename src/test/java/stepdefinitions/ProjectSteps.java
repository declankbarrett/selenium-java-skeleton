package stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import driver.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AddProjectPage;
import pages.AddUsersToProjectPage;
import pages.ProjectsListPage;
import utils.WaitUtils;

public class ProjectSteps {

    private final String newProjectName = "Automation Platform " + UUID.randomUUID();

    @Given("I open the new project form")
    public void iOpenTheNewProjectForm() {
        new AddProjectPage().open();
    }

    @When("I submit the empty project form")
    public void iSubmitTheEmptyProjectForm() {
        new AddProjectPage().submit();
    }

    @Then("the project form validation error should be displayed")
    public void theProjectFormValidationErrorShouldBeDisplayed() {
        assertTrue(new AddProjectPage().isValidationErrorVisible(),
                "The empty project form did not display a validation error");
    }

    @When("I create a new project")
    public void iCreateANewProject() {
        new AddProjectPage().createProject(
                newProjectName, "Commercial", "TypeScript", "2025-01-15", "Anna Nowak");
        WaitUtils.waitForUrlContains(DriverFactory.getDriver(), "add-users-to-project.html?projectId=");
    }

    @Then("the project member selection page should show the new project")
    public void theProjectMemberSelectionPageShouldShowTheNewProject() {
        AddUsersToProjectPage addUsersToProjectPage = new AddUsersToProjectPage();
        assertTrue(addUsersToProjectPage.getProjectName().contains(newProjectName),
                "The project member selection page showed the wrong project");
    }

    @When("I confirm the project member selection")
    public void iConfirmTheProjectMemberSelection() {
        AddUsersToProjectPage addUsersToProjectPage = new AddUsersToProjectPage();
        addUsersToProjectPage.confirm();
    }

    @Then("the new project should appear in the projects list")
    public void theNewProjectShouldAppearInTheProjectsList() {
        assertTrue(new ProjectsListPage().isProjectListed(newProjectName),
                "The newly created project was not listed");
    }
}
