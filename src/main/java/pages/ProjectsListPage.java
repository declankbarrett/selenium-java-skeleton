package pages;

import config.ConfigReader;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/** Projects list page of the test application. */
public class ProjectsListPage extends BasePage {

    private static final By PROJECT_ROWS = By.cssSelector("#projects-table-body tr");
    private static final By PROJECTS_TABLE = By.id("projects-table-body");

    private final By heading = By.xpath("//h2[normalize-space()='List of projects']");
    private final By usersLink = By.cssSelector("a[href='index.html']");
    private final By addProjectLink = By.cssSelector("a[href='add-project.html']");

    public ProjectsListPage open() {
        driver.get(ConfigReader.getBaseUrl() + "/projects.html");
        waitForElementVisible(heading);
        return this;
    }

    public boolean isLoaded() {
        return waitForElementVisible(heading).isDisplayed();
    }

    public int getProjectCount() {
        return WaitUtils.waitForElementsPresent(driver, PROJECT_ROWS).size();
    }

    public boolean isProjectListed(String projectName) {
        WaitUtils.waitForTextPresent(driver, PROJECTS_TABLE, projectName);
        return findProjectRow(projectName) != null;
    }

    public AddProjectPage openAddProject() {
        click(addProjectLink);
        return new AddProjectPage();
    }

    public UsersListPage openUsers() {
        click(usersLink);
        return new UsersListPage();
    }

    private WebElement findProjectRow(String projectName) {
        List<WebElement> rows = driver.findElements(PROJECT_ROWS);
        return rows.stream()
                .filter(row -> row.getText().contains(projectName))
                .findFirst()
                .orElse(null);
    }
}
