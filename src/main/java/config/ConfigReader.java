package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Loads environment configuration from {@code environments/<environment>.properties}.
 *
 * <p>The environment is chosen with {@code -Denvironment=local|int|qa} (defaults to {@code local}).
 * Any individual property can be overridden on the command line, e.g. {@code -Dbrowser=firefox}.
 */
public final class ConfigReader {

    private static final Logger LOG = LogManager.getLogger(ConfigReader.class);
    private static final String DEFAULT_ENVIRONMENT = "local";
    private static final Properties PROPERTIES = load();

    private ConfigReader() {}

    public static String getEnvironment() {
        return System.getProperty("environment", DEFAULT_ENVIRONMENT).trim().toLowerCase();
    }

    /** Returns a property value; system properties take precedence over the properties file. */
    public static String get(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing configuration property '" + key + "' for environment '" + getEnvironment() + "'");
        }
        return value.trim();
    }

    public static String getBaseUrl() {
        return get("baseUrl");
    }

    public static String getBrowser() {
        return get("browser");
    }

    public static int getTimeout() {
        return Integer.parseInt(get("timeout"));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty("headless", PROPERTIES.getProperty("headless", "false")));
    }

    private static Properties load() {
        String environment = getEnvironment();
        String path = "environments/" + environment + ".properties";
        Properties properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found on classpath: " + path);
            }
            properties.load(input);
            LOG.info("Loaded configuration for environment '{}'", environment);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read configuration file: " + path, e);
        }
    }
}
