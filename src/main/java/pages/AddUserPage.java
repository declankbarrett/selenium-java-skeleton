package pages;

import config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

/** New user form page. */
public class AddUserPage extends BasePage {

    private final By nameInput = By.id("name");
    private final By surnameInput = By.id("surname");
    private final By emailInput = By.id("email");
    private final By positionSelect = By.id("position");
    private final By createUserButton = By.id("create-user-button");
    private final By validationError = By.id("validationError");

    public AddUserPage open() {
        driver.get(ConfigReader.getBaseUrl() + "/add-user.html");
        waitForElementVisible(createUserButton);
        return this;
    }

    public void createUser(String name, String surname, String email, String position) {
        enterText(nameInput, name);
        enterText(surnameInput, surname);
        enterText(emailInput, email);
        new Select(waitForElementVisible(positionSelect)).selectByVisibleText(position);
        submit();
    }

    public void submit() {
        click(createUserButton);
    }

    public boolean isValidationErrorVisible() {
        return waitForElementVisible(validationError).isDisplayed();
    }
}
