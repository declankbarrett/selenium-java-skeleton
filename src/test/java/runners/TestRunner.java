package runners;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * Entry point for running all Cucumber features with JUnit 5.
 *
 * <p>Filter scenarios with tags at runtime, e.g. {@code mvn test -Dcucumber.filter.tags="@smoke"}.
 * Reporting plugins are configured in {@code src/test/resources/junit-platform.properties}.
 */
@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepdefinitions,hooks")
public class TestRunner {}
