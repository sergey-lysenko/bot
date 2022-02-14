package works.lysenko.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import works.lysenko.enums.Platform;

@SuppressWarnings("javadoc")
public class WebDrivers {

	public static WebDriver get() {
		return get(Platform.CHROME, false);
	}

	public static WebDriver get(Platform browser) {
		return get(browser, false);
	}

	public static WebDriver get(Platform browser, boolean maximize) {
		WebDriver d = null;
		switch (browser) {
		case FIREFOX:
			WebDriverManager.firefoxdriver().setup();
			d = new FirefoxDriver(getFireFoxOptions());
			break;
		case OPERA:
			WebDriverManager.operadriver().setup();
			d = new OperaDriver(getOperaOptions());
			break;
		case EDGE:
			WebDriverManager.edgedriver().setup();
			d = new EdgeDriver(getEdgeOptions());
			break;
		case SAFARI:
			WebDriverManager.safaridriver().setup();
			d = new SafariDriver(getSafariOptions());
			break;
		case CHROME:
			WebDriverManager.chromedriver().setup();
			d = new ChromeDriver(getChromeOptions());
			// d = WebDriverManager.chromedriver().browserInDockerAndroid().create();
			break;
		default:
			System.out.println("Browser not defined, testing is not possible");
			System.exit(3);
		}
		if (maximize)
			d.manage().window().maximize();
		return d;
	}

	private static ChromeOptions getChromeOptions() {
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
		return options;
	}

	private static FirefoxOptions getFireFoxOptions() {
		FirefoxOptions options = new FirefoxOptions();
		options.setCapability("devtools.console.stdout.content", true);
		return options;
	}

	private static EdgeOptions getEdgeOptions() {
		EdgeOptions options = new EdgeOptions();
		return options;
	}

	private static OperaOptions getOperaOptions() {
		OperaOptions options = new OperaOptions();
		return options;
	}

	private static SafariOptions getSafariOptions() {
		SafariOptions options = new SafariOptions();
		return options;
	}

}