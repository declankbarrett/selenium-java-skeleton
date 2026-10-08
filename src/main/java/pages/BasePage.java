package pages;

import driver.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

/**
 * Parent class for all page objects. Holds the common, reusable interactions so that
 * individual pages only describe their locators and business actions.
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();
    }

    protected void click(By locator) {
        log.debug("Clicking {}", locator);
        try {
            WaitUtils.waitForClickable(driver, locator).click();
        } catch (StaleElementReferenceException e) {
            // The page re-rendered between finding and clicking the element - find it again and retry once
            log.debug("Stale element {}, retrying click", locator);
            WaitUtils.waitForClickable(driver, locator).click();
        }
    }

    protected void enterText(By locator, String text) {
        log.debug("Entering text into {}", locator);
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForElementVisible(locator).getText().trim();
    }

    /** Returns true if the element is present and visible right now (does not wait). */
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected WebElement waitForElementVisible(By locator) {
        return WaitUtils.waitForVisible(driver, locator);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
