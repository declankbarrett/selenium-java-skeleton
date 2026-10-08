package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import utils.WaitUtils;

/** User details page of the Test Application (view-user.html). */
public class ViewUserPage extends BasePage {

    private static final String PATH = "view-user.html";

    private final By detailsTable = By.id("view-user-table");
    private final By detailsBody = By.id("view-user-table-body");

    public boolean isLoaded() {
        WaitUtils.waitForUrlContains(driver, PATH);
        return waitForElementVisible(detailsTable).isDisplayed();
    }

    /** Waits for the details (loaded asynchronously) to contain the text; false if they do not. */
    public boolean detailsContain(String text) {
        isLoaded();
        try {
            return WaitUtils.waitForTextPresent(driver, detailsBody, text);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getDetailsText() {
        return getText(detailsBody);
    }
}
