package pages;

import org.openqa.selenium.By;
import utils.WaitUtils;

/** Products page shown after login. Acts as the application's "dashboard". */
public class HomePage extends BasePage {

    private static final String DASHBOARD_PATH = "inventory.html";

    private final By pageTitle = By.cssSelector("[data-test='title']");
    private final By inventoryList = By.cssSelector("[data-test='inventory-list']");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By allItemsLink = By.id("inventory_sidebar_link");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");

    public boolean isLoaded() {
        WaitUtils.waitForUrlContains(driver, DASHBOARD_PATH);
        return waitForElementVisible(inventoryList).isDisplayed();
    }

    public String getHeaderTitle() {
        return getText(pageTitle);
    }

    public void openCart() {
        log.info("Opening shopping cart");
        click(cartLink);
        WaitUtils.waitForUrlContains(driver, "cart.html");
    }

    /** Navigates to the dashboard using the side menu, as a real user would. */
    public HomePage navigateToDashboard() {
        log.info("Navigating to dashboard via side menu");
        click(menuButton);
        click(allItemsLink);
        return this;
    }

    public LoginPage logout() {
        log.info("Logging out");
        click(menuButton);
        click(logoutLink);
        return new LoginPage();
    }
}
