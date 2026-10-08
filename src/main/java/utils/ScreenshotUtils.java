package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/** Captures screenshots and saves them under {@code target/screenshots}. */
public final class ScreenshotUtils {

    private static final Logger LOG = LogManager.getLogger(ScreenshotUtils.class);
    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private ScreenshotUtils() {}

    /** Takes a screenshot, writes it to disk and returns the raw bytes (useful for report attachments). */
    public static byte[] capture(WebDriver driver, String name) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        String fileName = name.replaceAll("[^a-zA-Z0-9-_]", "_") + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png";
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            Path file = SCREENSHOT_DIR.resolve(fileName);
            Files.write(file, screenshot);
            LOG.info("Screenshot saved: {}", file.toAbsolutePath());
        } catch (IOException e) {
            LOG.warn("Could not save screenshot '{}': {}", fileName, e.getMessage());
        }
        return screenshot;
    }
}
