# Selenium Java Skeleton Framework

## Overview

A clean, minimal UI test automation framework built with **Selenium 4**, **Cucumber BDD** and **JUnit 5**.
It is meant to be a starting point for new automation projects and a reference for training and onboarding.

The examples run against the public demo application [Sauce Demo](https://www.saucedemo.com), so the
framework works straight after cloning — no setup of a system under test is needed.

### Purpose

- Show good automation practices (Page Object Model, explicit waits, thread-safe drivers, externalised configuration)
- Keep everything small and readable — no "magic", no deep inheritance trees
- Be easy to extend as a project grows

## Technology Stack

| Tool              | Purpose                                   |
|-------------------|-------------------------------------------|
| Java 21           | Language                                  |
| Maven             | Build and dependency management           |
| Selenium 4        | Browser automation                        |
| Cucumber 7        | BDD / Gherkin feature files               |
| JUnit 5           | Test platform used to run Cucumber        |
| WebDriverManager  | Downloads the right browser driver for you |
| Log4j2            | Console and file logging                  |
| GitHub Actions    | Continuous integration                    |

## Prerequisites

- **Java 21** or newer (`java -version`)
- **Maven 3.9+** (`mvn -version`)
- At least one browser installed: **Chrome** (default), **Edge** or **Firefox**
- Git

## Setup

```shell
git clone https://github.com/declankbarrett/selenium-java-skeleton.git
cd selenium-java-skeleton
mvn clean test-compile
```

Driver binaries are downloaded automatically by WebDriverManager on the first run.

## Running Tests

```shell
# Run everything against the default (local) environment
mvn test

# Choose an environment
mvn test -Denvironment=local
mvn test -Denvironment=int
mvn test -Denvironment=qa

# Run by tag
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@login and not @regression"

# Override any config value from the command line
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge -Dheadless=true
```

Tests can also be run from the IDE by running `runners.TestRunner` or an individual `.feature` file
(with the Cucumber plugin installed).

### Configuration

Each environment has its own file in `src/main/resources/environments/`:

```properties
baseUrl=https://www.saucedemo.com
browser=chrome      # chrome | edge | firefox
timeout=10          # explicit wait timeout in seconds
headless=false      # true in int/qa so they run on CI agents
username=standard_user
password=secret_sauce
```

Lookup order: **`-D` system property → environment properties file**. Default environment is `local`.

> The demo credentials are public. For a real application, never commit credentials — pass them in with
> `-Dusername=... -Dpassword=...` from a secrets store (e.g. GitHub Actions secrets).

### Reports and Logs

After a run, look in `target/`:

| Location                               | Contents                                          |
|----------------------------------------|---------------------------------------------------|
| `target/cucumber-reports/cucumber.html` | Readable HTML report (open in a browser)          |
| `target/cucumber-reports/cucumber.json` | JSON report for third-party reporting tools       |
| `target/cucumber-reports/cucumber.xml`  | JUnit XML report for CI tools                     |
| `target/screenshots/`                  | Screenshots of failed scenarios (also embedded in the HTML report) |
| `target/logs/test-execution.log`       | Full Log4j2 log of the run                        |

## Framework Structure

```
.
├── .github/workflows/tests.yml        # CI pipeline
├── pom.xml                            # Dependencies and build configuration
└── src
    ├── main
    │   ├── java
    │   │   ├── config
    │   │   │   └── ConfigReader.java      # Loads <environment>.properties, supports -D overrides
    │   │   ├── driver
    │   │   │   ├── BrowserType.java       # Supported browsers
    │   │   │   └── DriverFactory.java     # Creates/stores a WebDriver per thread (ThreadLocal)
    │   │   ├── pages
    │   │   │   ├── BasePage.java          # Shared actions: click, enterText, getText, isDisplayed, waits
    │   │   │   ├── LoginPage.java
    │   │   │   └── HomePage.java          # The "dashboard" (products page)
    │   │   └── utils
    │   │       ├── ScreenshotUtils.java   # Saves screenshots to target/screenshots
    │   │       └── WaitUtils.java         # Explicit wait helpers
    │   └── resources
    │       ├── environments               # local / int / qa properties
    │       └── log4j2.xml                 # Logging configuration
    └── test
        ├── java
        │   ├── hooks
        │   │   └── Hooks.java             # Start browser before / screenshot + quit after each scenario
        │   ├── runners
        │   │   └── TestRunner.java        # JUnit 5 suite that runs all Cucumber features
        │   └── stepdefinitions            # Glue code: Gherkin steps -> page object calls
        │       ├── CommonSteps.java
        │       ├── LoginSteps.java
        │       └── NavigationSteps.java
        └── resources
            ├── features                   # Gherkin feature files
            │   ├── availability.feature
            │   ├── login.feature
            │   ├── logout.feature
            │   └── navigation.feature
            └── junit-platform.properties  # Cucumber report plugins
```

### Responsibilities at a glance

- **Feature files** describe *what* is tested, in business language.
- **Step definitions** translate each step into page object calls and hold the **assertions**.
- **Page objects** know *how* to interact with a page (locators + actions). They contain no assertions.
- **BasePage / utils** hold reusable, page-independent code.
- **Hooks** manage the browser lifecycle and failure screenshots.
- **DriverFactory / ConfigReader** are the only places that know about browsers and environments.

### Example tests

| Feature                | Scenario(s)                                     | Tags                       |
|------------------------|-------------------------------------------------|----------------------------|
| Application availability | Application is available                      | `@smoke @availability`     |
| Login                  | Successful login, unsuccessful login (outline)  | `@login @smoke/@regression`|
| Logout                 | Successful logout                               | `@logout @smoke`           |
| Navigation             | Navigate to dashboard, return from cart         | `@navigation @smoke/@regression` |

## Adding New Tests

### 1. Create a feature file

Add a `.feature` file in `src/test/resources/features/`:

```gherkin
@cart
Feature: Shopping cart

  @smoke
  Scenario: Add an item to the cart
    Given I am logged into the application
    When I add "Sauce Labs Backpack" to the cart
    Then the cart badge should show 1
```

Reuse existing steps wherever possible (e.g. `Given I am logged into the application`).

### 2. Create a page object

Add a class in `src/main/java/pages/` that extends `BasePage`. Keep locators private and expose
meaningful actions:

```java
package pages;

import org.openqa.selenium.By;

public class CartPage extends BasePage {

    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");

    public int getItemCount() {
        return Integer.parseInt(getText(cartBadge));
    }
}
```

Guidelines:
- Prefer stable locators: `id`, `data-test` attributes, then CSS. Avoid long XPaths.
- Use the `BasePage` helpers — they already wait for elements, so no `Thread.sleep()`.
- Return the next page object from actions that navigate (e.g. `loginAs()` returns `HomePage`).

### 3. Create step definitions

Add a class in `src/test/java/stepdefinitions/`:

```java
package stepdefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.Then;
import pages.CartPage;

public class CartSteps {

    private final CartPage cartPage = new CartPage();

    @Then("the cart badge should show {int}")
    public void theCartBadgeShouldShow(int expected) {
        assertEquals(expected, cartPage.getItemCount());
    }
}
```

Keep steps thin: call page objects and assert. Page objects get the browser from `DriverFactory`,
so there is no need to pass the driver around.

### 4. Run the tests

```shell
mvn test -Dcucumber.filter.tags="@cart"
```

Undefined steps are reported in the console with a ready-to-paste method snippet.

## CI/CD

`.github/workflows/tests.yml` runs on every push to `main`, on pull requests, and manually
(**Actions → UI Tests → Run workflow**, where you can pick the environment and a tag expression). It:

1. Checks out the code and sets up Java 21 (with Maven cache)
2. Compiles the project
3. Runs the tests headless
4. Uploads reports, screenshots and logs as the `test-results` artifact (even when tests fail)

## Future Enhancements

- **API automation** — add REST Assured for API tests and for fast test data setup
- **Parallel execution** — enable `cucumber.execution.parallel.enabled=true` (the driver is already `ThreadLocal`)
- **Selenium Grid** — add a `RemoteWebDriver` option to `DriverFactory` driven by a `gridUrl` property
- **Docker** — run tests and browsers in containers (e.g. `selenium/standalone-chrome`, docker-compose)
- **Advanced reporting** — Allure or ExtentReports, report publishing to GitHub Pages
- **Dependency injection** — PicoContainer to share state between step definition classes
- **Test data management** — external test data files or builders
- **Secrets** — read credentials from environment variables / a vault
- **Cross-browser CI matrix** — run Chrome, Edge and Firefox in parallel jobs
