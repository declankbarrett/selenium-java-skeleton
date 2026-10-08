package pages;

import config.ConfigReader;
import org.openqa.selenium.By;

/** Login page of the demo application (https://www.saucedemo.com). */
public class LoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage open() {
        log.info("Opening login page: {}", ConfigReader.getBaseUrl());
        driver.get(ConfigReader.getBaseUrl());
        waitForElementVisible(loginButton);
        return this;
    }

    public HomePage loginAs(String username, String password) {
        log.info("Logging in as '{}'", username);
        enterText(usernameInput, username);
        enterText(passwordInput, password);
        click(loginButton);
        return new HomePage();
    }

    public boolean isLoaded() {
        return waitForElementVisible(loginButton).isDisplayed();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
