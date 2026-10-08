package pages;

import config.ConfigReader;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/** Users list page of the test application. */
public class UsersListPage extends BasePage {

    private static final By USER_ROWS = By.cssSelector("#users-table-body tr");
    private static final By USERS_TABLE = By.id("users-table-body");

    private final By heading = By.xpath("//h2[normalize-space()='List of the users']");
    private final By addUserLink = By.cssSelector("a[href='add-user.html']");
    private final By projectsLink = By.cssSelector("a[href='projects.html']");

    public UsersListPage open() {
        driver.get(ConfigReader.getBaseUrl() + "/index.html");
        waitForElementVisible(heading);
        return this;
    }

    public boolean isLoaded() {
        return waitForElementVisible(heading).isDisplayed();
    }

    public int getUserCount() {
        return WaitUtils.waitForElementsPresent(driver, USER_ROWS).size();
    }

    public boolean isUserListed(String fullName) {
        WaitUtils.waitForTextPresent(driver, USERS_TABLE, fullName);
        return findUserRow(fullName) != null;
    }

    public ViewUserPage viewUser(String fullName) {
        WebElement row = waitForUserRow(fullName);
        row.findElement(By.cssSelector("button.btn-primary")).click();
        return new ViewUserPage();
    }

    public AddUserPage openAddUser() {
        click(addUserLink);
        return new AddUserPage();
    }

    public ProjectsListPage openProjects() {
        click(projectsLink);
        return new ProjectsListPage();
    }

    private WebElement waitForUserRow(String fullName) {
        WaitUtils.waitForTextPresent(driver, USERS_TABLE, fullName);
        return findUserRow(fullName);
    }

    private WebElement findUserRow(String fullName) {
        List<WebElement> rows = driver.findElements(USER_ROWS);
        return rows.stream()
                .filter(row -> row.getText().contains(fullName))
                .findFirst()
                .orElse(null);
    }
}
