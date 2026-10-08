package driver;

import config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Creates and stores one WebDriver per thread.
 *
 * <p>Using {@link ThreadLocal} keeps each test thread isolated, which makes the framework ready for
 * parallel execution later on without any changes to page objects or step definitions.
 */
public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {}

    public static void initDriver() {
        if (DRIVER.get() != null) {
            return;
        }
        BrowserType browser = BrowserType.from(ConfigReader.getBrowser());
        boolean headless = ConfigReader.isHeadless();
        LOG.info("Starting {} browser (headless={})", browser, headless);

        WebDriver driver = switch (browser) {
            case CHROME -> createChrome(headless);
            case EDGE -> createEdge(headless);
            case FIREFOX -> createFirefox(headless);
        };

        // Explicit waits are used throughout the framework, so implicit wait stays at zero.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        if (!headless) {
            driver.manage().window().maximize();
        }
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialised. Call DriverFactory.initDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            LOG.info("Closing browser");
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }

    private static WebDriver createChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Stop Chrome's password manager pop-ups from blocking the page during login tests
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        options.addArguments("--disable-notifications", "--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createEdge(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-notifications", "--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return new EdgeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless", "--width=1920", "--height=1080");
        }
        return new FirefoxDriver(options);
    }
}
