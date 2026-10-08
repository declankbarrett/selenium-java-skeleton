package hooks;

import config.ConfigReader;
import driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ScreenshotUtils;

/** Opens a fresh browser before each scenario and closes it afterwards. */
public class Hooks {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        LOG.info("===== Starting scenario: '{}' [env={}, browser={}] =====",
                scenario.getName(), ConfigReader.getEnvironment(), ConfigReader.getBrowser());
        DriverFactory.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                LOG.error("Scenario failed: '{}' - capturing screenshot", scenario.getName());
                byte[] screenshot = ScreenshotUtils.capture(DriverFactory.getDriver(), scenario.getName());
                scenario.attach(screenshot, "image/png", scenario.getName());
            }
        } catch (Exception e) {
            LOG.warn("Unable to capture screenshot: {}", e.getMessage());
        } finally {
            DriverFactory.quitDriver();
            LOG.info("===== Finished scenario: '{}' - {} =====", scenario.getName(), scenario.getStatus());
        }
    }
}
