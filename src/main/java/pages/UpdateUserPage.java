package pages;

import config.ConfigReader;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

/** Update user form of the Test Application (update-user.html). */
public class UpdateUserPage extends BasePage {

    private static final String PATH = "update-user.html";

    private final By emailInput = By.id("email");

    public boolean isLoaded() {
        WaitUtils.waitForUrlContains(driver, PATH);
        return waitForElementVisible(emailInput).isDisplayed();
    }

    /** Waits for the form (pre-filled asynchronously) to show the email; false if it does not. */
    public boolean isPrefilledWithEmail(String email) {
        isLoaded();
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getTimeout()))
                    .until(d -> email.equals(d.findElement(emailInput).getDomProperty("value")));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getEmail() {
        return waitForElementVisible(emailInput).getDomProperty("value");
    }
}
