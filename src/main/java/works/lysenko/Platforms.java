package works.lysenko;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import works.lysenko.enums.Platform;
import works.lysenko.utils.WebDrivers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static works.lysenko.Common.c;
import static works.lysenko.Constants.FORCED_PLATFORMS;
import static works.lysenko.Constants.STORED_PLATFORMS_FILE;

@SuppressWarnings({"UtilityClass", "FinalClass", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "WeakerAccess"})
public final class Platforms {

    private Platforms() {
    }

    public static List<Platform> available() {
        List<Platform> platforms = load();
        if (platforms.isEmpty()) {
            platforms = scan();
            save(platforms);
        }
        return platforms;
    }

    @SuppressWarnings({"null", "ConstantConditions", "ChainedMethodCall"})
    private static List<Platform> load() {
        List<String> platformNames;

        try {
            platformNames = Arrays.asList(
                    (null == FORCED_PLATFORMS) ? Files.readString(Paths.get(STORED_PLATFORMS_FILE)).split(",", -1)
                            : FORCED_PLATFORMS.split(",", -1));
        } catch (IOException e) {
            platformNames = new LinkedList<>();
        }
        List<Platform> platforms = new LinkedList<>();
        for (String name : platformNames)
            platforms.add(Platform.get(name));
        return platforms;
    }

    @SuppressWarnings({"ChainedMethodCall", "ProhibitedExceptionThrown"})
    public static void reset() {
        if (!(new File(STORED_PLATFORMS_FILE).delete()))
            throw new RuntimeException(c("Unable to delete platforms file '", STORED_PLATFORMS_FILE, "'"));
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ChainedMethodCall", "MagicCharacter", "ImplicitDefaultCharsetUsage", "ProhibitedExceptionThrown", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    private static void save(Iterable<Platform> platforms) {
        Collection<String> platformNames = new LinkedList<>();
        for (Platform p : platforms)
            platformNames.add(p.title());
        new File(STORED_PLATFORMS_FILE).getParentFile().mkdirs(); // Create parent directory
        try {
            Files.write(Paths.get(STORED_PLATFORMS_FILE), StringUtils.join(platformNames, ',').getBytes());
        } catch (IOException e) {
            throw new RuntimeException(c("Unable to write into platforms file '", STORED_PLATFORMS_FILE, "'"));
        }
    }

    @SuppressWarnings({"AccessOfSystemProperties", "ConstantConditions", "HardcodedFileSeparator", "ObjectAllocationInLoop", "NestedTryStatement", "DuplicateStringLiteralInspection", "OverlyBroadCatchBlock", "ProhibitedExceptionThrown", "ThrowCaughtLocally", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    private static List<Platform> scan() {
        List<Platform> platforms = new LinkedList<>();
        WebDriver driver;
        for (Platform platform : Platform.values())
            if (Platform.ANDROID == platform) {
                try {
                    AppiumDriverLocalService service = new AppiumServiceBuilder()
                            .withArgument(() -> "--base-path", "/wd/hub/").withLogFile(new File("appium.log")).build();
                    service.start();
                    File appDir = new File(new File(System.getProperty("user.dir")), ".");
                    File app;
                    try {
                        app = new File(appDir.getCanonicalPath(), "resources/anfang.apk");
                    } catch (IOException e) {
                        throw new RuntimeException(c("Unable to load default app from resources/anfang.apk"));
                    }
                    DesiredCapabilities capabilities = new DesiredCapabilities();
                    capabilities.setCapability("appium:automationName", "UiAutomator2"); //NON-NLS
                    if (null != app)
                        capabilities.setCapability("app", app.getAbsolutePath());
                    capabilities.setCapability("newCommandTimeout", "3000");
                    driver = new AndroidDriver(service.getUrl(), capabilities);
                    platforms.add(Platform.ANDROID);
                    driver.close();
                    service.close();
                } catch (Exception e) {
                    // d.close();
                }
            } else
                try {
                    driver = WebDrivers.get(platform);
                    platforms.add(platform);
                    driver.close();
                } catch (Exception e) {
                    // d.close();
                }
        return platforms;
    }
}
