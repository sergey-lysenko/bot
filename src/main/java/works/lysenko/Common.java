package works.lysenko;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import works.lysenko.utils.Color;

public class Common {

	/**
	 * Generate {@link org.openqa.selenium.By} object corresponding to given string
	 * locator
	 * 
	 * @param lc string with either xpath or css locator
	 * @return proper locator object based on contents of source string contents
	 */
	public static By by(String lc) {
		if (lc.substring(0, 2).equals("//") || lc.substring(0, 2).equals("./") || lc.substring(0, 3).equals(".//")
				|| lc.substring(0, 4).equals("(.//"))
			return By.xpath(lc);
		else
			return By.cssSelector(lc);
	}

	/**
	 * Generate random integer in the defined range
	 * 
	 * @param l minimum value
	 * @param u maximum value
	 * @return random integer
	 */
	public static int integer(int l, int u) {
		return new RandomDataGenerator().nextInt(l, u);
	}

	public WebDriver d;
	public Logger l;
	public Run r;

	public WebDriverWait w;

	public Common() {
		super();
	}

	public Common(Run r) {
		super();
		this.r = r;
		this.l = r.l;
		this.d = r.d;
		this.w = r.w;
	}

	/**
	 * @param lc string locator of element to be clicked on
	 */
	public void click(String lc) {
		l.log("Clicking " + lc);
		find(lc, true).click(); // Silent find during click
	}

	/**
	 * @param e element to be clicked on
	 */
	public void click(WebElement e) {
		l.log("Clicking " + e.getTagName() + " element");
		e.click();
	}

	/**
	 * Shortcut for {@link java.lang.String#format(t, v)}
	 * 
	 * @param t template
	 * @param v value
	 * @return filled in string template
	 */
	public static String fill(String t, Object... v) {
		return String.format(t, v);
	}

	/**
	 * Find an element by the provided String locator
	 * 
	 * @param lc string locator of target element
	 * @return located element reference
	 */
	public WebElement find(String lc) {
		return find(lc, false);
	}

	/**
	 * Find an element by the provided String locator, with optional omitting of log
	 * message creation
	 * 
	 * @param lc     string locator of target element
	 * @param silent whether to omit log output or not
	 * @return located element reference
	 */
	public WebElement find(String lc, boolean silent) {
		if (!silent)
			l.log("Finding " + lc);
		return d.findElement(by(lc));
	}

	/**
	 * Single-step location of an element by locator of it's parent and it's own
	 * relative locator to this parent element
	 * 
	 * @param pr string absolute locator of parent element
	 * @param ch string relative locator of child element
	 * @return located element reference
	 */
	public WebElement find(String pr, String ch) {
		l.log("Finding parent " + pr + " and child " + ch);
		return d.findElement(by(pr)).findElement(by(ch));
	}

	/**
	 * Find all elements defined by string locator
	 * 
	 * @param lc locator of 1-n elements to be found
	 * @return list of located element references
	 */
	public List<WebElement> findS(String lc) {
		l.log("Finding all " + l + " elements");
		return d.findElements(by(lc));
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
	 * Shortcut for {@link works.lysenko.Logger#l.log(ll, s)}
	 */
	public void log(int ll, String s) {
		l.log(ll, s);
	}

	/**
	 * Shortcut for {@link works.lysenko.Logger#l.log(s)}
	 */
	public void log(String s) {
		l.log(s);
	}

	private void makeCodeshot(String p, String f) {
		try {
			Files.writeString(Path.of(p, f + ".html"), d.getPageSource());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Produce a full-screen PNG Screenshot and save it in a location defined by
	 * Constants.DEFAULT_SHOTS_LOCATION
	 * 
	 * @param n of the screenshot
	 */
	public void makeScreenshot(String n) {
		makeScreenshot(null, Constants.DEFAULT_SHOTS_LOCATION, n);
	}

	/**
	 * Produce a framed PNG Screenshot of defined element and save it in a location
	 * defined by Constants.DEFAULT_SHOTS_LOCATION
	 * 
	 * @param element to be used for local framed screenshot
	 * @param name    of the screenshot
	 * @param r
	 */
	public void makeScreenshot(WebElement element, String name) {
		makeScreenshot(element, Constants.DEFAULT_SHOTS_LOCATION, name);
	}

	private void makeScreenshot(WebElement element, String path, String name) {
		File src = null;
		if (element == null)
			src = ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
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
	public void makeSnapshot(String f) {
		makeScreenshot(null, Constants.DEFAULT_SHOTS_LOCATION, f);
		makeCodeshot(Constants.DEFAULT_SHOTS_LOCATION, f);
	}

	/**
	 * Open a page defined by protocol and domain
	 * 
	 * @param p protocol
	 * @param u domain
	 */
	public void open(String p, String u) {
		String a = p + "://" + u;
		l.log("Opening " + a);
		d.get(a);
	}

	/**
	 * Open a page defined by domain using https:// protocol
	 * 
	 * @param u domain
	 * @param r
	 */
	public void openDomain(String u) {
		l.log("Opening https://" + u);
		d.get("https://" + u);
	}

	/**
	 * Read contents of the element defined by string locator
	 * 
	 * @param lc string locator of an element
	 * @return result of .getText() for this element
	 */
	public String read(String lc) {
		l.log("Reading from " + lc);
		return d.findElement(by(lc)).getText();
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
	 * @param lc string locator of requested element
	 * @return BASE64 encoded string screenshot
	 */
	public String screenShot(String lc) {
		return ((TakesScreenshot) d.findElement(by(lc))).getScreenshotAs(OutputType.BASE64);
	}

	/**
	 * Add a section title to the test log
	 * 
	 * @param s string to be added as section title
	 */
	public void section(String s) {
		l.log(0, Color.colorize("= " + s + " =", Color.BLUE_BOLD_BRIGHT));
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
	 * Send keys to the element defined by string locator
	 * 
	 * @param lc string locator of target input element
	 * @param s  CharSequence to send
	 */
	public void sendKeys(String lc, CharSequence s) {
		find(lc).sendKeys(s);
	}

	/**
	 * Sleep during defined amount of milliseconds with record in the test log
	 * 
	 * @param ms amount of milliseconds to pause for
	 */
	public void sleep(long ms) {
		if (ms < Constants.SILENT_SLEEPING_TRESHHOLD)
			l.log(0, Color.colorize("[WARNING]: " + "Sleeping for less then " + Constants.SILENT_SLEEPING_TRESHHOLD
					+ " ms is better to be perfomed in silent mode"));
		sleep(ms, false);
	}

	/**
	 * Sleep during defined amount of milliseconds with optional bypassing of
	 * logging
	 * 
	 * @param ms     amount of milliseconds to pause for
	 * @param silent whether to bypass output to test log. Useful for short delays
	 */
	public void sleep(long ms, boolean silent) {
		try {
			if (!silent)
				l.log("Sleeping " + ms + " ms");
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switch to other WebDriver window
	 * 
	 * @param w window handle to switch to
	 */
	public void switchTo(String w) {
		l.log("Switching to " + w);
		d.switchTo().window(w);
	}

	/**
	 * Generate equally probable true or false value
	 * 
	 * @return 'true' or 'false' with equal probability
	 */
	public static boolean trueOrFalse() {
		return isTrue(.5);
	}

	/**
	 * Simulate typing of a text into the input field defined by string locator
	 * 
	 * @param lc string locator of target input element
	 * @param s  string to type in
	 * @return whether pasting was used or not
	 */
	public boolean typeIn(String lc, String s) {
		return typeIn(lc, s, 0, false);
	}

	/**
	 * Simulate typing of a text into the input field defined by string locator,
	 * with optional screening of typed characters in logs (useful for passwords or
	 * other sensitive data)
	 * 
	 * @param lc     string locator of target input element
	 * @param s      string to type in
	 * @param secret hide text in log (useful for password)
	 * @return whether pasting was used or not
	 */
	public boolean typeIn(String lc, String s, boolean secret) {
		return typeIn(lc, s, 0, secret);
	}

	/**
	 * Simulate typing of a text into the input field defined by string locator
	 * 
	 * @param lc string locator of target input element
	 * @param s  string to type in
	 * @param pp probability of instant input (pasting from clipboard simulation)
	 * @return whether pasting was used or not
	 */
	public boolean typeIn(String lc, String s, double pp) {
		return typeIn(lc, s, pp, false);
	}

	/**
	 * Simulate typing of a text or pasting if from clipboard into the input field
	 * defined by string locator, with optional screening of typed characters in
	 * logs (useful for passwords or other sensitive data)
	 * 
	 * @param lc     string locator of target input element
	 * @param s      string to type in
	 * @param pp     probability of instant input (pasting from clipboard
	 *               simulation)
	 * @param secret hide text in log
	 * @return whether pasting was used or not
	 */
	public boolean typeIn(String lc, String c, double pp, boolean secret) {
		WebElement e = find(lc);
		l.log("Typing '" + ((secret) ? "•".repeat(c.length()) : c) + "' into '" + lc);
		if (isTrue(pp)) {
			// simulation of pasting from clipboard by instant addition of all content
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
	 * @param lc string locator of an element to be waited for
	 */
	public void wait(String lc) {
		waitVisibility(lc);
	}

	/**
	 * Wait for appearance of the defined element and the click on it
	 * 
	 * @param lc string locator of an element to be waited for
	 */
	public void waitClick(String lc) {
		wait(lc);
		click(lc);
	}

	/**
	 * Wait for an element to be clickable
	 * 
	 * @param lc string locator of an element expected to be clickable
	 */
	public void waitClickable(String lc) {
		l.log("Waiting for clickability of " + lc);
		w.until(ExpectedConditions.elementToBeClickable(by(lc)));
	}

	/**
	 * Wait for an element to be not visible
	 * 
	 * @param lc string locator of an element expected to be invisible
	 */
	public void waitInvisibility(String lc) {
		l.log("Waiting for invisibility of " + lc);
		w.until(ExpectedConditions.invisibilityOfElementLocated(by(lc)));
	}

	/**
	 * Wait for an element to be selected
	 * 
	 * @param lc string locator of an element expected to be selected
	 */
	public void waitSeleted(String lc) {
		l.log("Waiting for " + lc + "to be selected");
		w.until(ExpectedConditions.visibilityOfElementLocated(by(lc)));
	}

	/**
	 * Wait for a text value of an element to be as defined
	 * 
	 * @param lc string locator of an element
	 * @param s  expected value
	 */
	public void waitValue(String lc, String s) {
		l.log("Waiting for " + lc + " to have value '" + s + "'");
		w.until(ExpectedConditions.textToBe(by(lc), s));
	}

	/**
	 * Wait for a text value of an element to change from defined one
	 * 
	 * @param lc string locator of an element
	 * @param s  the value to change from
	 * @return value after the detected change
	 */
	public String waitValueNot(String lc, String s) {
		l.log("Waiting while " + l + " still have value '" + s + "'");
		w.until(ExpectedConditions.not(ExpectedConditions.textToBe(by(lc), s)));
		return read(lc);
	}

	/**
	 * Wait for a text value to be not empty
	 * 
	 * @param lc string locator of an element
	 * @return value after the detected change
	 */
	public String waitValueNotEmpty(String lc) {
		l.log("Waiting while " + l + " is still empty");
		w.until(ExpectedConditions.not(ExpectedConditions.textToBe(by(lc), "")));
		return read(lc);
	}

	/**
	 * Wait for appearance of the defined element
	 * 
	 * @param lc string locator of an element to be waited for
	 */
	public void waitVisibility(String lc) {
		l.log("Waiting for visibility of " + lc);
		w.until(ExpectedConditions.visibilityOfElementLocated(by(lc)));
	}

	/**
	 * Write lines of the provided a set to a file
	 * 
	 * @param fl optional first line of the text to be added to a file (title)
	 * @param ss set of lines to be stored in a file
	 * @param fn name of the file to be written
	 */
	public static void writeToFile(String fl, Set<String> ss, String fn) {
		BufferedWriter writer;
		try {
			new File(fn).getParentFile().mkdirs(); // Create parent directory
			writer = new BufferedWriter(new FileWriter(fn));
			if (null != fl)
				writer.write(fl + System.lineSeparator());
			for (String s : ss) {
				writer.write(s + System.lineSeparator());
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
