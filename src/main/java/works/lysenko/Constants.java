package works.lysenko;

import org.jfree.svg.SVGGraphics2D;
import works.lysenko.enums.Platform;

import static works.lysenko.Common.c;

@SuppressWarnings({"unused", "StaticMethodOnlyUsedInOneClass", "ClassHasNoToStringMethod", "PublicConstructor", "ClassWithoutLogger", "ClassWithoutConstructor", "ClassWithTooManyTransitiveDependents", "ClassWithTooManyDependents", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class Constants {

    public static final int SVG_WIDTH = 3440;
    public static final int SVG_HEIGHT = 1440;
    public static final String FINALS_NO_IMPLEMENTATION = "finals(): no implementation";
    public static final String ACTION_NO_IMPLEMENTATION = "action(): no implementation";
    public static final String BOT = "Bot";
    public static final String HALT_ISN_T_SUPPOSED_TO_BE_CALLED_FOR_LEAF_SCENARIO = "halt() isn't supposed to be called for Leaf Scenario";
    public static final String SWITCHING_TO_NEXT_ARTICLE = "Switching to next Article";
    public static final String __BUT_ACTUAL_IS__ = "' but actual is '";
    public static final int EXCEPTION_RETRIES = 9;
    public static final String DEFAULT_TEST = "";
    public static final String DEFAULT_DOMAIN = "";
    public static final String DEFAULT_DEVICE = "";
    public static final String DEFAULT_PLATFORM = Platform.CHROME.title();
    public static final String LF = System.lineSeparator();
    public static final String TEST = ".test";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String GENERATED_CONFIG_FILE = c("target/generated-sources/generated", TEST); //NON-NLS
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String STORED_PARAMETERS_FILE = "etc/parameters";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String STORED_PLATFORMS_FILE = "etc/platforms";
    // Use these platforms instead of autodetect. Set to null for detection reactivation
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String FORCED_PLATFORMS = null;
    // public static final String DEVICES_FILE = "etc/devices"; // TODO: uncomment during implementation of virtual devices handling
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String RESOURCES = "src/test/resources/";
    public static final String KNOWN_ISSUES = c(RESOURCES, "_knownIssues.properties");
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String TESTS = c(RESOURCES, "tests/");
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String RUNS = "target/runs/";
    public static final String CONFIGURATION_DOWNSTREAM = "include_downstream";
    public static final String CONFIGURATION_BUNDLE_ID = "bundleId";
    public static final String CONFIGURATION_CONJOINT = "conjoint";
    public static final String CONFIGURATION_UPSTREAM = "include_upstream";
    public static final String CONFIGURATION_PERSIST = "persist";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String CONFIGURATION_CYCLES = "cycles";
    public static final String CONFIGURATION_ADEBUG = "adebug";
    public static final String CONFIGURATION_DEBUG = "debug";
    public static final String CONFIGURATION_EWAIT = "ewait";
    public static final String CONFIGURATION_IWAIT = "iwait";
    public static final String CONFIGURATION_ROOT = "root";
    public static final String CONFIGURATION_APP = "app";
    public static final String CONFIGURATION_DIR = "dir";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String DEFAULT_DOWNSTREAM = "false";
    public static final String DEFAULT_BUNDLE_ID = null;
    public static final String DEFAULT_CONJOINT = "true";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String DEFAULT_UPSTREAM = "false";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String DEFAULT_PERSIST = "false";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String DEFAULT_ADEBUG = "false";
    public static final String DEFAULT_CYCLES = "1";
    public static final String DEFAULT_WEIGHT = "0.0";
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public static final String DEFAULT_DEBUG = "false";
    public static final String DEFAULT_EWAIT = "30";
    public static final String DEFAULT_IWAIT = "1";
    public static final String DEFAULT_ROOT = "";
    public static final String DEFAULT_APP = "";
    public static final String DEFAULT_DIR = "";
    public static final int DEFAULT_SUFFICIENCY_RETRIES = 11; // 13, 17
    public static final int SILENT_SLEEPING_THRESHOLD = 10;
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String RUN_JSON_FILENAME = "%1$s/%1$s.run.json";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String RUN_LOG_FILENAME = "%1$s/%1$s.run.log";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String RUN_SVG_FILENAME = "%1$s/%1$s.run.svg";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String RUN_SNAPSHOT_FILENAME = "%1$s/snapshots/%1$s.%2$s.%3$s";
    public static final String JSON_FILENAME = "%1$s.%2$s.json";
    public static final String FILENAME = "%1$s.%2$s.%3$s";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _ELEMENT_WITH_TEXT = "//%1$s[text()='%2$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _CHILD_WITH_TEXT = ".//%1$s[text()='%2$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _SECOND_BUTTON = "(.//button)[2]";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _FIRST_BUTTON = "(.//button)[1]";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _SECOND_SPAN = "(.//span)[2]";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _FIRST_SPAN = "(.//span)[1]";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _SECOND_DIV = "(.//div)[2]";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _FIRST_DIV = "(.//div)[1]";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _BUTTON_TEXT = "//button[text()='%1$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _LABEL_TEXT = "//label[text()='%1$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _SPAN_TEXT = "//span[text()='%1$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _DIV_TEXT = "//div[text()='%1$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _H1_TEXT = "//h1[text()='%1$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _H2_TEXT = "//h2[text()='%1$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _LI_TEXT = "//li[text()='%1$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _A_TEXT = "//a[text()='%1$s']";
    @SuppressWarnings("HardcodedFileSeparator")
    public static final String _P_TEXT = "//p[text()='%1$s']";
    public static final String CHECKED = "checked";
    @SuppressWarnings({"MagicNumber", "ImplicitNumericConversion", "PackageVisibleField"})
    SVGGraphics2D g = new SVGGraphics2D(2560, 1440);
}
