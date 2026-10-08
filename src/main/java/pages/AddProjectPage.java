package pages;

import config.ConfigReader;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/** New project form page. */
public class AddProjectPage extends BasePage {

    private final By sectorInputs = By.cssSelector("input[name='sector']");
    private final By nameInput = By.id("name");
    private final By startDateInput = By.id("start-date");
    private final By ownerSelect = By.id("owner-dropdown");
    private final By createProjectButton = By.cssSelector("button[onclick='addProject()']");
    private final By validationError = By.id("validationError");

    public AddProjectPage open() {
        driver.get(ConfigReader.getBaseUrl() + "/add-project.html");
        waitForElementVisible(sectorInputs);
        return this;
    }

    public void createProject(
            String name, String sector, String technology, String startDate, String ownerName) {
        enterText(nameInput, name);
        driver.findElement(By.cssSelector("input[name='sector'][value='" + sector + "']")).click();
        driver.findElement(By.cssSelector("input[id*='technology'][value='" + technology + "']")).click();
        enterText(startDateInput, startDate);
        selectOwner(ownerName);
        click(createProjectButton);
    }

    public void submit() {
        click(createProjectButton);
    }

    public boolean isValidationErrorVisible() {
        return waitForElementVisible(validationError).isDisplayed();
    }

    private void selectOwner(String ownerName) {
        new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getTimeout()))
                .until(currentDriver -> new Select(currentDriver.findElement(ownerSelect))
                        .getOptions().stream()
                        .anyMatch(option -> option.getText().contains(ownerName)));

        Select owners = new Select(driver.findElement(ownerSelect));
        String ownerValue = owners.getOptions().stream()
                .filter(option -> option.getText().contains(ownerName))
                .map(option -> option.getAttribute("value"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No project owner found matching: " + ownerName));
        owners.selectByValue(ownerValue);
    }
}
