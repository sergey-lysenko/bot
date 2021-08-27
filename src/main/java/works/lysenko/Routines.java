package works.lysenko;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * This is a set of static-only code snippets frequenlty reused throughout the
 * test implementation
 * 
 * @author Sergii Lysenko
 *
 */
public class Routines {

	/**
	 * @param s string with either xpath or css locator
	 * @return proper locator object based on contents of source string contents
	 */
	public static By by(String s) {
		if (s.substring(0, 2).equals("//") || s.substring(0, 2).equals("./") || s.substring(0, 3).equals(".//")
				|| s.substring(0, 4).equals("(.//"))
			return By.xpath(s);
		else
			return By.cssSelector(s);
	}

	/**
	 * @param l string locator of element to be clicked on
	 * @param r reference to Run class instance
	 */
	public static void click(String l, Run r) {
		log("Clicking " + l, r);
		find(l, true, r).click(); // Silent find during click
	}

	/**
	 * @param e element to be clicked on
	 * @param r
	 */
	public static void click(WebElement e, Run r) {
		log("Clicking " + e.getTagName() + " element", r);
		e.click();
	}

	/**
	 * @param s string to be colored red or yellow based on presence of [SEVERE] or
	 *          [WARNING] substrings
	 * @return same string with added ANSI coloring escape sequences
	 */
	public static String colorize(String s) {
		if (s.contains("[SEVERE]")) {
			return colorize(s, Color.RED);
		}
		if (s.contains("[WARNING]")) {
			return colorize(s, Color.YELLOW);
		}
		if (s.contains("[NOTICE]")) {
			return colorize(s, Color.CYAN);
		}
		return s;
	}

	/**
	 * @param s string to be colorized
	 * @param c color escape sequence to be applied
	 * @return string surrounded by defined color sequence and RESET
	 */
	public static String colorize(String s, Color c) {
		return c + s + Color.RESET;
	}

	/**
	 * Shortcut for String.format(template, values)
	 * 
	 * @param template
	 * @param values
	 * @return filled in string template
	 */
	public static String fill(String template, Object... values) {
		return String.format(template, values);
	}

	/**
	 * @param l      string locator of target element
	 * @param silent whether to omit log output or not
	 * @param r
	 * @return located element reference
	 */
	public static WebElement find(String l, boolean silent, Run r) {
		if (!silent)
			log("Finding " + l, r);
		return r.driver.findElement(by(l));
	}

	/**
	 * @param l string locator of target element
	 * @param r
	 * @return located element reference
	 */
	public static WebElement find(String l, Run r) {
		return find(l, false, r);
	}

	/**
	 * Single-step location of an element by locator of it's parent and it's own
	 * relative locator vs. to this parent element
	 * 
	 * @param p string absolute locator of parent element
	 * @param c string relative locator of child element
	 * @param r
	 * @return located element reference
	 */
	public static WebElement find(String p, String c, Run r) {
		log("Finding parent " + p + " and child " + c, r);
		return r.driver.findElement(by(p)).findElement(by(c));
	}

	/**
	 * @param l locator of 1-n elements to be found
	 * @param r
	 * @return List of located element references
	 */
	public static List<WebElement> findS(String l, Run r) {
		log("Finding all " + l + " elements", r);
		return r.driver.findElements(by(l));
	}

	/**
	 * Generate random integer in defined range
	 * 
	 * @param l minimum value
	 * @param u maximum value
	 * @return random integer
	 */
	public static int integer(int l, int u) {
		return new RandomDataGenerator().nextInt(l, u);
	}

	/**
	 * Return 'true' with defined probability, or 'false' otherwise
	 * 
	 * @param d probability of 'true' selection
	 * @return random boolean value
	 */
	public static boolean isTrue(double d) {
		return new Random().nextFloat() < d;
	}

	/**
	 * @param l log level, 0 is for no markers, 1-n is for number of '•' markers
	 *          before a string
	 * @param s string of text to be written to log
	 */
	public static void log(int l, String s) {
		log(l, s, (Long) null);
	}

	private static void log(int l, String s, Long t) {
		logConsole(renderLog(0, l, s, t));
	}

	/**
	 * Write a string of text to test execution log with preceding check of browser
	 * console for present errors and eventual output of found browser console logs
	 * 
	 * @param l log level, 0 is for no markers, 1-n is for number of '•' markers
	 *          before a string
	 * @param s string of text to be written to log
	 * @param r
	 */
	public static void log(int l, String s, Run r) {
		log(l, s, r, null);
	}

	/**
	 * Write a string of text to test execution log with preceding check of browser
	 * console for present errors and eventual output of found browser console logs
	 * 
	 * @param l log level, 0 is for no markers, 1-n is for number of '•' markers
	 *          before a string
	 * @param s string of text to be written to log
	 * @param t redefined time of event, or it can be set to null for measurement by
	 *          this method
	 * @param r
	 */
	public static void log(int l, String s, Run r, Long t) {
		if (r.logCount++ == 0)
			logln();
		LogEntries logs;
		if (r.logs.contains(LogType.BROWSER)) {
			logs = r.driver.manage().logs().get(LogType.BROWSER);
			for (LogEntry log : logs) {
				problem("[BROWSER] " + log.toString(), r);
			}
		}
		if (r.logs.contains(LogType.CLIENT)) {

			logs = r.driver.manage().logs().get(LogType.CLIENT);
			for (LogEntry log : logs) {
				problem("[CLIENT] " + log.toString(), r);
			}
		}
		if (r.logs.contains(LogType.DRIVER)) {
			logs = r.driver.manage().logs().get(LogType.DRIVER);
			for (LogEntry log : logs) {
				problem("[DRIVER] " + log.toString(), r);
			}
		}
		// TODO: figure out unavailability of these three types of logs
		// (run.logs.contains(LogType.PERFORMANCE))
		// if (run.logs.contains(LogType.PROFILER))
		// if (run.logs.contains(LogType.SERVER))
		logDo(renderLog((null == r.current) ? 0 : r.current.depth(), l, s, (null == t) ? r.timer() : t), r);
	}

	/**
	 * Output the string into test execution log with default log level (1) without
	 * Run context (which mean that browser logs will not be verified before this
	 * log output).
	 * 
	 * @param s string to be written to the log
	 */
	public static void log(String s) {
		log(1, s);
	}

	/**
	 * Output the string into test execution log with default log level (1)
	 * 
	 * @param s string to be written to the log
	 * @param r
	 */
	public static void log(String s, Run r) {
		log(1, s, r);
	}

	public static void logConsole(String s) {
		System.out.println(s);
	}

	private static void logDo(String s, Run r) {
		logConsole(s);
		r.logWrite(s);
	}

	public static void logln() {
		System.out.println("");
	}

	private static void makeCodeshot(String p, String f, Run r) {
		try {
			Files.writeString(Path.of(p, f + ".html"), r.driver.getPageSource());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Produce a full-screen PNG Screenshot and save it in a location defined by
	 * Constants.DEFAULT_SHOTS_LOCATION
	 * 
	 * @param name of the screenshot
	 * @param r
	 */
	public static void makeScreenshot(String name, Run r) {
		makeScreenshot(null, Constants.DEFAULT_SHOTS_LOCATION, name, r);
	}

	/**
	 * Produce a framed PNG Screenshot of defined element and save it in a location
	 * defined by Constants.DEFAULT_SHOTS_LOCATION
	 * 
	 * @param element to be used for local framed screenshot
	 * @param name    of the screenshot
	 * @param r
	 */
	public static void makeScreenshot(WebElement element, String name, Run r) {
		makeScreenshot(element, Constants.DEFAULT_SHOTS_LOCATION, name, r);
	}

	private static void makeScreenshot(WebElement element, String path, String name, Run r) {
		File src = null;
		if (element == null)
			src = ((TakesScreenshot) r.driver).getScreenshotAs(OutputType.FILE);
		else
			src = ((TakesScreenshot) element).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(src, new File(path + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Simultaneously make a full-screen screenshot and a copy of page html code and
	 * save in a location defined by Constants.DEFAULT_SHOTS_LOCATION
	 * 
	 * @param f name of snapshot
	 * @param r
	 */
	public static void makeSnapshot(String f, Run r) {
		makeScreenshot(null, Constants.DEFAULT_SHOTS_LOCATION, f, r);
		makeCodeshot(Constants.DEFAULT_SHOTS_LOCATION, f, r);
	}

	/**
	 * Open a page defined by protocol and domain
	 * 
	 * @param p protocol
	 * @param u domain
	 * @param r
	 */
	public static void open(String p, String u, Run r) {
		String a = p + "://" + u;
		log("Opening " + a, r);
		r.driver.get(a);
	}

	/**
	 * Open a page defined by domain using https:// protocol
	 * 
	 * @param u domain
	 * @param r
	 */
	public static void openDomain(String u, Run r) {
		log("Opening https://" + u, r);
		r.driver.get("https://" + u);
	}

	private static void problem(String s, Run r) {
		r.problem(s);
	}

	/**
	 * Read contents of the element defined by string locator
	 * 
	 * @param l string locator of an element
	 * @param r
	 * @return result of .getText() for this element
	 */
	public static String read(String l, Run r) {
		log("Reading from " + l, r);
		return r.driver.findElement(by(l)).getText();
	}

	/**
	 * Render log message
	 * 
	 * @param d depth of log (number of added spaces)
	 * @param l level of log (number of dots)
	 * @param s message
	 * @param t if this timestamp is null, massage displayed as is
	 * @return rendered log message
	 */
	public static String renderLog(int d, int l, String s, Long t) {
		StringBuilder sb = new StringBuilder();
		sb.append((null == t) ? "" : renderTime(t) + " "); // timestamp
		sb.append(" ".repeat(Math.abs(d))); // logical scenario level
		sb.append("•".repeat(Math.abs(l))); // logical message level
		sb.append(((0 == l) ? "" : " ")); // optional space
		sb.append(s); // message
		return sb.toString();
	}

	public static String renderTime(Long t) {
		return "[" + new DecimalFormat("0000000.000").format(Double.valueOf(t) / 1000) + "]";
	}

	/**
	 * Repeat defined key given number of times
	 * 
	 * @param k key to be repeated
	 * @param t times to repeat
	 * @return requested sequence
	 */
	public static String repeat(Keys k, int t) {
		String sequence = "";
		for (int i = 0; i < t; i++)
			sequence = sequence + k;
		return sequence;
	}

	/**
	 * Produce BASE64 encoded string screenshot of an element
	 * 
	 * @param l string locator of requested element
	 * @param r
	 * @return BASE64 encoded string screenshot
	 */
	public static String screenShot(String l, Run r) {
		return ((TakesScreenshot) r.driver.findElement(by(l))).getScreenshotAs(OutputType.BASE64);
	}

	/**
	 * Get an element from a list of strings different from defined one. There is no
	 * check that a list contains defined string, only the exclusion of defined
	 * value is performed.
	 * 
	 * @param e string to be excluded from selection
	 * @param l list of string to select from
	 * @return an element from a list of strings which differs from specified one
	 */
	public static String selectOtherStringFromList(String e, List<String> l) {
		List<String> copy = new ArrayList<>(l);
		copy.remove(e);
		return selectSomeStringFromList(copy);
	}

	/**
	 * Get an element from a list of WebElement objects.
	 * 
	 * @param l list of WebElement objects to select from
	 * @return a WebElement object from a list
	 */
	public static WebElement selectSomeElementFromList(List<WebElement> l) {
		return l.get(new Random().nextInt(l.size()));
	}

	/**
	 * Select random element from a set
	 * 
	 * @param s set to select from
	 * @return random Object from a set
	 */
	public static Object selectSomeElementFromSet(Set<?> s) {
		// Only applicable for small sets, otherwise creates a big overhead
		// TODO: optimize
		return s.toArray()[new Random().nextInt(s.size())];
	}

	/**
	 * Get an element from a list of strings.
	 * 
	 * @param l list of string to select from
	 * @return an element from a list of strings
	 */
	public static String selectSomeStringFromList(List<String> l) {
		return l.get(new Random().nextInt(l.size()));
	}

	/**
	 * Send keys to element
	 * 
	 * @param l string locator of target input element
	 * @param s CharSequence to send
	 */
	public static void sendKeys(String l, CharSequence s, Run r) {
		find(l, r).sendKeys(s);
	}

	/**
	 * Sleep during defined amount of milliseconds with record in the test log
	 * 
	 * @param ms amount of milliseconds to pause for
	 */
	public static void sleep(long ms) {
		if (ms < Constants.SILENT_SLEEPING_TRESHHOLD)
			Routines.log(0, colorize("[WARNING]: " + "Sleeping for less then " + Constants.SILENT_SLEEPING_TRESHHOLD
					+ " ms is better to be perfomed in silent mode"));
		sleep(ms, false);
	}

	/**
	 * Sleep during defined amount of milliseconds with optional bypassing of output
	 * to the test log
	 * 
	 * @param ms     amount of milliseconds to pause for
	 * @param silent whether to bypass output to test log. Useful for short delays
	 */
	public static void sleep(long ms, boolean silent) {
		try {
			if (!silent)
				log("Sleeping " + ms + " ms");
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switch to other Web Driver window
	 * 
	 * @param w window handle to switch to
	 * @param r
	 */
	public static void switchTo(String w, Run r) {
		log("Switching to " + w, r);
		r.driver.switchTo().window(w);
	}

	/**
	 * @return 'true' or 'false' with equal probability
	 */
	public static boolean trueOrFalse() {
		return isTrue(.5);
	}

	/**
	 * Simulate typing of text into an input field
	 * 
	 * @param l      string locator of target input element
	 * @param c      string to type in
	 * @param secret hide text in log (useful for password)
	 * @param r
	 * @return whether pasting was used or not
	 */
	public static boolean typeIn(String l, String c, boolean secret, Run r) {
		return typeIn(l, c, 0, secret, r);
	}

	/**
	 * Simulate typing of text into an input field or pasting from clipboard
	 * 
	 * @param l                string locator of target input element
	 * @param c                string to type in
	 * @param pasteProbability probability of instant input (pasting from clipboard
	 *                         simulation)
	 * @param secret           hide text in log (useful for password)
	 * @param r
	 * @return whether pasting was used or not
	 */
	public static boolean typeIn(String l, String c, double pasteProbability, boolean secret, Run r) {
		WebElement e = find(l, r);
		log("Typing '" + ((secret) ? "•".repeat(c.length()) : c) + "' into '" + l, r);
		if (isTrue(pasteProbability)) {
			// simulation of pasting from clipboard, instant addition of all content
			e.sendKeys(c);
			return true;
		} else {
			char[] chars = c.toCharArray();
			for (char ch : chars) {
				sleep(integer(5, 75), true); // silent sleep
				e.sendKeys(String.valueOf(ch));
			}
			return false;
		}
	}

	/**
	 * Simulate typing of text into an input field or pasting from clipboard
	 * 
	 * @param l                string locator of target input element
	 * @param c                string to type in
	 * @param pasteProbability probability of instant input (pasting from clipboard
	 *                         simulation)
	 * @param r
	 * @return whether pasting was used or not
	 */
	public static boolean typeIn(String l, String c, double pasteProbability, Run r) {
		return typeIn(l, c, pasteProbability, false, r);
	}

	/**
	 * Simulate typing of text into an input field or pasting from clipboard
	 * 
	 * @param l                string locator of target input element
	 * @param c                string to type in
	 * @param pasteProbability probability of instant input (pasting from clipboard
	 *                         simulation)
	 * @param r
	 * @return whether pasting was used or not
	 */
	public static boolean typeIn(String l, String c, Run r) {
		return typeIn(l, c, 0, false, r);
	}

	/**
	 * Supply visual representation for non-printable character
	 * 
	 * @param k key to be screened
	 * @return
	 */
	public static String visualize(Keys k) {
		switch (k) {
		case ARROW_DOWN:
			return "↓";
		case ARROW_UP:
			return "↑";
		default:
			return k.toString();
		}
	}

	/**
	 * Wait for appearance of the defined element
	 * 
	 * @param l string locator of an element to be waited for
	 * @param r
	 */
	public static void wait(String l, Run r) {
		waitVisibility(l, r);
	}

	/**
	 * Wait for an element to be clickable
	 * 
	 * @param l string locator of an element expected to be clickable
	 * @param r
	 */
	public static void waitClickable(String l, Run r) {
		log("Waiting for clickability of " + l, r);
		r.wait.until(ExpectedConditions.elementToBeClickable(by(l)));
	}

	/**
	 * Wait for an element to be not visible
	 * 
	 * @param l string locator of an element expected to be invisible
	 * @param r
	 */
	public static void waitInvisibility(String l, Run r) {
		log("Waiting for invisibility of " + l, r);
		r.wait.until(ExpectedConditions.invisibilityOfElementLocated(by(l)));
	}

	/**
	 * Wait for an element to be selected
	 * 
	 * @param l string locator of an element expected to selected
	 * @param r
	 */
	public static void waitSeleted(String l, Run r) {
		log("Waiting for " + l + "to be selected", r);
		r.wait.until(ExpectedConditions.visibilityOfElementLocated(by(l)));
	}

	/**
	 * Wait for a text value of an element to be as defined
	 * 
	 * @param l string locator of an element
	 * @param s expected value
	 * @param r
	 */
	public static void waitValue(String l, String s, Run r) {
		log("Waiting for " + l + " to have value '" + s + "'", r);
		r.wait.until(ExpectedConditions.textToBe(by(l), s));
	}

	/**
	 * Wait for a text value to change from defined one
	 * 
	 * @param l string locator of an element
	 * @param s the value to change from
	 * @param r
	 * @return value after the detected change
	 */
	public static String waitValueNot(String l, String s, Run r) {
		log("Waiting while " + l + " still have value '" + s + "'", r);
		r.wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(by(l), s)));
		return read(l, r);
	}

	/**
	 * Wait for a text value to be not empty
	 * 
	 * @param l string locator of an element
	 * @param r
	 * @return value after the detected change
	 */
	public static String waitValueNotEmpty(String l, Run r) {
		log("Waiting while " + l + " is still empty", r);
		r.wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(by(l), "")));
		return read(l, r);
	}

	/**
	 * Wait for appearance of the defined element
	 * 
	 * @param l string locator of an element to be waited for
	 * @param r
	 */
	public static void waitVisibility(String l, Run r) {
		log("Waiting for visibility of " + l, r);
		r.wait.until(ExpectedConditions.visibilityOfElementLocated(by(l)));
	}

	/**
	 * Write lines of a set to a file
	 * 
	 * @param firstLine first line of the text to be added to a file (title)
	 * @param ss        set of lines to be stored in a file
	 * @param fileName  name of the file to be written
	 */
	public static void writeToFile(String firstLine, Set<String> ss, String fileName) {
		BufferedWriter writer;
		try {
			new File(fileName).getParentFile().mkdirs();
			writer = new BufferedWriter(new FileWriter(fileName));
			if (null != firstLine)
				writer.write(firstLine + System.lineSeparator());
			for (String s : ss) {
				writer.write(s + System.lineSeparator());
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
