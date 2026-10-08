package pages;

import org.openqa.selenium.By;
import utils.WaitUtils;

/** User details page. */
public class ViewUserPage extends BasePage {

    private final By detailsTable = By.id("view-user-table");

    public boolean isLoaded() {
        return waitForElementVisible(detailsTable).isDisplayed();
    }

    public String getDetails(String email) {
        WaitUtils.waitForTextPresent(driver, detailsTable, email);
        return getText(detailsTable);
    }
}
