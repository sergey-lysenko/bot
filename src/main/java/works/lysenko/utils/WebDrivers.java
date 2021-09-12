package works.lysenko.utils;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class WebDrivers {

	public static WebDriver get(Browser browser) {
		return get(browser, null, false);
	}

	public static WebDriver get(Browser browser, boolean maximize) {
		return get(browser, null, maximize);
	}

	public static WebDriver get(Browser browser, String profile, boolean maximize) {
		WebDriver d;
		switch (browser) {
		case FIREFOX:
			WebDriverManager.firefoxdriver().setup();
			d = new FirefoxDriver(getFireFoxOptions());
			break;
		case CHROME:
		default:
			WebDriverManager.chromedriver().setup();
			d = new ChromeDriver(getChromeOptions(profile));
		}
		if (maximize)
			d.manage().window().maximize();
		return d;
	}

	private static ChromeOptions getChromeOptions(String profile) {
		// TODO: externalize that
		ChromeOptions options = new ChromeOptions();
		options.addArguments("window-size=1920,1080");
		options.addArguments("lang=en-GB");
		options.addArguments("disable-gpu");
		options.addArguments("no-sandbox");
		options.addArguments("disable-dev-shm-usage");
		options.addArguments("disable-setuid-sandbox");
		options.addArguments("disable-infobars");
		options.addArguments("enable-logging");
		options.addArguments("v=1");
		if (null != profile)
			options.addArguments("user-data-dir=" + profile);
		return options;
	}

	private static FirefoxOptions getFireFoxOptions() {
		// TODO: externalize that
		FirefoxOptions options = new FirefoxOptions();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", "en-GB");
		options.setProfile(profile);
		return options;
	}
}
