package driver;

import java.util.Arrays;

/** Browsers supported by the framework. */
public enum BrowserType {
    CHROME,
    EDGE,
    FIREFOX;

    public static BrowserType from(String name) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported browser '" + name + "'. Supported: " + Arrays.toString(values())));
    }
}
