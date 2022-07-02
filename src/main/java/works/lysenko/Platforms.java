package works.lysenko;

import static works.lysenko.Constants.STORED_BROWSERS_FILE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import works.lysenko.enums.Platform;
import works.lysenko.utils.WebDrivers;

@SuppressWarnings("javadoc")
public class Platforms {

	public static List<Platform> available() {
		List<Platform> platforms = load();
		if (platforms.size() == 0) {
			platforms = scan();
			save(platforms);
		}
		return platforms;
	}

	private static List<Platform> load() {
		List<String> browserNames = null;
		try {
			browserNames = Arrays.asList(Files.readString(Paths.get(STORED_BROWSERS_FILE)).split(",", -1));
		} catch (@SuppressWarnings("unused") IOException e) {
			browserNames = new LinkedList<>();
		}
		List<Platform> browsers = new LinkedList<>();
		for (String n : browserNames)
			browsers.add(Platform.get(n));
		return browsers;
	}

	public static void reset() {
		new File(STORED_BROWSERS_FILE).delete();
	}

	private static void save(List<Platform> browsers) {
		List<String> browserNames = new LinkedList<>();
		for (Platform b : browsers)
			browserNames.add(b.title());
		new File(STORED_BROWSERS_FILE).getParentFile().mkdirs(); // Create parent directory
		try {
			Files.write(Paths.get(STORED_BROWSERS_FILE), StringUtils.join(browserNames, ',').getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<Platform> scan() {
		List<Platform> platforms = new LinkedList<>();
		WebDriver d = null;
		for (Platform b : Platform.values())
			if (b.equals(Platform.ANDROID))
				try {
					AppiumDriverLocalService service = new AppiumServiceBuilder()
							.withArgument(() -> "--base-path", "/wd/hub/").withLogFile(new File("appium.log")).build();
					service.start();
					File appDir = new File(new File(System.getProperty("user.dir")), "apps");
					File app = null;
					try {
						app = new File(appDir.getCanonicalPath(), "anfang.apk");
					} catch (IOException e) {
						e.printStackTrace();
					}
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability("appium:automationName", "UiAutomator2");
					if (null != app)
						capabilities.setCapability("app", app.getAbsolutePath());
					d = new AndroidDriver(service.getUrl(), capabilities);
					platforms.add(b);
					d.close();
					service.close();
				} catch (@SuppressWarnings("unused") Exception e) {
					// d.close();
				}
			else
				try {
					d = WebDrivers.get(b);
					platforms.add(b);
					d.close();
				} catch (@SuppressWarnings("unused") Exception e) {
					// d.close();
				}
		return platforms;
	}
}
