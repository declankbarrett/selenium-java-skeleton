package pages;

import java.time.Duration;
import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

/** Project member selection page shown after creating a project. */
public class AddUsersToProjectPage extends BasePage {

    private final By projectName = By.id("project-name");
    private final By confirmButton = By.id("confirmation-button");

    public String getProjectName() {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getTimeout()))
                .until(currentDriver -> {
                    String text = currentDriver.findElement(projectName).getText().trim();
                    return text.isEmpty() ? null : text;
                });
    }

    public ProjectsListPage confirm() {
        click(confirmButton);
        WaitUtils.waitForUrlContains(driver, "projects.html");
        return new ProjectsListPage();
    }
}
