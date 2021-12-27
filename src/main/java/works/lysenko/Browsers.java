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

import works.lysenko.utils.Browser;
import works.lysenko.utils.WebDrivers;

@SuppressWarnings("javadoc")
public class Browsers {

	public static List<Browser> available() {
		List<Browser> browsers = load();
		if (browsers.size() == 0) {
			browsers = scan();
			save(browsers);
		}
		return browsers;
	}

	public static void reset() {
		new File(STORED_BROWSERS_FILE).delete();
	}

	private static List<Browser> scan() {
		List<Browser> browsers = new LinkedList<Browser>();
		WebDriver d = null;
		for (Browser b : Browser.values()) {
			try {
				d = WebDrivers.get(b);
				browsers.add(b);
				d.close();
			} catch (Exception e) {
				// d.close();
			}
		}
		return browsers;
	}

	private static List<Browser> load() {
		List<String> browserNames = null;
		try {
			browserNames = Arrays.asList(Files.readString(Paths.get(STORED_BROWSERS_FILE)).split(",", -1));
		} catch (IOException e) {
			browserNames = new LinkedList<String>();
		}
		List<Browser> browsers = new LinkedList<Browser>();
		for (String n : browserNames) {
			browsers.add(Browser.get(n));
		}
		return browsers;
	}

	private static void save(List<Browser> browsers) {
		List<String> browserNames = new LinkedList<String>();
		for (Browser b : browsers) {
			browserNames.add(b.getName());
		}
		new File(STORED_BROWSERS_FILE).getParentFile().mkdirs(); // Create parent directory
		try {
			Files.write(Paths.get(STORED_BROWSERS_FILE), StringUtils.join(browserNames, ',').getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
