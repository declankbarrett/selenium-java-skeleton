package utils;

import config.ConfigReader;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Small wrapper around {@link WebDriverWait} using the configured timeout. */
public final class WaitUtils {

    private WaitUtils() {}

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return newWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return newWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static boolean waitForUrlContains(WebDriver driver, String fragment) {
        return newWait(driver).until(ExpectedConditions.urlContains(fragment));
    }

    private static WebDriverWait newWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getTimeout()));
    }
}
