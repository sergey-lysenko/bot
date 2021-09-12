package works.lysenko;

import static works.lysenko.Constants.DEFAULT_SHOTS_LOCATION;
import static works.lysenko.Constants.SILENT_SLEEPING_TRESHHOLD;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import works.lysenko.utils.Ansi;

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
	 * Generate {@link org.openqa.selenium.By} object corresponding to given string
	 * locator
	 * 
	 * @param lc array of strings with either xpath or css locator
	 * @return proper locator object based on contents of source string contents
	 */
	public static By by(String[] lc) {
		return by((String) selectOne(lc));
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
	 * Generate random integer in the defined range
	 * 
	 * @param l minimum value
	 * @param u maximum value
	 * @return random integer
	 */
	public static Integer integer(int l, int u) {
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
	 * Get an element from a list of WebElement objects.
	 * 
	 * @param l list of WebElement objects to select from
	 * @return a WebElement object from a list
	 */
	public static WebElement select3One(List<WebElement> l) {
		return l.get(new Random().nextInt(l.size()));
	}

	/**
	 * @param <T>
	 * @param l
	 * @return
	 */
	public static <T> T selectOne(List<T> l) {
		return l.get(new Random().nextInt(l.size()));
	}

	public static Object selectOne(Object[] s) {
		return s[new Random().nextInt(Array.getLength(s))];
	}

	public static <T> Object selectOne(Set<T> s) {
		// TODO: optimize
		return s.toArray()[new Random().nextInt(s.size())];
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
	public static String selectOther(String e, List<String> l) {
		List<String> copy = new ArrayList<>(l);
		copy.remove(e);
		return selectOne(copy);
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

	public Output o;

	public Logger l;

	public Results r;

	public Execution x;

	public WebDriver d;

	public WebDriverWait w;

	public Common() {
		super();
	}

	public Common(Execution x) {
		super();
		this.x = x;
		this.d = x.d;
		this.l = x.l;
		this.o = x.o;
		this.r = x.r;
		this.w = x.w;
	}

	public void clear(String... lc) {
		l.log("Clearing " + describe(lc));
		find(true, lc).clear();
		; // Silent find during clear
	}

	/**
	 * @param lc string locator of element to be clicked on
	 */
	public void click(String... lc) {
		l.log("Clicking " + describe(lc));
		find(true, lc).click(); // Silent find during click
	}

	public void click(WebElement e) {
		click(e, true);
	}

	/**
	 * @param e element to be clicked on
	 */
	public void click(WebElement e, boolean geometry) {
		l.log("Clicking " + describe(e, true));
		e.click();
	}

	private String describe(Rectangle r) {
		return "h" + r.height + " w" + r.width + " @ " + "x" + r.x + " y" + r.y;
	}

	private String describe(String[] lc) {
		if (lc.length == 0)
			return "";
		if (lc.length == 1)
			return lc[0];
		if (lc.length > 1)
			return Arrays.toString(lc);
		return null;
	}

	private String describe(WebElement e, boolean geometry) {
		String an = e.getAccessibleName();
		String tg = e.getTagName();
		return tg + ((an.isEmpty()) ? "" : "'" + an + "'") + ((geometry) ? (" @ " + describe(e.getRect())) : "");
	}

	public WebElement find(boolean silent, String... lc) {
		WebElement e = null;
		if (!silent && lc.length > 0)
			l.log("Finding " + lc[0]);
		if (lc.length == 0)
			e = d.findElement(by("//body"));
		if (lc.length > 0)
			e = d.findElement(by(lc[0]));
		if (lc.length > 1)
			for (int i = 1; i < lc.length; i++) {
				if (!silent)
					l.log(" ... and child " + lc[i]);
				e = e.findElement(by(lc[i]));
			}
		return find(e);
	}

	public WebElement find(String... lc) {
		return find(false, lc);
	}

	public WebElement find(WebElement e) {
		Actions actions = new Actions(d);
		actions.moveToElement(e);
		actions.perform();
		return e;
	}

	/**
	 * Find all elements defined by string locator
	 * 
	 * @param lc locator of 1-n elements to be found
	 * @return list of located element references
	 */
	public List<WebElement> findS(String lc) {
		l.log("Finding all " + lc + " elements");
		return d.findElements(by(lc));
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
	 * DEFAULT_SHOTS_LOCATION
	 * 
	 * @param n of the screenshot
	 */
	public void makeScreenshot(String n) {
		makeScreenshot(null, DEFAULT_SHOTS_LOCATION, n);
	}

	/**
	 * Produce a framed PNG Screenshot of defined element and save it in a location
	 * defined by DEFAULT_SHOTS_LOCATION
	 * 
	 * @param element to be used for local framed screenshot
	 * @param name    of the screenshot
	 * @param x
	 */
	public void makeScreenshot(WebElement element, String name) {
		makeScreenshot(element, DEFAULT_SHOTS_LOCATION, name);
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
	 * save in a location defined by DEFAULT_SHOTS_LOCATION
	 * 
	 * @param f name of snapshot
	 * @param x
	 */
	public void makeSnapshot(String f) {
		makeScreenshot(null, DEFAULT_SHOTS_LOCATION, f);
		makeCodeshot(DEFAULT_SHOTS_LOCATION, f);
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
	 * @param x
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

	public String read(WebElement e) {
		return read(e, true);
	}

	/**
	 * Read contents of the element defined by string locator
	 * 
	 * @param lc string locator of an element
	 * @return result of .getText() for this element
	 */
	public String read(WebElement e, boolean geometry) {
		l.log("Reading from " + describe(e, geometry));
		return e.getText();
	}

	/**
	 * Read contents of the element defined by string locator
	 * 
	 * @param lc string locator of an element
	 * @return result of .getText() for this element
	 */
	public String readInput(String lc) {
		l.log("Reading from " + lc);
		return d.findElement(by(lc)).getAttribute("value");
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
		l.log(0, Ansi.colorize("= " + s + " =", Ansi.BLUE_BOLD_BRIGHT));
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

	public void sleep(long ms) {
		sleep(ms, null);
	}

	public void sleep(long ms, boolean silent) {
		sleep(ms, null, silent);
	}

	/**
	 * Sleep during defined amount of milliseconds with record in the test log
	 * 
	 * @param ms amount of milliseconds to pause for
	 */
	public void sleep(long ms, String s) {
		if (ms < SILENT_SLEEPING_TRESHHOLD)
			l.log(0, Ansi.colorize("[WARNING]: " + "Sleeping for less then " + SILENT_SLEEPING_TRESHHOLD
					+ " ms is better to be perfomed in silent mode"));
		sleep(ms, s, false);
	}

	/**
	 * Sleep during defined amount of milliseconds with optional bypassing of
	 * logging
	 * 
	 * @param ms     amount of milliseconds to pause for
	 * @param silent whether to bypass output to test log. Useful for short delays
	 */
	public void sleep(long ms, String s, boolean silent) {
		try {
			if (!silent)
				l.log((null == s) ? "Sleeping " + ms + " ms" : s);
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
	 * Call {@link Common#typeInto(String, Object, double, boolean)} with
	 * {@code pasteProbability} set to {@value 0} and {@code secret} set to
	 * {@value false}
	 * 
	 * @param lc single String locator. To facilitate multilevel search, use
	 *           {@code typeInto(find(str1, str2, ...), ...);}
	 * @param c  Content to type, Object.toString() in used to get string
	 *           representation
	 * @return true of direct copy was used, false otherwise
	 */
	public boolean typeInto(String lc, Object c) {
		return typeInto(lc, c, 0, false);
	}

	/**
	 * Call {@link Common#typeInto(String, Object, double, boolean)} with
	 * {@code pasteProbability} set to {@value 0}
	 * 
	 * @param lc     single String locator. To facilitate multilevel search, use
	 *               {@code typeInto(find(str1, str2, ...), ...);}
	 * @param c      Content to type, Object.toString() in used to get string
	 *               representation
	 * @param secret whether to screen content in the log by '•' symbols (as in
	 *               passwords)
	 * @return true of direct copy was used, false otherwise
	 */
	public boolean typeInto(String lc, Object c, boolean secret) {
		return typeInto(lc, c, 0, secret);
	}

	/**
	 * Call {@link Common#typeInto(String, Object, double, boolean)} with
	 * {@code secret} set to {@value false}
	 * 
	 * @param lc single String locator. To facilitate multilevel search, use
	 *           {@code typeInto(find(str1, str2, ...), ...);}
	 * @param c  Content to type, Object.toString() in used to get string
	 *           representation
	 * @param pp Probability in range [0.0 - 1.0] of using direct copy to simulate
	 *           pasting from clipboard;
	 * @return true of direct copy was used, false otherwise
	 */
	public boolean typeInto(String lc, Object c, double pp) {
		return typeInto(lc, c, pp, false);
	}

	/**
	 * Call {@link Common#typeInto(WebElement, Object, double, boolean)} with
	 * WebElement defined by String locator
	 * 
	 * @param lc     single String locator. To facilitate multilevel search, use
	 *               {@code typeInto(find(str1, str2, ...), ...);}
	 * @param c      Content to type, Object.toString() in used to get string
	 *               representation
	 * @param pp     Probability in range [0.0 - 1.0] of using direct copy to
	 *               simulate pasting from clipboard;
	 * @param secret whether to screen content in the log by '•' symbols (as in
	 *               passwords)
	 * @return true of direct copy was used, false otherwise
	 */
	public boolean typeInto(String lc, Object c, double pp, boolean secret) {
		WebElement e = find(lc);
		return typeInto(e, c, pp, secret);
	}

	/**
	 * Call {@link Common#typeInto(WebElement, Object, double, boolean)} with
	 * {@code pasteProbability} set to {@value 0} and {@code secret} set to
	 * {@value false}
	 * 
	 * @param e WebElement to type into
	 * @param c Content to type, Object.toString() in used to get string
	 *          representation
	 * @return true of direct copy was used, false otherwise
	 */
	public boolean typeInto(WebElement e, Object c) {
		return typeInto(e, c, 0, false);
	}

	/**
	 * Call {@link Common#typeInto(WebElement, Object, double, boolean)} with
	 * {@code pasteProbability} set to {@value 0}
	 * 
	 * @param e      WebElement to type into
	 * @param c      Content to type, Object.toString() in used to get string
	 *               representation
	 * @param secret whether to screen content in the log by '•' symbols (as in
	 *               passwords)
	 * @return true of direct copy was used, false otherwise
	 */
	public boolean typeInto(WebElement e, Object c, boolean secret) {
		return typeInto(e, c, 0, secret);
	}

	/**
	 * Call {@link Common#typeInto(WebElement, Object, double, boolean)} with
	 * {@code secret} set to {@value false}
	 * 
	 * @param e  WebElement to type into
	 * @param c  Content to type, Object.toString() in used to get string
	 *           representation
	 * @param pp Probability in range [0.0 - 1.0] of using direct copy to simulate
	 *           pasting from clipboard;
	 * @return true of direct copy was used, false otherwise
	 */
	public boolean typeInto(WebElement e, Object c, double pp) {
		return typeInto(e, c, pp, false);
	}

	/**
	 * Simulate typing the value into the WebElement
	 * 
	 * @param e      WebElement to type into
	 * @param c      Content to type, Object.toString() in used to get string
	 *               representation
	 * @param pp     Probability in range [0.0 - 1.0] of using direct copy to
	 *               simulate pasting from clipboard;
	 * @param secret whether to screen content in the log by '•' symbols (as in
	 *               passwords)
	 * @return true of direct copy was used, false otherwise
	 */
	public boolean typeInto(WebElement e, Object c, double pp, boolean secret) {
		CharSequence symbols = String.valueOf(c);
		l.log("Typing '" + ((secret) ? "•".repeat(symbols.length()) : c) + "' into " + describe(e, true));
		if (isTrue(pp)) {
			// simulation of pasting from clipboard by instant addition of all content
			e.sendKeys(symbols);
			return true;
		} else {
			char[] chars = symbols.toString().toCharArray();
			for (char ch : chars) {
				sleep(integer(0, 50), true); // silent sleep
				e.sendKeys(String.valueOf(ch));
			}
			return false;
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
	 * Wait for appearance of the defined element
	 * 
	 * @param lc string locator of an element to be waited for
	 */
	public void wait(String[] lc) {
		waitVisibility(lc);
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
	 * Wait for appearance of the defined element and the click on it
	 * 
	 * @param lc string locator of an element to be waited for
	 */
	public void waitThenClick(String lc) {
		wait(lc);
		click(lc);
	}

	/**
	 * Wait for appearance of the defined element and the click on it
	 * 
	 * @param lc string locator of an element to be waited for
	 */
	public void waitThenClick(String[] lc) {
		wait(lc);
		click(lc);
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
		l.log("Waiting while " + lc + " still have value '" + s + "'");
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
		l.log("Waiting while " + lc + " is still empty");
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
	 * Wait for appearance of the defined element
	 * 
	 * @param lc string locator of an element to be waited for
	 */
	public void waitVisibility(String[] lc) {
		l.log("Waiting for visibility of one of " + Arrays.toString(lc));
		w.until(ExpectedConditions.visibilityOfElementLocated(by(lc)));
	}

}
