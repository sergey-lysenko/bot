package works.lysenko;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.InteractsWithApps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import works.lysenko.enums.Ansi;
import works.lysenko.enums.Platform;
import works.lysenko.enums.Severity;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static works.lysenko.Constants.EXCEPTION_RETRIES;
import static works.lysenko.Constants.RESOURCES;
import static works.lysenko.Constants.RUN_SNAPSHOT_FILENAME;
import static works.lysenko.Constants.SILENT_SLEEPING_THRESHOLD;
import static works.lysenko.enums.Ansi.colorize;
import static works.lysenko.enums.Platform.ANDROID;
import static works.lysenko.enums.Platform.CHROME;
import static works.lysenko.enums.Platform.FIREFOX;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "BooleanParameter", "StaticMethodOnlyUsedInOneClass", "OverlyComplexClass", "ClassWithTooManyMethods", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "LocalVariableHidesMemberVariable", "BoundedWildcard", "AccessOfSystemProperties", "ReturnOfNull", "ClassWithoutLogger", "PublicMethodWithoutLogging", "OverloadedVarargsMethod", "ClassWithTooManyDependents", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "MethodWithMultipleLoops", "WeakerAccess"})
public class Common {

    /**
     *
     */
    @SuppressWarnings("UseOfConcreteClass")
    public Output o = null;
    /**
     *
     */
    @SuppressWarnings({"UseOfConcreteClass", "StandardVariableNames"})
    public Logger l = null;
    /**
     *
     */
    @SuppressWarnings("UseOfConcreteClass")
    public Results r = null;
    /**
     *
     */
    @SuppressWarnings({"ClassReferencesSubclass", "UseOfConcreteClass"})
    public Execution x = null;
    /**
     *
     */
    @SuppressWarnings("StandardVariableNames")
    public WebDriver d = null;
    /**
     *
     */
    public WebDriverWait w = null;

    /**
     *
     */
    @SuppressWarnings("PublicConstructor")
    public Common() {
    }

    /**
     * @param execution instance of Execution object
     */
    @SuppressWarnings({"ClassReferencesSubclass", "UseOfConcreteClass", "PublicConstructor", "OverridableMethodCallDuringObjectConstruction"})
    public Common(Execution execution) {
        x = execution;
        populateShortcuts();
    }

    /**
     * Generate {@link org.openqa.selenium.By} object corresponding to given string
     * locator
     *
     * @param lc string
     * @return proper locator object based on contents of source string contents
     */
    @SuppressWarnings({"HardcodedFileSeparator", "OverlyComplexBooleanExpression", "ImplicitNumericConversion", "MethodWithMultipleReturnPoints", "MagicCharacter"})
    public static By by(String lc) {
        if (lc.startsWith("//") || lc.startsWith("./") || lc.startsWith(".//") || lc.startsWith("(.//") || lc.startsWith("(//"))
            return By.xpath(lc);
        if (!lc.isEmpty() && '#' == lc.charAt(0) || lc.contains(" > ") || lc.contains(":nth-"))
            return By.cssSelector(lc);
        return by(Descriptors.locatorOf(lc));
    }

    /**
     * Generate {@link org.openqa.selenium.By} object corresponding to given string
     * locator
     *
     * @param locators array of locator strings to randomly select from
     * @return one of locators
     */
    public static By by(String[] locators) {
        return by((String) selectOneOf(locators));
    }

    /**
     * Fast concatenation of text representations into single string
     *
     * @param strings strings to join
     * @return concatenated string
     */
    @SuppressWarnings("StringBufferWithoutInitialCapacity")
    public static String c(Object... strings) {
        StringBuilder sb = new StringBuilder();
        for (Object o : strings)
            sb.append(o);
        return sb.toString();
    }

    /**
     * Read contents of the text file into List of Strings
     *
     * @param path to a file
     * @return list of lines from a file
     */
    @SuppressWarnings({"CollectionWithoutInitialCapacity", "CallToSuspiciousStringMethod", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    public static List<String> loadLinesFromFile(Path path) {
        List<String> out = new ArrayList<>();
        try (Stream<String> in = Files.lines(path)) {
            in.forEach(r -> {
                String clean = r.split("#")[0].trim();
                if (!clean.isEmpty()) out.add(clean);
            });
        } catch (IOException e) {
            throw new IllegalArgumentException(c("Unable to read lines from '", path.toString(), "'"));
        }
        return out;
    }

    /**
     * Return first line of file as String
     *
     * @param path to a file
     * @return list of lines from a file
     */
    @SuppressWarnings("ChainedMethodCall")
    public static String loadStringFromFile(Path path) {
        return loadLinesFromFile(path).get(0);
    }

    /**
     * Shortcut for {@link java.lang.String#format(String, Object...)}
     *
     * @param template template
     * @param value    value
     * @return filled in string template
     */
    public static String fill(String template, Object... value) {
        return String.format(template, value);
    }

    /**
     * Generate a random integer in the defined range.
     *
     * @param from minimum value
     * @param to   maximum value
     * @return random integer
     */
    @SuppressWarnings({"ChainedMethodCall", "AutoBoxing"})
    public static Integer integer(int from, int to) {
        return new RandomDataGenerator().nextInt(from, to);
    }

    /**
     * Generate a random integer. With the range and list of values to exclude.
     *
     * @param from minimum value
     * @param to   maximum value
     * @param x    list of values to exclude
     * @return random integer
     */
    @SuppressWarnings({"unused", "ChainedMethodCall", "ObjectAllocationInLoop", "MethodCallInLoopCondition", "AutoBoxing"})
    public static Integer integer(int from, int to, Integer... x) {
        Integer n; // Non-primitive for asList() compatibility
        do n = new RandomDataGenerator().nextInt(from, to); while (Arrays.asList(x).contains(n));
        return n;
    }

    /**
     * Return 'true' of 'false' with equal probability
     *
     * @return random boolean value
     */
    @SuppressWarnings("MagicNumber")
    public static boolean isTrue() {
        return isTrue(0.5);
    }

    /**
     * Return 'true' with defined probability, or 'false' otherwise
     *
     * @param d probability of 'true' selection
     * @return random boolean value
     */
    @SuppressWarnings({"ChainedMethodCall", "ImplicitNumericConversion"})
    public static boolean isTrue(double d) {
        if (1.0 < d || 0.0 > d)
            throw new IllegalArgumentException(c("Given probability ", d, " is outside the valid range of [0.0 - 1.0]"));
        return new SecureRandom().nextFloat() < d;
    }

    /**
     * @param <K>   key type
     * @param <V>   value type
     * @param map   to operate on
     * @param value to search for
     * @return key of an element by its value
     */
    @SuppressWarnings("ChainedMethodCall")
    public static <K, V> Stream<K> keys(Map<K, V> map, V value) {
        return map.entrySet().stream().filter(entry -> value.equals(entry.getValue())).map(Map.Entry::getKey);
    }

    /**
     * Repeat defined key given amount times
     *
     * @param keys        key to repeat
     * @param repetitions times to repeat
     * @return requested sequence
     */
    @SuppressWarnings("unused")
    public static String repeat(Keys keys, int repetitions) {
        String sequence = "";
        for (int i = 0; i < repetitions; i++)
            sequence = c(sequence, keys);
        return sequence;
    }

    /**
     * Get a random object from a list of objects
     *
     * @param <T>  objects type
     * @param list list of objects
     * @return a random object from a list of objects
     */
    @SuppressWarnings("ChainedMethodCall")
    public static <T> T selectOneOf(List<T> list) {
        return list.get(new SecureRandom().nextInt(list.size()));
    }

    /**
     * Get a random object from an array of objects
     *
     * @param a array of objects
     * @return a random element from an array of objects
     */
    @SuppressWarnings("ChainedMethodCall")
    public static Object selectOneOf(Object[] a) {
        return a[new SecureRandom().nextInt(Array.getLength(a))];
    }

    /**
     * Get a random object from a set of objects
     *
     * @param <T>        objects type
     * @param collection of objects
     * @return a random object from a set of objects
     */
    @SuppressWarnings("ChainedMethodCall")
    public static <T> Object selectOneOf(Collection<T> collection) {
        return collection.toArray()[new SecureRandom().nextInt(collection.size())];
    }

    /**
     * Get an element from a list of strings different from defined one.
     * Presence of a string in a list not verified, only the exclusion
     * of defined value performed.
     *
     * @param exclude string to exclude
     * @param list    list of strings to exclude from
     * @return an element from a list of strings, which differs from specified one.
     */
    @SuppressWarnings("unused")
    public static String selectOther(String exclude, List<String> list) {
        List<String> copy = new ArrayList<>(list);
        copy.remove(exclude);
        return selectOneOf(copy);
    }

    /**
     * @param t milliseconds
     * @return String human-readable representation of given milliseconds time
     * interval
     */
    @SuppressWarnings({"MethodWithMultipleReturnPoints", "MagicCharacter"})
    public static String timeH(long t) {
        String s = String.valueOf(t);
        if (3 < s.length())
            return c(String.valueOf(ArrayUtils.insert(s.length() - 3, String.valueOf(t).toCharArray(), '.')), " s");
        return c(s, " ms");
    }

    /**
     * Supply visual representation for non-printable character
     *
     * @param keys key to visualize
     * @return visual representation for non-printable character
     */
    @SuppressWarnings({"unused", "SwitchStatement", "MethodWithMultipleReturnPoints"})
    public static String visualize(Keys keys) {
        switch (keys) {
            case ARROW_DOWN:
                return "↓";
            case ARROW_UP:
                return "↑";
            default:
                return keys.toString();
        }
    }

    /**
     * Write lines of the provided set to a file
     *
     * @param firstLine optional first line of the text to be added to a file (title)
     * @param strings   set of lines to be stored in a file
     * @param fileName  name of the file to be written
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored", "ImplicitDefaultCharsetUsage", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    public static void writeToFile(String firstLine, Iterable<String> strings, String fileName) {
        new File(fileName).getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            if (null != firstLine) writer.write(c(firstLine, System.lineSeparator()));
            for (String s : strings)
                writer.write(c(s, System.lineSeparator()));
        } catch (IOException e) {
            throw new IllegalArgumentException(c("Unable to write to file '", fileName, "'"));
        }
    }

    @SuppressWarnings({"OverlyBroadCatchBlock", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    static Properties readProperties(String name) {
        Properties prop = new Properties();
        if (null != name) {
            try (FileInputStream fis = new FileInputStream(name)) {
                prop.load(fis);
            } catch (IOException e) {
                throw new IllegalArgumentException(c("Unable to load requested '", name, "' properties file"));
            }
        }
        return prop;
    }

    @SuppressWarnings("AutoBoxing")
    private static String describe(Rectangle r) {
        return c("h", r.height, " w", r.width, " @ ", "x", r.x, " y", r.y);
    }

    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private static String describe(String[] lc) {
        if (0 == lc.length) return "";
        if (1 == lc.length) return lc[0];
        return Arrays.toString(lc);
    }

    /**
     * @param e  element to search in
     * @param lc locator of sub-element to search for
     * @return child element of e defined by c
     */
    @SuppressWarnings("unused")
    public static WebElement find(SearchContext e, String lc) {
        return e.findElement(by(lc));
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
     * rectangle, and 1.0 is for rightmost of the lowest part of an object.
     *
     * @param x horizontal location of the requested point relative to the given
     *          rectangle
     * @param y vertical location of the requested point relative to the given
     *          rectangle
     * @param r base rectangle
     * @return Point object based on given parameters
     */
    @SuppressWarnings("NumericCastThatLosesPrecision")
    public static Point getPoint(double x, double y, Rectangle r) {
        @SuppressWarnings("UnnecessaryExplicitNumericCast") int offsetX = (int) Math.round((double) r.width * x);
        @SuppressWarnings("UnnecessaryExplicitNumericCast") int offsetY = (int) Math.round((double) r.height * y);
        return new Point(r.x + offsetX, r.y + offsetY);
    }

    /**
     * @return contents of Clipboard as String
     */
    @SuppressWarnings({"unused", "ChainedMethodCall", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    public static String readClipboard() {
        String result;
        try {
            result = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            throw new IllegalStateException("Unable to read clipboard");
        }
        return result;
    }

    /**
     * Generate "Any element with given value of 'text' attribute" descriptor. Used for Android testing mostly.
     *
     * @param text to search for
     * @return locator for text attribute of any element
     */
    @SuppressWarnings({"HardcodedFileSeparator", "StandardVariableNames"})
    public static String text(String text) {
        String k = c("Text \"", text, "\"");
        String v = c("//*[@text='", text, "']");
        Descriptors.put(k, v);
        return k;
    }

    /**
     * Wrapper for {@link Common#text(String)} accepting {@code int} values
     *
     * @param i int value to search for
     * @return locator for text attribute of any element
     */
    public static String text(int i) {
        return text(String.valueOf(i));
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings({"FeatureEnvy", "DuplicateStringLiteralInspection"})
    public void clear(boolean sendKeys, WebElement e) {
        l.log(c("Clearing ", describe(e)));
        if (x._debug()) l.log(c(" text in '", describe(e), "' before clearing: '", e.getAttribute("value"), "'"));
        if (sendKeys) {
            e.sendKeys(Keys.CONTROL, "a");
            e.sendKeys(Keys.BACK_SPACE);
        } else e.clear();
        if (x._debug()) l.log(c(" text in '", describe(e), "' after clearing: '", e.getAttribute("value"), "'"));
    }

    /**
     * Clear an input by executing {@link org.openqa.selenium.WebElement#clear()}
     * method
     *
     * @param lc locator(s) of elements to clear
     */
    @SuppressWarnings("unused")
    public void clear(String... lc) {
        WebElement e = find(false, true, lc);
        clear(e);
    }

    /**
     * Clear an input by executing {@link org.openqa.selenium.WebElement#clear()}
     * method
     *
     * @param e element to clear
     */
    public void clear(WebElement e) {
        clear(false, e);
    }

    /**
     * Perform a non-centered click on an element. Exact location of click defined
     * by two double typed relative x and y coordinates, where value 0.0 corresponds
     * to leftmost or upmost part of the element, as 1.0 is for rightmost of the lowest
     * part of an object.
     * <p>
     * Consequently, coordinates (0.5, 0.5) defining a center of the object
     *
     * @param x  requested x coordinate offset
     * @param y  requested y coordinate offset
     * @param lc string locator(s) of element to be clicked on
     */
    @SuppressWarnings({"FeatureEnvy", "AutoBoxing", "DuplicateStringLiteralInspection"})
    public void clickOn(double x, double y, String... lc) {
        l.log(c("Clicking on '", describe(lc), "' at (x", x, ", y", y, ")"));
        l.log(2, c("Locating the element '", describe(lc), "' ..."));
        WebElement e = find(true, true, lc);
        Rectangle re = e.getRect();
        l.log(2, c("Element's rectangle is '", describe(re), "' ..."));
        Point center = getCenter(re);
        Point p = getPoint(x, y, re);
        int offsetX = p.x - center.x;
        int offsetY = p.y - center.y;
        l.log(2, c("Calculated offset in pixels from center is  (x", offsetX, ", y", offsetY, ") ..."));
        Actions actions = new Actions(d);
        actions.moveToElement(e);
        actions.moveByOffset(offsetX, offsetY);
        actions.click();
        actions.perform();
    }

    /**
     * @param lc string locator of element to be clicked on
     */
    @SuppressWarnings({"FeatureEnvy", "ValueOfIncrementOrDecrementUsed", "ChainedMethodCall", "BooleanVariableAlwaysNegated", "AutoBoxing", "MethodWithMultipleReturnPoints", "DuplicateStringLiteralInspection"})
    public void clickOn(String... lc) {
        if (0 < lc.length) {
            boolean done = false;
            int attempt = 0;
            do try {
                WebElement e = find(false, true, lc);
                l.log(c("Clicking on '", describe(lc), "'")); //NON-NLS
                if (null != e) {
                    e.click();
                    done = true;
                } else {
                    l.logProblem(Severity.S2, "element is null");
                    return;
                }
            } catch (StaleElementReferenceException ex) {
                l.logProblem(Severity.S2, c("Caught ", ex.getClass().getName(), ", while trying to click(), during attempt ", ++attempt, " ..."));
            } while (!done && EXCEPTION_RETRIES >= attempt);
        } else l.logProblem(Severity.S3, "Empty locators list in click()");
    }

    /**
     * @param e WebElement to click on
     */
    @SuppressWarnings("unused")
    public void clickOn(WebElement e) {
        clickOn(e, true);
    }

    /**
     * @param e        element to be clicked on
     * @param geometry whether to output information about geometry or not
     */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void clickOn(WebElement e, boolean geometry) {
        l.log(c("Clicking on '", describe(e, geometry), "'"));
        e.click();
    }

    /**
     * Shortcut for checking the presence of a data in the common test data
     * container
     *
     * @param field of test data to be verified
     * @return true if data is present
     */
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    public boolean containsKey(Object field) {
        boolean containsKey = x.dataContainsKey(field);
        if (x._debug()) log(3, c("containsKey(", field, ")", "\u2192", containsKey));
        return containsKey;
    }

    /**
     * Shortcut for checking the presence of a data in the common test data
     * container
     *
     * @param fields of test data to be verified
     * @return true if all data is present
     */
    @SuppressWarnings({"unused", "IfStatementMissingBreakInLoop", "UnnecessaryUnicodeEscape"})
    public boolean containsKeys(Object... fields) {
        boolean containsKeys = true;
        for (Object o : fields)
            if (!x.dataContainsKey(o)) containsKeys = false;
        if (x._debug()) log(3, c("containsKeys(", Arrays.toString(fields), ")", "\u0336", containsKeys));
        return containsKeys;
    }

    private String describe(WebElement e) {
        return describe(e, true);
    }

    private String describe(WebElement e, boolean geometry) {
        String an = x.in(CHROME) ? e.getAccessibleName() : "";
        String tg = e.getTagName();
        return c(((null == tg) ? "element" : tg), (an.isEmpty() ? "" : c(" '", an, "'")), (geometry ? c(" @ ", describe(e.getRect())) : ""));
    }

    /**
     * @param locator of element
     * @return true if element exists
     */
    @SuppressWarnings({"ChainedMethodCall", "BooleanMethodNameMustStartWithQuestion"})
    public boolean exists(String locator) {
        int oc = findAll(locator).size();
        return 0 < oc;
    }

    /**
     * Find an element defined by one or several nested String locators.
     *
     * @param silent   if true, no output added to log
     * @param scrollTo if true, window scrolled to make this element visible on screen
     * @param lc       one or several nested locators
     * @return WebElement object
     */
    @SuppressWarnings({"FeatureEnvy", "MagicNumber", "ValueOfIncrementOrDecrementUsed", "ChainedMethodCall", "BooleanVariableAlwaysNegated", "ReturnOfNull", "HardcodedFileSeparator", "ArrayLengthInLoopCondition", "AutoBoxing", "ImplicitNumericConversion", "OverlyComplexMethod", "MethodWithMultipleReturnPoints", "MethodWithMoreThanThreeNegations", "DuplicateStringLiteralInspection"})
    public WebElement find(boolean silent, boolean scrollTo, String... lc) {

        WebElement e = null;
        boolean done = false;
        int attempt = 0;
        do try {
            if (!silent && 0 < lc.length) l.log(c("Finding '", lc[0], "'"));
            if (0 == lc.length) e = d.findElement(by("//body"));
            if (0 < lc.length) e = d.findElement(by(lc[0]));
            if (1 < lc.length) for (int i = 1; i < lc.length; i++) {
                if (!silent) l.log(c(" ... and child '", lc[i], "'"));
                if (null != e) e = e.findElement(by(lc[i]));
            }
            done = true;
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException ex) {
            l.logProblem(Severity.S2, c("Caught '", ex.getClass().getName(), "', while trying to find(), during attempt '", ++attempt, "' ..."));
            sleep(333);
        } while (!done && EXCEPTION_RETRIES > attempt);
        if (EXCEPTION_RETRIES == attempt) {
            logProblem(Severity.S1, "Maximum retries amount reached, find() returns null, test failure imminent");
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

        if (in(FIREFOX)) ((JavascriptExecutor) x.d).executeScript("arguments[0].scrollIntoView(true);", e);
        else {
            Actions actions = new Actions(d);
            actions.moveToElement(e);
            actions.perform();
        }
        return e;
    }

    /**
     * Find all elements defined by string locator
     *
     * @param lc locator of 1-n elements to be found
     * @return list of located element references
     */
    @SuppressWarnings({"CollectionWithoutInitialCapacity", "AutoBoxing"})
    public List<WebElement> findAll(String lc) {
        List<WebElement> e;
        if (null == lc) e = new ArrayList<>();
        else e = d.findElements(by(lc));
        l.log(c("Finding all '", lc, "' > '", e.size(), "' elements"));
        return e;
    }

    /**
     * Find visible instance of the defined element and the click on it
     *
     * @param lc string locator of an element to be waited for
     */
    @SuppressWarnings({"unused", "ValueOfIncrementOrDecrementUsed", "ChainedMethodCall", "BooleanVariableAlwaysNegated", "AutoBoxing", "DuplicateStringLiteralInspection"})
    public void findThenClick(String lc) {
        List<WebElement> list = findAll(lc);
        WebElement target = null;
        boolean done = false;
        int attempt = 0;
        do try {
            for (WebElement e : list)
                if (e.isDisplayed()) target = e;
            done = true;
        } catch (TimeoutException ex) {
            l.logProblem(Severity.S2, c("Caught '", ex.getClass().getName(), "', while trying to findThenClick(), during attempt '", ++attempt, "' ..."));
        } while (!done || EXCEPTION_RETRIES < attempt);
        if (null != target) target.click();
    }

    /**
     * Shortcut for getting a data from the common test data container
     *
     * @param field of test data to be retrieved
     * @return copy of test data
     */
    @SuppressWarnings("unused")
    public Object get(Object field) {
        Object o = x.dataGet(field);
        if (x._debug()) getLog(field, o);
        return o;
    }

    /**
     * Shortcut for getting a data from the common test data container, with
     * optional default
     *
     * @param field of test data to be retrieved
     * @param def   default value
     * @return copy of test data
     */
    @SuppressWarnings("unused")
    public Object get(Object field, Object def) {
        Object o = x.dataGetOrDefault(field, def);
        if (x._debug()) getLog(field, o);
        return o;
    }

    /**
     * Shortcut for getting a Boolean data from the common test data container
     *
     * @param field of test data to be retrieved
     * @return copy of test data
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public Boolean getBoolean(Object field) {
        Boolean o = Boolean.valueOf((String) x.dataGet(field));
        if (x._debug()) getLog(field, o);
        return o;
    }

    /**
     * Shortcut for getting an int data from the common test data container
     *
     * @param field of test data to be retrieved
     * @return copy of test data
     */
    @SuppressWarnings("AutoBoxing")
    public int getInt(Object field) {
        int o = Integer.parseInt((String) x.dataGet(field));
        if (x._debug()) getLog(field, o);
        return o;
    }

    /**
     * Shortcut for getting an int data from the common test data container
     *
     * @param field of test data to be retrieved
     * @param def   value in case of absence of field
     * @return copy of test data
     */
    public int getInt(Object field, int def) {
        return (containsKey(field)) ? getInt(field) : def;
    }

    @SuppressWarnings("UnnecessaryUnicodeEscape")
    private void getLog(Object field, Object o) {
        log(3, c("get(", field, ")", "\u21D2", o, " "));
    }

    /**
     * Shortcut for getting a String data from the common test data container.
     *
     * @param field of test data to retrieve
     * @return copy of test data
     */
    public String getString(Object field) {
        String o = (String) x.dataGet(field);
        if (x._debug()) getLog(field, o);
        return o;
    }

    /**
     * @return browser in which current execution takes place
     */
    @SuppressWarnings("unused")
    public Platform in() {
        return x.in();
    }

    /**
     * @param platform platform to check against
     * @return true if current execution is in defined platform
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean in(Platform platform) {
        return x.in(platform);
    }

    /**
     * @param lc locator
     * @return true is this Web Element is disabled
     */
    @SuppressWarnings({"unused", "ChainedMethodCall"})
    public boolean isDisabled(String lc) {
        l.log(c("Checking whether '", lc, "' is disabled"));
        return !d.findElement(by(lc)).isEnabled();
    }

    /**
     * @param lc locator
     * @return true is this Web Element is disabled
     */
    @SuppressWarnings({"unused", "ChainedMethodCall"})
    public boolean isEnabled(String lc) {
        l.log(c("Checking whether ", lc, " is enabled"));
        return d.findElement(by(lc)).isEnabled();
    }

    /**
     * Verify presence of an element on page
     *
     * @param lc locator
     * @return true is this Web Element is present in DOM
     */
    @SuppressWarnings("unused")
    public boolean isPresent(String lc) {
        return isPresent(lc, false);
    }

    /**
     * Verify presence of an element on page
     *
     * @param lc     locator
     * @param silent whether to post a log message or not
     * @return true is this Web Element is present in DOM
     */
    @SuppressWarnings("ChainedMethodCall")
    public boolean isPresent(String lc, boolean silent) {
        if (!silent) l.log(c("Checking visibility of '", lc, "'"));
        return !d.findElements(by(lc)).isEmpty();
    }

    /**
     * Verify presence of all elements from list on page
     *
     * @param lc locator
     * @return true is all of these Web Elements is present in DOM
     */
    @SuppressWarnings({"ConstantConditions", "unused", "ChainedMethodCall", "AutoBoxing"})
    public boolean isPresentAllOf(String... lc) {
        boolean present = false;
        for (String loc : lc)
            present = (!d.findElements(by(loc)).isEmpty()) && present;
        l.log(c("Checking visibility of all element from list '", Arrays.toString(lc), "' > ", present));
        return present;
    }

    /**
     * Verify presence of any elements from list on page
     *
     * @param lc locator
     * @return true is any of these Web Elements is present in DOM
     */
    @SuppressWarnings({"unused", "ChainedMethodCall", "AutoBoxing"})
    public boolean isPresentAnyOf(String... lc) {
        boolean present = false;
        for (String loc : lc)
            present = (!d.findElements(by(loc)).isEmpty()) || present;
        l.log(c("Checking visibility of any element from list '", Arrays.toString(lc), "' > ", present));
        return present;
    }

    /**
     * Shortcut for {@link works.lysenko.Logger#log(int, String)}
     *
     * @param level  of message
     * @param string of message
     */
    @SuppressWarnings("QuestionableName")
    public void log(int level, String string) {
        l.log(level, string);
    }

    /**
     * Shortcut for {@link works.lysenko.Logger#log(String)}
     *
     * @param string of message
     */
    @SuppressWarnings("QuestionableName")
    public void log(String string) {
        l.log(string);
    }

    /**
     * Shortcut for {@link works.lysenko.Logger#logProblem(Severity, String)}
     *
     * @param se of a problem
     * @param st description of a problem
     */
    public void logProblem(Severity se, String st) {
        l.logProblem(se, st);
    }

    @SuppressWarnings({"StandardVariableNames", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    private void makeCodeshot(String f) {
        String ext = x.in(ANDROID) ? "xml" : "html";
        try {
            Files.writeString(Path.of(shotLocation(f, ext)), d.getPageSource());
        } catch (IOException e) {
            throw new IllegalArgumentException(c("Unable to write code snapshot to '", f, "'"));
        }
    }

    /**
     * Produce a full-screen PNG Screenshot and save it in a location defined by
     *
     * @param name of the screenshot
     */
    @SuppressWarnings("unused")
    public void makeScreenshot(String name) {
        makeScreenshot(null, name);
    }

    @SuppressWarnings({"SameParameterValue", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    private void makeScreenshot(WebElement element, String name) {
        File src;
        if (null == element) src = ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
        else src = element.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(src, new File(shotLocation(name, "png")));
        } catch (IOException e) {
            throw new IllegalArgumentException(c("Unable to write screenshot '", name, "'"));
        }
    }

    /**
     * Simultaneously make a full-screen named screenshot and a copy of page html
     * code
     *
     * @param name of snapshot
     */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void makeSnapshot(String name) {
        if (x._debug()) log(3, c("Making '", name, "' screenshot "));
        makeScreenshot(null, name);
        if (x._debug()) log(3, c("Making '", name, "' snapshot of page code"));
        makeCodeshot(name);
    }

    /**
     * Open a page defined by url or domain.
     *
     * @param u domain or full url
     */
    @SuppressWarnings({"unused", "ConstantConditions", "DuplicateStringLiteralInspection", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    public void open(String u) {
        URL url = null;
        try {
            url = new URL(u);
        } catch (MalformedURLException e) {
            try {
                url = new URL("https://" + u);
            } catch (MalformedURLException f1) {
                try {
                    url = new URL("http://" + u);
                } catch (MalformedURLException f2) {
                    throw new IllegalArgumentException(c("Unable to open '", url.toString(), "'"));
                }
            }
        }
        if (null != url) {
            l.log(c("Opening ", url));
            d.get(url.toString());
        }
    }

    /**
     * Open a page defined by protocol and domain. Protocol parameter could be
     * overridden by directly specifying protocol in the domain parameter value
     *
     * @param p default protocol
     * @param u domain or full url
     */
    @SuppressWarnings({"unused", "ConstantConditions", "HardcodedFileSeparator", "DuplicateStringLiteralInspection", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    public void open(String p, String u) {
        URL url = null;
        try {
            url = new URL(u);
        } catch (MalformedURLException e) {
            try {
                url = new URL(p + "://" + u);
            } catch (MalformedURLException e1) {
                throw new IllegalArgumentException(c("Unable to open '", url.toString(), "'"));
            }
        }
        if (null != url) {
            l.log(c("Opening ", url));
            d.get(url.toString());
        }
    }

    /**
     * Populate links to sub-objects from Execution
     */
    public void populateShortcuts() {
        d = x.d;
        l = x.l;
        o = x.o;
        r = x.r;
        w = x.w;
    }

    /**
     * Shortcut for putting a data into the common test data container
     *
     * @param field of test data to be updated
     * @param value of this field
     */
    @SuppressWarnings({"FeatureEnvy", "UnnecessaryUnicodeEscape"})
    public void put(Object field, Object value) {
        Object o = x.dataPut(field, value);
        if (x._debug()) {
            JSONObject jsonData = x.getJsonData();
            log(3, c("put(", field, ")", "\u21D0", "[", value, "\u2192", o, "]"));
            l.logFile(jsonData.toString(), "data", "json");
        }
    }

    /**
     * Read contents of the element defined by string locator
     *
     * @param locator string locator of an element
     * @return result of .getText() for this element
     */
    @SuppressWarnings({"ChainedMethodCall", "DuplicateStringLiteralInspection"})
    public String read(String locator) {
        String text = d.findElement(by(locator)).getText();
        l.log(c("Reading from '", locator, "' > '", text, "'"));
        return text;
    }

    /**
     * Read contents of the WebElement
     *
     * @param e WebElement to read text from
     * @return text
     */
    @SuppressWarnings("unused")
    public String read(WebElement e) {
        return read(e, true);
    }

    /**
     * Read contents of given WebElement with optional logging of geometry
     * information
     *
     * @param e        element to read from
     * @param geometry whether to post a log message or not
     * @return result of .getText() for this element
     */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public String read(WebElement e, boolean geometry) {
        l.log(c("Reading from '", describe(e, geometry), "'"));
        return e.getText();
    }

    /**
     * Read value attribute of the element defined by String locator
     *
     * @param lc string locator of an element
     * @return contents of value attribute of the element
     */
    @SuppressWarnings({"unused", "ChainedMethodCall", "DuplicateStringLiteralInspection"})
    public String readInput(String lc) {
        l.log(c("Reading from '", lc, "'"));
        return d.findElement(by(lc)).getAttribute("value");
    }

    /**
     * Read value attribute of the element
     *
     * @param e an element
     * @return contents of value attribute of the element
     */
    @SuppressWarnings({"unused", "DuplicateStringLiteralInspection"})
    public String readInput(WebElement e) {
        l.log(c("Reading from '", describe(e), "'"));
        return e.getAttribute("value");
    }

    /**
     * Shortcut for removing a data from the common test data container
     *
     * @param field of test data to be updated
     */
    public void remove(Object field) {
        log(3, c("remove(", field, ")"));
        x.dataRemove(field);
    }

    /**
     * Restart application in case of Android testing
     *
     * @param pause to make before activation of the app
     */
    @SuppressWarnings({"FeatureEnvy", "OverlyBroadCatchBlock"})
    public void restartApp(long pause) {
        if (x.in(ANDROID)) {
            l.log(c("Terminating ", x._bundleId()));
            ((InteractsWithApps) d).terminateApp(x._bundleId());
            try {
                l.log(c("Activating ", x._bundleId()));
                sleep(pause);
                ((InteractsWithApps) d).activateApp(x._bundleId());
                sleep(pause); // TODO: investigate the reason of needing two attempts
                ((InteractsWithApps) d).activateApp(x._bundleId());
            } catch (Exception e) {
                //
            }
        } else l.logProblem(Severity.S3, "restartApp() only applicable for Android testing");
    }

    /**
     * Produce BASE64 encoded string screenshot of an element
     *
     * @param lc string locator of requested element
     * @return BASE64 encoded string screenshot
     */
    @SuppressWarnings({"unused", "ChainedMethodCall"})
    public String screenShot(String lc) {
        return d.findElement(by(lc)).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Scroll forward
     *
     * @param steps to scroll forward
     * @return element
     */
    @SuppressWarnings({"unused", "ReturnOfNull", "AutoBoxing", "MethodWithMultipleReturnPoints", "DuplicateStringLiteralInspection"})
    public WebElement scrollForward(int steps) {
        // TODO: [framework] investigate reason of unpredictable scrolling behavior
        if (x.in(ANDROID)) {
            l.log(c("Scrolling ", steps, " step(s) forward")); //NON-NLS
            return d.findElement(AppiumBy.androidUIAutomator(c("new UiScrollable(new UiSelector().scrollable(true))", ".scrollForward(", steps, ")")));
        }
        l.logProblem(Severity.S3, "scrollForward() only applicable for Android testing");
        return null;
    }

    /**
     * @param text to scroll to
     */
    public void scrollIntoView(String text) {
        scrollIntoView(text, true);
    }

    /**
     * Scroll an element with given text into view
     *
     * @param text to scroll to
     * @param lazy whether to check for the presence of a text on screen before actual scrolling attempt
     */
    @SuppressWarnings({"FeatureEnvy", "ChainedMethodCall", "MethodWithMultipleReturnPoints", "DuplicateStringLiteralInspection", "CallToSuspiciousStringMethod"})
    public void scrollIntoView(String text, boolean lazy) {
        if (x.in(ANDROID)) {
            if (lazy) {
                l.log(c("Verifying that text '", text, "' is not already on-screen")); //NON-NLS
                List<WebElement> all = findAll(text(text));
                for (WebElement a : all) {
                    if (a.getText().equals(text)) {
                        l.log(c("Text '", text, "' is already on-screen"));
                        return;
                    }
                }
            }
            l.log(c("Scrolling to '", text, "' text"));
            d.findElement(AppiumBy.androidUIAutomator(c("new UiScrollable(new UiSelector().scrollable(true))", ".scrollIntoView(new UiSelector().textContains(\"", text, "\"))")));
            return;
        }
        l.logProblem(Severity.S3, "scrollIntoView() only applicable for Android testing");
    }

    /**
     * Add a section title to the test log
     *
     * @param s string to be added as section title
     */
    public void section(String s) {
        l.logln();
        l.log(0, colorize(c("= ", s, " ="), Ansi.BLUE_BOLD_BRIGHT));
    }

    /**
     * Send keys as Action
     *
     * @param keys CharSequence to send
     */
    @SuppressWarnings("unused")
    public void sendKeys(CharSequence keys) {
        sendKeys(keys, 1);
    }

    /**
     * Send keys as Action defined number of times
     *
     * @param keys CharSequence to send
     * @param i    times to repeat
     */
    @SuppressWarnings({"AssignmentToMethodParameter", "ValueOfIncrementOrDecrementUsed", "ChainedMethodCall", "ObjectAllocationInLoop"})
    public void sendKeys(CharSequence keys, int i) {
        do new Actions(x.d).sendKeys(keys).build().perform(); while (0 < --i);
    }

    /**
     * Send keys to the element defined by string locator
     *
     * @param lc         string locator of target input element
     * @param keysToSend CharSequence to send
     */
    @SuppressWarnings({"unused", "ChainedMethodCall"})
    public void sendKeys(String lc, CharSequence keysToSend) {
        find(lc).sendKeys(keysToSend);
    }

    private String shotLocation(String... v) {
        return Output.name(x, RUN_SNAPSHOT_FILENAME, v);
    }

    /**
     * Sleep during defined amount of milliseconds
     *
     * @param millis amount of milliseconds to wait
     */
    public void sleep(long millis) {
        sleep(millis, null);
    }

    /**
     * Sleep during defined amount of milliseconds with optional bypassing of
     * logging
     *
     * @param millis amount of milliseconds to wait
     * @param silent whether to bypass logging
     */
    public void sleep(long millis, boolean silent) {
        sleep(millis, null, silent);
    }

    /**
     * Sleep during defined amount of milliseconds with optional custom message
     *
     * @param millis amount of milliseconds to wait
     * @param string custom message to post into the execution log
     */
    @SuppressWarnings({"AutoBoxing", "QuestionableName", "ImplicitNumericConversion"})
    public void sleep(long millis, String string) {
        if (SILENT_SLEEPING_THRESHOLD > millis)
            l.log(0, colorize(c("[WARNING]: ", "Sleeping for less then ", SILENT_SLEEPING_THRESHOLD, " ms is better to be performed in silent mode")));
        sleep(millis, string, false);
    }

    /**
     * Sleep during defined amount of milliseconds with optional custom message and
     * optional bypassing of logging
     *
     * @param millis amount of milliseconds to pause for
     * @param string text to display in the execution log, can be set to null for default
     *               "Sleeping X ms" message
     * @param silent whether to bypass output to test log. Useful for short delays
     */
    @SuppressWarnings({"AutoBoxing", "QuestionableName", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    public void sleep(long millis, String string, boolean silent) {
        try {
            if (!silent) l.log(null == string ? c("Sleeping ", millis, " ms") : string);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted sleep()");
        }
    }

    /**
     * Switch to other WebDriver window
     *
     * @param w window handle to switch to
     */
    @SuppressWarnings({"unused", "ChainedMethodCall"})
    public void switchTo(String w) {
        l.log(c("Switching to '", w, "'"));
        d.switchTo().window(w);
    }

    /**
     * Call {@link Common#typeInto(String, Object, double, boolean)} with
     * {@code pasteProbability} set to 0 and {@code secret} set to
     * false
     *
     * @param lc      single String locator. To facilitate multilevel search, use
     *                {@code typeInto(find(str1, str2, ...), ...);}
     * @param content Content to type, Object.toString() in used to get string
     *                representation
     * @return true of direct copy was used, false otherwise
     */
    @SuppressWarnings({"unused", "BooleanMethodNameMustStartWithQuestion", "ImplicitNumericConversion"})
    public boolean typeInto(String lc, Object content) {
        return typeInto(lc, content, 0, false);
    }

    /**
     * Call {@link Common#typeInto(String, Object, double, boolean)} with
     * {@code pasteProbability} set to 0
     *
     * @param lc      single String locator. To facilitate multilevel search, use
     *                {@code typeInto(find(str1, str2, ...), ...);}
     * @param content Content to type, Object.toString() in used to get string
     *                representation
     * @param secret  whether to screen content in the log by '•' symbols (as in
     *                passwords)
     * @return true of direct copy was used, false otherwise
     */
    @SuppressWarnings({"unused", "BooleanMethodNameMustStartWithQuestion", "ImplicitNumericConversion"})
    public boolean typeInto(String lc, Object content, boolean secret) {
        return typeInto(lc, content, 0, secret);
    }

    /**
     * Call {@link Common#typeInto(String, Object, double, boolean)} with
     * {@code secret} set to false
     *
     * @param lc      single String locator. To facilitate multilevel search, use
     *                {@code typeInto(find(str1, str2, ...), ...);}
     * @param content Content to type, Object.toString() in used to get string
     *                representation
     * @param pp      Probability in range [0.0 - 1.0] of using direct copy to simulate
     *                pasting from clipboard;
     * @return true of direct copy was used, false otherwise
     */
    @SuppressWarnings({"UnusedReturnValue", "BooleanMethodNameMustStartWithQuestion"})
    public boolean typeInto(String lc, Object content, double pp) {
        return typeInto(lc, content, pp, false);
    }

    /**
     * Call {@link Common#typeInto(WebElement, Object, double, boolean)} with
     * WebElement defined by String locator
     *
     * @param lc      single String locator. To facilitate multilevel search, use
     *                {@code typeInto(find(str1, str2, ...), ...);}
     * @param content Content to type, Object.toString() in used to get string
     *                representation
     * @param pp      Probability in range [0.0 - 1.0] of using direct copy to
     *                simulate pasting from clipboard;
     * @param secret  whether to screen content in the log by '•' symbols (as in
     *                passwords)
     * @return true of direct copy was used, false otherwise
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean typeInto(String lc, Object content, double pp, boolean secret) {
        WebElement e = find(lc);
        return typeInto(e, content, pp, secret);
    }

    /**
     * Call {@link Common#typeInto(WebElement, Object, double, boolean)} with
     * {@code pasteProbability} set to 0 and {@code secret} set to
     * false
     *
     * @param e       WebElement to type into
     * @param content Content to type, Object.toString() in used to get string
     *                representation
     * @return true of direct copy was used, false otherwise
     */
    @SuppressWarnings({"unused", "BooleanMethodNameMustStartWithQuestion", "ImplicitNumericConversion"})
    public boolean typeInto(WebElement e, Object content) {
        return typeInto(e, content, 0, false);
    }

    /**
     * Call {@link Common#typeInto(WebElement, Object, double, boolean)} with
     * {@code pasteProbability} set to 0
     *
     * @param e       WebElement to type into
     * @param content Content to type, Object.toString() in used to get string
     *                representation
     * @param secret  whether to screen content in the log by '•' symbols (as in
     *                passwords)
     * @return true of direct copy was used, false otherwise
     */
    @SuppressWarnings({"unused", "BooleanMethodNameMustStartWithQuestion", "ImplicitNumericConversion"})
    public boolean typeInto(WebElement e, Object content, boolean secret) {
        return typeInto(e, content, 0, secret);
    }

    /**
     * Call {@link Common#typeInto(WebElement, Object, double, boolean)} with
     * {@code secret} set to false
     *
     * @param e       WebElement to type into
     * @param content Content to type, Object.toString() in used to get string
     *                representation
     * @param pp      Probability in range [0.0 - 1.0] of using direct copy to simulate
     *                pasting from clipboard;
     * @return true of direct copy was used, false otherwise
     */
    @SuppressWarnings({"unused", "BooleanMethodNameMustStartWithQuestion"})
    public boolean typeInto(WebElement e, Object content, double pp) {
        return typeInto(e, content, pp, false);
    }

    /**
     * Simulate typing the value into the WebElement
     *
     * @param e       WebElement to type into
     * @param content Content to type, Object.toString() in used to get string
     *                representation
     * @param pp      Probability in range [0.0 - 1.0] of using direct copy to
     *                simulate pasting from clipboard;
     * @param secret  whether to screen content in the log by '•' symbols (as in
     *                passwords)
     * @return true of direct copy was used, false otherwise
     */
    @SuppressWarnings({"MagicNumber", "ChainedMethodCall", "BooleanMethodNameMustStartWithQuestion", "MethodWithMultipleReturnPoints", "OverlyBroadCatchBlock"})
    public boolean typeInto(WebElement e, Object content, double pp, boolean secret) {
        CharSequence symbols = String.valueOf(content);
        l.log(c("Typing '", (secret ? "•".repeat(symbols.length()) : content), "' into '", describe(e), "'"));
        try {
            if (isTrue(pp)) {
                // simulation of pasting from clipboard by instant addition of all content
                e.sendKeys(symbols);
                return true;
            }
            char[] chars = symbols.toString().toCharArray();
            for (char ch : chars) {
                sleep(integer(0, 50).longValue(), true); // silent sleep
                e.sendKeys(String.valueOf(ch));
            }
            return false;
        } catch (Exception ex) {
            logProblem(Severity.S2, "Exception " + ex.getClass().getName() + " caught while trying to type '" + (secret ? "•".repeat(symbols.length()) : content) + "' into " + describe(e) + ", attempting workaround ...");
            Actions action = new Actions(x.d);
            action.moveToElement(e).click().sendKeys(String.valueOf(content)).build().perform();
            return true;
        }
    }

    /**
     * @param uploader locator of upload input
     * @param name     filename of file to upload
     */
    @SuppressWarnings({"unused", "ChainedMethodCall", "AccessOfSystemProperties", "HardcodedFileSeparator"})
    public void upload(String uploader, String name) {
        l.log(c("Uploading '", name, "' through '", uploader, "'"));
        find(false, false, uploader).sendKeys(System.getProperty("user.dir") + "/" + RESOURCES + name);
    }

    /**
     * Verify that defined element is enabled and then click on it
     *
     * @param lc string locator of an element to be waited for
     */
    @SuppressWarnings("unused")
    public void verifyThenClickOn(String lc) {
        WebElement e = find(lc);
        Assertions.assertTrue(e.isEnabled(), "Element not enabled");
        clickOn(lc);
    }

    /**
     * Wait for appearance of the defined element
     *
     * @param lc string locator of an element to be waited for
     */
    public void wait(String lc) {
        waitVisibilityOf(lc);
    }

    /**
     * Wait for appearance of the defined element
     *
     * @param lc string locator of an element to be waited for
     */
    public void wait(String[] lc) {
        waitVisibilityOf(lc);
    }

    /**
     * Wait for an element to be clickable
     *
     * @param lc string locator of an element expected to be clickable
     */
    @SuppressWarnings("unused")
    public void waitClickable(String lc) {
        l.log(c("Waiting for clickability of '", lc, "'")); //NON-NLS
        w.until(elementToBeClickable(by(lc)));
    }

    /**
     * Wait for an element to be not visible
     *
     * @param lc string locator of an element expected to be invisible
     */
    public void waitInvisibility(String lc) {
        l.log(c("Waiting for invisibility of '", lc, "'"));
        w.until(invisibilityOfElementLocated(by(lc)));
    }

    /**
     * Wait for an element to be selected
     *
     * @param lc string locator of an element expected to be selected
     */
    @SuppressWarnings({"unused", "DuplicateStringLiteralInspection"})
    public void waitSelected(String lc) {
        l.log(c("Waiting for '", lc, "' to be selected"));
        w.until(visibilityOfElementLocated(by(lc)));
    }

    /**
     * Wait for appearance of the defined element and the click on it
     *
     * @param lc string locator of an element to be waited for
     * @param x  coordinate offset
     * @param y  coordinate offset
     */
    @SuppressWarnings("unused")
    public void waitThenClickOn(double x, double y, String lc) {
        wait(lc);
        clickOn(x, y, lc);
    }

    /**
     * Wait for appearance of the defined element and the click on it
     *
     * @param lc string locator of an element to be waited for
     */
    public void waitThenClickOn(String lc) {
        wait(lc);
        clickOn(lc);
    }

    /**
     * Wait for appearance of the defined element and the click on it
     *
     * @param lc string locator of an element to be waited for
     */
    @SuppressWarnings("unused")
    public void waitThenClickOn(String[] lc) {
        wait(lc);
        clickOn(lc);
    }

    /**
     * Wait for appearance of the defined element and return reference to it
     *
     * @param lc string locator of an element to be waited for
     * @return element
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public WebElement waitThenFind(String[] lc) {
        wait(lc);
        return find(lc);
    }

    /**
     * Wait for a text value to differ from defined.
     *
     * @param lc string locator of an element
     * @param s  expected value
     */
    @SuppressWarnings({"unused", "DuplicateStringLiteralInspection"})
    public void waitValue(String lc, String s) {
        l.log(c(c("Waiting for '", lc, "' to have value '", s, "'")));
        w.until(textToBe(by(lc), s));
    }

    /**
     * Wait for a text value of an element to change from defined one.
     *
     * @param lc string locator of an element
     * @param s  the value to change from
     * @return value after the detected change
     */
    @SuppressWarnings({"unused", "DuplicateStringLiteralInspection"})
    public String waitValueNot(String lc, String s) {
        l.log(c("Waiting while '", lc, "' still have value '", s, "'"));
        w.until(not(textToBe(by(lc), s)));
        return read(lc);
    }

    /**
     * Wait for a non-empty text value
     *
     * @param lc string locator of an element
     * @return value after the detected change
     */
    @SuppressWarnings({"unused", "MagicNumber", "ImplicitNumericConversion", "DuplicateStringLiteralInspection"})
    public String waitValueNotEmpty(String lc) {
        // TODO: [framework] this routine seems to be working not as expected
        l.log(c("Waiting while '", lc, "' is still empty"));
        w.until(not(textToBe(by(lc), "")));
        sleep(333);
        String t = read(lc);
        l.log(c("The text of '", lc, "' is now '", t, "'"));
        return t;
    }

    /**
     * Wait for appearance of the defined element
     *
     * @param lc string locator of an element to wait for
     */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void waitVisibilityOf(String lc) {
        l.log(c("Waiting for visibility of '", lc, "'"));
        w.until(visibilityOfElementLocated(by(lc)));
    }

    /**
     * Wait for appearance of the defined element
     *
     * @param lc string locator of an element wait for
     */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public void waitVisibilityOf(String[] lc) {
        l.log(c("Waiting for visibility of '", Arrays.toString(lc), "'"));
        w.until(visibilityOfElementLocated(by(lc)));
    }

}
