package works.lysenko;

import static works.lysenko.Constants.ALL_;
import static works.lysenko.Constants.BODY;
import static works.lysenko.Constants.CAUGHT_;
import static works.lysenko.Constants.CHECKING_;
import static works.lysenko.Constants.CLEARING_;
import static works.lysenko.Constants.CLICKABILITY_OF_;
import static works.lysenko.Constants.CLICKING_;
import static works.lysenko.Constants.EMPTY;
import static works.lysenko.Constants.EXCEPTION_RETRIES;
import static works.lysenko.Constants.E_IS_NULL;
import static works.lysenko.Constants.FINDING_;
import static works.lysenko.Constants.FOR_;
import static works.lysenko.Constants.FROM_;
import static works.lysenko.Constants.HTML;
import static works.lysenko.Constants.HTTP;
import static works.lysenko.Constants.INVISIBILITY_OF_;
import static works.lysenko.Constants.OPENING_;
import static works.lysenko.Constants.PNG;
import static works.lysenko.Constants.READING_;
import static works.lysenko.Constants.RESOURCES;
import static works.lysenko.Constants.SCREENSHOTS;
import static works.lysenko.Constants.SILENT_SLEEPING_TRESHOLD;
import static works.lysenko.Constants.SLEEPING_;
import static works.lysenko.Constants.SWITCHING_;
import static works.lysenko.Constants.THE_TEXT_OF_;
import static works.lysenko.Constants.TO_;
import static works.lysenko.Constants.TO_BE_SELECTED;
import static works.lysenko.Constants.TYPING_;
import static works.lysenko.Constants.UPLOADING_;
import static works.lysenko.Constants.USER_DIR;
import static works.lysenko.Constants.VALUE;
import static works.lysenko.Constants.VISIBILITY_OF_;
import static works.lysenko.Constants.WAITING_;
import static works.lysenko.Constants.WHETHER_;
import static works.lysenko.Constants.WHILE_;
import static works.lysenko.Constants.XML;
import static works.lysenko.Constants._AFTER_CLEARING_;
import static works.lysenko.Constants._AND_CHILD_;
import static works.lysenko.Constants._BEFORE_CLEARING_;
import static works.lysenko.Constants._DISABLED;
import static works.lysenko.Constants._ELEMENTS;
import static works.lysenko.Constants._EMPTY;
import static works.lysenko.Constants._ENABLED;
import static works.lysenko.Constants._HAVE_VALUE_;
import static works.lysenko.Constants._INTO_;
import static works.lysenko.Constants._IS;
import static works.lysenko.Constants._MS;
import static works.lysenko.Constants._NOW_;
import static works.lysenko.Constants._S;
import static works.lysenko.Constants._STILL;
import static works.lysenko.Constants._TEXT_IN_;
import static works.lysenko.Constants._THROUGH_;
import static works.lysenko.Constants._TO;
import static works.lysenko.Constants.u0020;
import static works.lysenko.Constants.u0027;
import static works.lysenko.Constants.u0028;
import static works.lysenko.Constants.u002E;
import static works.lysenko.Constants.u002F;
import static works.lysenko.Constants.u003A;
import static works.lysenko.Constants.u003D;
import static works.lysenko.Constants.u003E;
import static works.lysenko.Constants.u0040;
import static works.lysenko.Constants.u0061;
import static works.lysenko.Constants.u0073;
import static works.lysenko.Constants.u2022;
import static works.lysenko.Constants.u2191;
import static works.lysenko.Constants.u2193;
import static works.lysenko.enums.Platform.ANDROID;
import static works.lysenko.enums.Platform.CHROME;
import static works.lysenko.enums.Platform.FIREFOX;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import works.lysenko.enums.Ansi;
import works.lysenko.enums.Platform;
import works.lysenko.enums.Severity;

/**
 * @author Sergii Lysenko
 */
public class Common {

	/**
	 * Generate {@link org.openqa.selenium.By} object corresponding to given string
	 * locator
	 *
	 * @param lc string with either xpath or css locator
	 * @return proper locator object based on contents of source string contents
	 */
	public static By by(String lc) {
		if (lc.substring(0, 2).equals(u002F + u002F) //
				|| lc.substring(0, 2).equals(u002E + u002F) //
				|| lc.substring(0, 3).equals(u002E + u002F + u002F) //
				|| lc.substring(0, 4).equals(u0028 + u002E + u002F + u002F)) //
			return By.xpath(lc);
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
	public static int integer(int l, int u) {
		return new RandomDataGenerator().nextInt(l, u);
	}

	/**
	 * Generate random integer in the defined range with list of values to exclude
	 *
	 * @param l minimum value
	 * @param u maximum value
	 * @param x list of values to exclude
	 * @return random integer
	 */
	@SuppressWarnings("boxing")
	public static int integer(int l, int u, Integer... x) {
		Integer n; // Non-primitive for asList() compatibility
		do
			n = new RandomDataGenerator().nextInt(l, u);
		while (Arrays.asList(x).contains(n));
		return n;
	}

	/**
	 * Return 'true' of 'false' with equal probability
	 *
	 * @return random boolean value
	 */
	public static boolean isTrue() {
		return isTrue(0.5);
	}

	/**
	 * Return 'true' with defined probability, or 'false' otherwise
	 *
	 * @param d probability of 'true' selection
	 * @return random boolean value
	 */
	public static boolean isTrue(double d) {
		if (d > 1.0 || d < 0.0)
			throw new IllegalArgumentException("Given probability " + d + " is outside the valid range of [0.0 - 1.0]"); //$NON-NLS-1$ //$NON-NLS-2$
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
		String sequence = EMPTY;
		for (int i = 0; i < t; i++)
			sequence = sequence + k;
		return sequence;
	}

	/**
	 * Get random object from a list of objects
	 *
	 * @param <T> type of objects in a list
	 * @param l   list of objects
	 * @return random object from a list of objects
	 */
	public static <T> T selectOne(List<T> l) {
		return l.get(new Random().nextInt(l.size()));
	}

	/**
	 * Get random object from an array of objects
	 *
	 * @param a array of objects
	 * @return random element from an array of objects
	 */
	public static Object selectOne(Object[] a) {
		return a[new Random().nextInt(Array.getLength(a))];
	}

	/**
	 * Get random object from a set of objects
	 *
	 * @param <T> type of objects in a set
	 * @param s   set of objects
	 * @return random object from a set of objects
	 */
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
	 * @param t milliseconds
	 * @return String human readable representation of given milliseconds time
	 *         interval
	 */
	public static String timeH(long t) {
		String s = String.valueOf(t);
		if (s.length() > 3)
			return String.valueOf(ArrayUtils.insert(s.length() - 3, String.valueOf(t).toCharArray(), '.')) + _S;
		return s + _MS;
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
	 * @return visual representation for non-printable character
	 */
	public static String visualize(Keys k) {
		switch (k) {
		case ARROW_DOWN:
			return u2193;
		case ARROW_UP:
			return u2191;
		default:
			return k.toString();
		}
	}

	/**
	 * Write lines of the provided set to a file
	 *
	 * @param fl optional first line of the text to be added to a file (title)
	 * @param ss set of lines to be stored in a file
	 * @param fn name of the file to be written
	 */
	@SuppressWarnings("resource")
	public static void writeToFile(String fl, Set<String> ss, String fn) {
		BufferedWriter writer;
		try {
			new File(fn).getParentFile().mkdirs(); // Create parent directory
			writer = new BufferedWriter(new FileWriter(fn));
			if (null != fl)
				writer.write(fl + System.lineSeparator());
			for (String s : ss)
				writer.write(s + System.lineSeparator());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	public Output o;

	/**
	 *
	 */
	public Logger l;

	/**
	 *
	 */
	public Results r;

	/**
	 *
	 */
	public Execution x;

	/**
	 *
	 */
	public WebDriver d;

	/**
	 *
	 */
	public WebDriverWait w;

	/**
	 *
	 */
	AppiumDriverLocalService service = null;

	/**
	 *
	 */
	public Common() {
		super();
	}

	/**
	 * @param x instance of Execution object
	 */
	public Common(Execution x) {
		super();
		this.x = x;
		populateShortcuts();
	}

	/**
	 * Clear an input by either executing
	 * {@link org.openqa.selenium.WebElement#clear()} method or by simulating the
	 * <strong>Ctrl+A</strong> and <strong>Backspace</strong> keystrokes
	 *
	 * @param sendKeys <strong>Ctrl+A*</strong> and <strong>Backspace</strong> if
	 *                 true
	 * @param lc       locator(s) of elements to clear
	 */
	public void clear(boolean sendKeys, String... lc) {
		WebElement e = find(false, true, lc);
		clear(sendKeys, e);
	}

	/**
	 * Clear an input by either executing
	 * {@link org.openqa.selenium.WebElement#clear()} method or by simulating the
	 * <strong>Ctrl+A</strong> and <strong>Backspace</strong> keystrokes
	 *
	 * @param sendKeys <strong>Ctrl+A*</strong> and <strong>Backspace</strong> if
	 *                 true
	 * @param e        element to clear
	 */
	public void clear(boolean sendKeys, WebElement e) {
		this.l.log(CLEARING_ + describe(e));
		if (this.x._debug())
			this.l.log(_TEXT_IN_ + describe(e) + _BEFORE_CLEARING_ + quote(e.getAttribute(VALUE)));
		if (sendKeys) {
			e.sendKeys(Keys.CONTROL, u0061);
			e.sendKeys(Keys.BACK_SPACE);
		} else
			e.clear();
		if (this.x._debug())
			this.l.log(_TEXT_IN_ + describe(e) + _AFTER_CLEARING_ + quote(e.getAttribute(VALUE)));
	}

	/**
	 * Clear an input by executing {@link org.openqa.selenium.WebElement#clear()}
	 * method
	 *
	 * @param sendKeys <strong>Ctrl+A*</strong> and <strong>Backspace</strong> if
	 *                 true
	 * @param lc       locator(s) of elements to clear
	 */
	public void clear(String... lc) {
		WebElement e = find(false, true, lc);
		clear(e);
	}

	/**
	 * Clear an input by executing {@link org.openqa.selenium.WebElement#clear()}
	 * method
	 *
	 * @param sendKeys <strong>Ctrl+A*</strong> and <strong>Backspace</strong> if
	 *                 true
	 * @param e        element to clear
	 */
	public void clear(WebElement e) {
		clear(false, e);
	}

	/**
	 * Perform a non-centered click on an element. Exact location of click defined
	 * by two double typed relative x and y coordinates, where value 0.0 corresponds
	 * to leftmost or upmost part of the element, as 1.0 is for rightmost of lowest
	 * part of an object.
	 *
	 * Consequently, coordinates (0.5, 0.5) defining a center of the object
	 *
	 * @param x  requested x coordinate offset
	 * @param y
	 * @param lc string locator(s) of element to be clicked on
	 */
	@SuppressWarnings("nls")
	public void click(@SuppressWarnings("hiding") double x, double y, String... lc) {
		this.l.log("Clicking " + describe(lc) + " at (x" + x + ",y" + y + ")");
		this.l.log(2, "Locating the element " + describe(lc) + " ...");
		WebElement e = find(true, true, lc);
		Rectangle re = e.getRect();
		this.l.log(2, "Element's rectangle is " + describe(re) + " ...");
		Point c = getCenter(re);
		Point p = getPoint(x, y, re);
		int offsetX = p.x - c.x;
		int offsetY = p.y - c.y;
		this.l.log(2, "Calculated offset in pixels from center is  (x" + offsetX + ",y" + offsetY + ") ...");
		Actions actions = new Actions(this.d);
		actions.moveToElement(e);
		actions.moveByOffset(offsetX, offsetY);
		actions.click();
		actions.perform();
	}

	/**
	 * @param lc string locator of element to be clicked on
	 */
	public void click(String... lc) {
		if (lc.length > 0) {
			boolean done = false;
			int attempt = 0;
			do
				try {
					WebElement e = find(false, true, lc);
					this.l.log(CLICKING_ + describe(lc));
					if (e != null) {
						e.click();
						done = true;
					} else {
						this.l.logProblem(Severity.S2, E_IS_NULL);
						return;
					}
				} catch (StaleElementReferenceException ex) {
					this.l.logProblem(Severity.S2, CAUGHT_ + ex.getClass().getName()
							+ ", while trying to click(), during attempt " + ++attempt + " ..."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			while (!done && attempt <= EXCEPTION_RETRIES);
		} else
			this.l.logProblem(Severity.S3, "Empty locators list in click()"); //$NON-NLS-1$
	}

	/**
	 * @param e WebElement to click on
	 */
	public void click(WebElement e) {
		click(e, true);
	}

	/**
	 * @param e        element to be clicked on
	 * @param geometry
	 */
	public void click(WebElement e, boolean geometry) {
		this.l.log(CLICKING_ + describe(e, true));
		e.click();
	}

	private static String describe(Rectangle r) {
		return "h" + r.height + " w" + r.width + " @ " + "x" + r.x + " y" + r.y; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$
	}

	private static String describe(String[] lc) {
		if (lc.length == 0)
			return EMPTY;
		if (lc.length == 1)
			return lc[0];
		if (lc.length > 1)
			return Arrays.toString(lc);
		return null;
	}

	private String describe(WebElement e) {
		return describe(e, true);
	}

	private String describe(WebElement e, boolean geometry) {
		String an = this.x.in(CHROME) ? e.getAccessibleName() : EMPTY;
		String tg = e.getTagName();
		return tg + (an.isEmpty() ? EMPTY : u0020 + quote(an)) + (geometry ? u0040 + describe(e.getRect()) : EMPTY);
	}

	/**
	 * @param lc locator of element
	 * @return true if element exists
	 */
	public boolean exists(String lc) {
		int oc = findAll(lc).size();
		return oc > 0;
	}

	/**
	 * Find an element defined by one or several nested String locators
	 *
	 * @param silent   if true, no output will be added to log
	 * @param scrollTo if true, window will be scrolled to make this element visible
	 *                 on screen
	 * @param lc       one or several nested String locators
	 * @return WebElement object
	 * @throws Exception
	 */
	public WebElement find(boolean silent, boolean scrollTo, String... lc) {

		WebElement e = null;
		boolean done = false;
		int attempt = 0;
		do
			try {
				if (!silent && lc.length > 0)
					this.l.log(FINDING_ + lc[0]);
				if (lc.length == 0)
					e = this.d.findElement(by(BODY));
				if (lc.length > 0)
					e = this.d.findElement(by(lc[0]));
				if (lc.length > 1)
					for (int i = 1; i < lc.length; i++) {
						if (!silent)
							this.l.log(_AND_CHILD_ + lc[i]);
						if (null != e)
							e = e.findElement(by(lc[i]));
					}
				done = true;
			} catch (TimeoutException | NoSuchElementException | StaleElementReferenceException ex) {
				this.l.logProblem(Severity.S2, CAUGHT_ + ex.getClass().getName()
						+ ", while trying to find(), during attempt " + ++attempt + " ..."); //$NON-NLS-1$//$NON-NLS-2$
				sleep(333);
			}
		while (!done && attempt < EXCEPTION_RETRIES);
		if (attempt >= EXCEPTION_RETRIES) {
			logProblem(Severity.S1, "Maximum retries amount reached, find() returns null, test failure imminent"); //$NON-NLS-1$
			return null;
		}
		return scrollTo ? find(e) : e;
	}

	/**
	 * Find an element defined by one or several nested String locators
	 *
	 * @param lc one or several nested String locators
	 * @return WebElement object
	 */
	public WebElement find(String... lc) {
		return find(false, true, lc);
	}

	/**
	 * "Find" an element which could be located outside of current view by
	 * performing moveToElement() call
	 *
	 * @param e WebElement to scroll to
	 * @return same web element
	 */
	public WebElement find(WebElement e) {

		if (in(FIREFOX))
			((JavascriptExecutor) this.x.d).executeScript("arguments[0].scrollIntoView(true);", e); //$NON-NLS-1$
		else {
			Actions actions = new Actions(this.d);
			actions.moveToElement(e);
			actions.perform();
		}
		return e;
	}

	/**
	 * @param e
	 * @param c
	 * @return child element of e defined by c
	 */
	public static WebElement find(WebElement e, String c) {
		return e.findElement(by(c));
	}

	/**
	 * Find all elements defined by string locator
	 *
	 * @param lc locator of 1-n elements to be found
	 * @return list of located element references
	 */
	public List<WebElement> findAll(String lc) {
		List<WebElement> e = this.d.findElements(by(lc));
		this.l.log(FINDING_ + ALL_ + lc + u003E + e.size() + _ELEMENTS);
		return e;
	}

	/**
	 * Find visible instance of the defined element and the click on it
	 *
	 * @param lc string locator of an element to be waited for
	 */
	public void findThenClick(String lc) {
		List<WebElement> list = findAll(lc);
		WebElement target = null;
		boolean done = false;
		int attempt = 0;
		do
			try {
				for (WebElement e : list)
					if (e.isDisplayed())
						target = e;
				done = true;
			} catch (TimeoutException ex) {
				this.l.logProblem(Severity.S2, CAUGHT_ + ex.getClass().getName()
						+ ", while trying to findThenClick(), during attempt " + ++attempt + " ..."); //$NON-NLS-1$ //$NON-NLS-2$
			}
		while (!done || attempt > EXCEPTION_RETRIES);
		if (null != target)
			target.click();
	}

	/**
	 * Calculate a point placed in a center of a rectangle
	 *
	 * @param r Rectangle
	 * @return Point located in a center of the given Rectangle
	 */
	public static Point getCenter(Rectangle r) {
		return new Point(r.x + r.width / 2, r.y + r.height / 2);
	}

	/**
	 * Calculate a point based on a rectangle and relative coefficients of x and y
	 * coordinates. Coordinate 0.0 corresponds to leftmost or upper part of the
	 * rectangle, and 1.0 is for rightmost of lowest part of an object.
	 *
	 * @param x horizontal location of the requested point relative to the given
	 *          rectangle
	 * @param y vertical location of the requested point relative to the given
	 *          rectangle
	 * @param r base rectangle
	 * @return Point object based on given parameters
	 */
	@SuppressWarnings("boxing")
	public static Point getPoint(double x, double y, Rectangle r) {
		int offsetX = (int) Math.round(Double.valueOf(r.width) * x);
		int offsetY = (int) Math.round(Double.valueOf(r.height) * y);
		return new Point(r.x + offsetX, r.y + offsetY);
	}

	/**
	 * @return browser in which current execution takes place
	 */
	public Platform in() {
		return this.x.in();
	}

	/**
	 * @param b
	 * @return true if current is in defined browser
	 */
	public boolean in(Platform b) {
		return this.x.in(b);
	}

	/**
	 * @param lc
	 * @return true is this Web Element is disabled
	 */
	public boolean isDisabled(String lc) {
		this.l.log(CHECKING_ + WHETHER_ + lc + _IS + _DISABLED);
		return !this.d.findElement(by(lc)).isEnabled();
	}

	/**
	 * @param lc
	 * @return true is this Web Element is disabled
	 */
	public boolean isEnabled(String lc) {
		this.l.log(CHECKING_ + WHETHER_ + lc + _IS + _ENABLED);
		return this.d.findElement(by(lc)).isEnabled();
	}

	/**
	 * @param lc
	 * @return true is this Web Element is present in DOM
	 */
	public boolean isPresent(String lc) {
		this.l.log(CHECKING_ + VISIBILITY_OF_ + lc);
		return !this.d.findElements(by(lc)).isEmpty();
	}

	/**
	 * Shortcut for {@link works.lysenko.Logger#l.log(ll, s)}
	 *
	 * @param ll
	 * @param s
	 */
	public void log(int ll, String s) {
		this.l.log(ll, s);
	}

	/**
	 * Shortcut for {@link works.lysenko.Logger#l.log(s)}
	 *
	 * @param s
	 */
	public void log(String s) {
		this.l.log(s);
	}

	/**
	 * Shortcut for {@link works.lysenko.Logger#l.logProblem(s)}
	 *
	 * @param se
	 * @param st
	 */
	public void logProblem(Severity se, String st) {
		this.l.logProblem(se, st);
	}

	private void makeCodeshot(String p, String f) {
		String ext = u002E + (this.x.in(ANDROID) ? XML : HTML);
		try {
			Files.writeString(Path.of(p, f + ext), this.d.getPageSource());
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
		makeScreenshot(null, shotLocation(), n);
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
		makeScreenshot(element, shotLocation(), name);
	}

	private void makeScreenshot(WebElement element, String path, String name) {
		File src = null;
		if (element == null)
			src = ((TakesScreenshot) this.d).getScreenshotAs(OutputType.FILE);
		else
			src = ((TakesScreenshot) element).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(src, new File(path + name + u002E + PNG));
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
		makeScreenshot(null, shotLocation(), f);
		makeCodeshot(shotLocation(), f);
	}

	/**
	 * Open a page defined by url or domain.
	 *
	 * @param u domain or full url
	 */
	public void open(String u) {
		URL url = null;
		try {
			url = new URL(u);
		} catch (@SuppressWarnings("unused") MalformedURLException e) {
			try {
				url = new URL(HTTP + u0073 + u003A + u002F + u002F + u);
			} catch (@SuppressWarnings("unused") MalformedURLException f1) {
				try {
					url = new URL(HTTP + u003A + u002F + u002F + u);
				} catch (MalformedURLException f2) {
					f2.printStackTrace();
				}
			}
		}
		if (null != url) {
			this.l.log(OPENING_ + url.toString());
			this.d.get(url.toString());
		}
	}

	/**
	 * Open a page defined by protocol and domain. Protocol parameter could be
	 * overridden by directly specifying protocol in the domain parameter value
	 *
	 * @param p default protocol
	 * @param u domain or full url
	 */
	public void open(String p, String u) {
		URL url = null;
		try {
			url = new URL(u);
		} catch (@SuppressWarnings("unused") MalformedURLException e) {
			try {
				url = new URL(p + u003A + u002F + u002F + u);
			} catch (MalformedURLException f) {
				f.printStackTrace();
			}
		}
		if (null != url) {
			this.l.log(OPENING_ + url.toString());
			this.d.get(url.toString());
		}
	}

	/**
	 * Populate links to sub-objects from Execution
	 */
	public void populateShortcuts() {
		this.d = this.x.d;
		this.l = this.x.l;
		this.o = this.x.o;
		this.r = this.x.r;
		this.w = this.x.w;
	}

	private static String quote(String s) {
		return u0027 + s + u0027;
	}

	/**
	 * Read contents of the element defined by string locator
	 *
	 * @param lc string locator of an element
	 * @return result of .getText() for this element
	 */
	public String read(String lc) {
		this.l.log(READING_ + FROM_ + lc);
		return this.d.findElement(by(lc)).getText();
	}

	/**
	 * Read contents of the WebElement
	 *
	 * @param e WebElement to read text from
	 * @return text
	 */
	public String read(WebElement e) {
		return read(e, true);
	}

	/**
	 * Read contents of given WebElement with optional logging of geometry
	 * information
	 *
	 * @param e
	 * @param geometry
	 * @return result of .getText() for this element
	 */
	public String read(WebElement e, boolean geometry) {
		this.l.log(READING_ + FROM_ + describe(e, geometry));
		return e.getText();
	}

	/**
	 * @return contents of Clipboard as String
	 */
	public static String readClipboard() {
		String result = null;
		try {
			result = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Read value attribute of the element defined by String locator
	 *
	 * @param lc string locator of an element
	 * @return contents of value attribute of the element
	 */
	public String readInput(String lc) {
		this.l.log(READING_ + FROM_ + lc);
		return this.d.findElement(by(lc)).getAttribute(VALUE);
	}

	/**
	 * Read value attribute of the element
	 *
	 * @param e an element
	 * @return contents of value attribute of the element
	 */
	public String readInput(WebElement e) {
		this.l.log(READING_ + FROM_ + describe(e));
		return e.getAttribute(VALUE);
	}

	/**
	 * Produce BASE64 encoded string screenshot of an element
	 *
	 * @param lc string locator of requested element
	 * @return BASE64 encoded string screenshot
	 */
	public String screenShot(String lc) {
		return ((TakesScreenshot) this.d.findElement(by(lc))).getScreenshotAs(OutputType.BASE64);
	}

	/**
	 * Add a section title to the test log
	 *
	 * @param s string to be added as section title
	 */
	public void section(String s) {
		this.l.logln();
		this.l.log(0, Ansi.colorize(u003D + s + u003D, Ansi.BLUE_BOLD_BRIGHT));
	}

	/**
	 * Send keys as Action
	 *
	 * @param s CharSequence to send
	 */
	public void sendKeys(CharSequence s) {
		sendKeys(s, 1);
	}

	/**
	 * Send keys as Action defined number of times
	 *
	 * @param s CharSequence to send
	 * @param i times to repeat
	 */
	public void sendKeys(CharSequence s, int i) {
		int i2 = i;
		do
			new Actions(this.x.d).sendKeys(s).build().perform();
		while (--i2 > 0);
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

	private String shotLocation() {
		return SCREENSHOTS + this.x.t.startedAt() + u002F;
	}

	/**
	 * Sleep during defined amount of milliseconds
	 *
	 * @param ms
	 */
	public void sleep(long ms) {
		sleep(ms, null);
	}

	/**
	 * Sleep during defined amount of milliseconds with optional bypassing of
	 * logging
	 *
	 * @param ms
	 * @param silent
	 */
	public void sleep(long ms, boolean silent) {
		sleep(ms, null, silent);
	}

	/**
	 * Sleep during defined amount of milliseconds with optional custom message
	 *
	 * @param ms amount of milliseconds to pause for
	 * @param s
	 */
	@SuppressWarnings("nls")
	public void sleep(long ms, String s) {
		if (ms < SILENT_SLEEPING_TRESHOLD)
			this.l.log(0, Ansi.colorize("[WARNING]: " + "Sleeping for less then " + SILENT_SLEEPING_TRESHOLD
					+ " ms is better to be perfomed in silent mode"));
		sleep(ms, s, false);
	}

	/**
	 * Sleep during defined amount of milliseconds with optional custom message and
	 * optional bypassing of logging
	 *
	 * @param ms     amount of milliseconds to pause for
	 * @param s      text to displey in log, can be set to null for default
	 *               "Sleeping X ms" message
	 * @param silent whether to bypass output to test log. Useful for short delays
	 */
	public void sleep(long ms, String s, boolean silent) {
		try {
			if (!silent)
				this.l.log(null == s ? SLEEPING_ + ms + _MS : s);
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switch to other WebDriver window
	 *
	 * @param window handle to switch to
	 */
	public void switchTo(String window) {
		this.l.log(SWITCHING_ + TO_ + window);
		this.d.switchTo().window(window);
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
	@SuppressWarnings("nls")
	public boolean typeInto(WebElement e, Object c, double pp, boolean secret) {
		CharSequence symbols = String.valueOf(c);
		this.l.log(TYPING_ + quote(secret ? u2022.repeat(symbols.length()) : String.valueOf(c)) + _INTO_ + describe(e));
		try {
			if (isTrue(pp)) {
				// simulation of pasting from clipboard by instant addition of all content
				e.sendKeys(symbols);
				return true;
			}
			char[] chars = symbols.toString().toCharArray();
			for (char ch : chars) {
				sleep(integer(0, 50), true); // silent sleep
				e.sendKeys(String.valueOf(ch));
			}
			return false;
		} catch (Exception ex) {
			logProblem(Severity.S2,
					"Exception " + ex.getClass().getName() + " caught while trying to type "
							+ quote(secret ? u2022.repeat(symbols.length()) : String.valueOf(c)) + _INTO_ + describe(e)
							+ ", attempting workaround ...");
			Actions action = new Actions(this.x.d);
			action.moveToElement(e).click().sendKeys(String.valueOf(c)).build().perform();
			return true;
		}
	}

	/**
	 * @param uploader locator of upload input
	 * @param name     filename of file to upload
	 */
	public void upload(String uploader, String name) {
		this.l.log(UPLOADING_ + quote(name) + _THROUGH_ + uploader);
		find(false, false, uploader).sendKeys(System.getProperty(USER_DIR) + u002F + RESOURCES + name);
	}

	/**
	 * Verify that defined element is enabled and then click on it
	 *
	 * @param lc string locator of an element to be waited for
	 */
	public void verifyThenClick(String lc) {
		WebElement e = find(lc);
		Assertions.assertTrue(e.isEnabled());
		click(lc);
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
		this.l.log(WAITING_ + FOR_ + CLICKABILITY_OF_ + lc);
		this.w.until(ExpectedConditions.elementToBeClickable(by(lc)));
	}

	/**
	 * Wait for an element to be not visible
	 *
	 * @param lc string locator of an element expected to be invisible
	 */
	public void waitInvisibility(String lc) {
		this.l.log(WAITING_ + FOR_ + INVISIBILITY_OF_ + lc);
		this.w.until(ExpectedConditions.invisibilityOfElementLocated(by(lc)));
	}

	/**
	 * Wait for an element to be selected
	 *
	 * @param lc string locator of an element expected to be selected
	 */
	public void waitSeleted(String lc) {
		this.l.log(WAITING_ + FOR_ + lc + TO_BE_SELECTED);
		this.w.until(ExpectedConditions.visibilityOfElementLocated(by(lc)));
	}

	/**
	 * Wait for appearance of the defined element and the click on it
	 *
	 * @param lc string locator of an element to be waited for
	 * @param X
	 * @param Y
	 */
	public void waitThenClick(double X, double Y, String lc) {
		wait(lc);
		click(X, Y, lc);
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
	 * Wait for appearance of the defined element and return reference to it
	 *
	 * @param lc string locator of an element to be waited for
	 * @return element
	 */
	public WebElement waitThenFind(String lc) {
		wait(lc);
		return find(lc);
	}

	/**
	 * Wait for appearance of the defined element and return reference to it
	 *
	 * @param lc string locator of an element to be waited for
	 * @return element
	 */
	public WebElement waitThenFind(String[] lc) {
		wait(lc);
		return find(lc);
	}

	/**
	 * Wait for a text value of an element to be as defined
	 *
	 * @param lc string locator of an element
	 * @param s  expected value
	 */
	public void waitValue(String lc, String s) {
		this.l.log(WAITING_ + FOR_ + lc + _TO + _HAVE_VALUE_ + quote(s));
		this.w.until(ExpectedConditions.textToBe(by(lc), s));
	}

	/**
	 * Wait for a text value of an element to change from defined one
	 *
	 * @param lc string locator of an element
	 * @param s  the value to change from
	 * @return value after the detected change
	 */
	public String waitValueNot(String lc, String s) {
		this.l.log(WAITING_ + WHILE_ + lc + _STILL + _HAVE_VALUE_ + quote(s));
		this.w.until(ExpectedConditions.not(ExpectedConditions.textToBe(by(lc), s)));
		return read(lc);
	}

	/**
	 * Wait for a text value to be not empty
	 *
	 * @param lc string locator of an element
	 * @return value after the detected change
	 */
	public String waitValueNotEmpty(String lc) {
		// TODO: this routine seems to be working not as expected
		this.l.log(WAITING_ + WHILE_ + lc + _IS + _STILL + _EMPTY);
		this.w.until(ExpectedConditions.not(ExpectedConditions.textToBe(by(lc), EMPTY)));
		sleep(333);
		String t = read(lc);
		this.l.log(THE_TEXT_OF_ + lc + _IS + _NOW_ + quote(t));
		return t;
	}

	/**
	 * Wait for appearance of the defined element
	 *
	 * @param lc string locator of an element to be waited for
	 */
	public void waitVisibility(String lc) {
		this.l.log(WAITING_ + FOR_ + VISIBILITY_OF_ + lc);
		this.w.until(ExpectedConditions.visibilityOfElementLocated(by(lc)));
	}

	/**
	 * Wait for appearance of the defined element
	 *
	 * @param lc string locator of an element to be waited for
	 */
	public void waitVisibility(String[] lc) {
		this.l.log(WAITING_ + FOR_ + VISIBILITY_OF_ + Arrays.toString(lc));
		this.w.until(ExpectedConditions.visibilityOfElementLocated(by(lc)));
	}

}
