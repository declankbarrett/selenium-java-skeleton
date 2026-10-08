package pages;

import config.ConfigReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

/**
 * Users list page of the Test Application ({@code appBaseUrl}/index.html), including the
 * search box, Position filter and Clear button added by the "search and filter users" story.
 */
public class UsersListPage extends BasePage {

    private static final String PATH = "/index.html";
    private static final int USER_ROW_CELLS = 3;

    // Search/filter IDs are the proposed contract from the test plan (open question Q9) - confirm with the developer.
    private final By searchInput = By.id("users-search");
    private final By positionFilter = By.id("position-filter");
    private final By clearButton = By.id("clear-filters");
    private final By noResultsMessage = By.id("no-users-message");

    private final By usersTable = By.id("users-table");
    private final By tableRows = By.cssSelector("#users-table-body tr");
    private final By rowCells = By.cssSelector("td");
    private final By viewButton = By.cssSelector(".btn-primary");
    private final By updateButton = By.cssSelector(".btn-secondary");
    private final By removeButton = By.cssSelector(".btn-danger");

    /** One user row as rendered in the table. */
    public record UserRow(String fullName, String email, String position) {}

    public UsersListPage open() {
        String url = ConfigReader.get("appBaseUrl").replaceAll("/+$", "") + PATH;
        log.info("Opening users list: {}", url);
        driver.get(url);
        return this;
    }

    /** Waits for the table and the initial (unfiltered) load to render at least one user. */
    public boolean isLoaded() {
        WaitUtils.waitForUrlContains(driver, PATH);
        waitForElementVisible(usersTable);
        return waitForRows(rows -> !rows.isEmpty());
    }

    /** Types into the search box without pressing Enter, so only the debounce can trigger a search. */
    public void search(String text) {
        log.info("Searching users for '{}'", text);
        enterText(searchInput, text);
    }

    public String getSearchText() {
        return waitForElementVisible(searchInput).getDomProperty("value");
    }

    /** Waits for the asynchronously loaded option to exist before selecting it. */
    public void selectPosition(String position) {
        log.info("Filtering users by position '{}'", position);
        newWait().until(d -> getPositionOptions().contains(position));
        new Select(waitForElementVisible(positionFilter)).selectByVisibleText(position);
    }

    public String getSelectedPosition() {
        newWait().until(d -> !getPositionOptions().isEmpty());
        return new Select(waitForElementVisible(positionFilter)).getFirstSelectedOption().getText().trim();
    }

    public List<String> getPositionOptions() {
        return new Select(waitForElementVisible(positionFilter)).getOptions().stream()
                .map(option -> option.getText().trim())
                .toList();
    }

    /** Positions are loaded asynchronously; waits until more than the default option exists. */
    public boolean waitForPositionOptionsLoaded() {
        try {
            return newWait().until(d -> getPositionOptions().size() > 1);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clearFilters() {
        log.info("Clearing search and position filter");
        click(clearButton);
    }

    public String getNoResultsMessage() {
        return getText(noResultsMessage);
    }

    /** Waits for the no-results message; returns false if it does not appear within the timeout. */
    public boolean isNoResultsMessageShown() {
        try {
            return waitForElementVisible(noResultsMessage).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** User rows currently rendered (rows with fewer cells, e.g. a no-results row, are ignored). */
    public List<UserRow> getUserRows() {
        List<UserRow> users = new ArrayList<>();
        for (WebElement row : driver.findElements(tableRows)) {
            List<WebElement> cells = row.findElements(rowCells);
            if (cells.size() >= USER_ROW_CELLS) {
                users.add(new UserRow(cells.get(0).getText().trim(), cells.get(1).getText().trim(),
                        cells.get(2).getText().trim()));
            }
        }
        return users;
    }

    public List<String> getListedEmails() {
        return getUserRows().stream().map(UserRow::email).toList();
    }

    /**
     * Waits until the rendered rows satisfy the condition. Used after typing/filtering so a check
     * never passes on the stale table that is still shown before the debounced request returns.
     */
    public boolean waitForRows(Predicate<List<UserRow>> condition) {
        try {
            return newWait().until(d -> condition.test(getUserRows()));
        } catch (TimeoutException e) {
            log.warn("Rows did not reach the expected state; last rows: {}", safeRows());
            return false;
        }
    }

    public ViewUserPage viewUser(String email) {
        log.info("Viewing user {}", email);
        clickInRow(email, viewButton);
        return new ViewUserPage();
    }

    public UpdateUserPage updateUser(String email) {
        log.info("Opening update form for {}", email);
        clickInRow(email, updateButton);
        return new UpdateUserPage();
    }

    public void removeUser(String email) {
        log.info("Removing user {}", email);
        clickInRow(email, removeButton);
    }

    private void clickInRow(String email, By button) {
        newWait().until(d -> {
            for (WebElement row : d.findElements(tableRows)) {
                List<WebElement> cells = row.findElements(rowCells);
                if (cells.size() >= USER_ROW_CELLS && email.equals(cells.get(1).getText().trim())) {
                    row.findElement(button).click();
                    return true;
                }
            }
            return false;
        });
    }

    private List<UserRow> safeRows() {
        try {
            return getUserRows();
        } catch (StaleElementReferenceException e) {
            return List.of();
        }
    }

    private WebDriverWait newWait() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getTimeout()));
        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(NoSuchElementException.class);
        return wait;
    }
}
