package works.lysenko;

@SuppressWarnings("javadoc")
public class Constants {

	public static final int EXCEPTION_RETRIES = 5;

	public final static String LF = System.lineSeparator();
	
	public final static String RESOURCES = "src/test/resources/";
	public final static String SCREENSHOTS = "target/screenshots/";
	public final static String RUNS = "target/runs/";
	public static final String GENERATED_CONFIG_FILE = "target/generated-sources/generated.properties";

	public static final String CONFIGURATION_DOWNSTREAM = "include_downstream";
	public static final String CONFIGURATION_CONJOINT = "conjoint";
	public static final String CONFIGURATION_UPSTREAM = "include_upstream";
	public static final String CONFIGURATION_CYCLES = "cycles";
	public static final String CONFIGURATION_DEBUG = "debug";
	public static final String CONFIGURATION_ROOT = "root";

	public static final String DEFAULT_SCENARIO_WEIGHT = "0.0";
	public static final String DEFAULT_DOWNSTREAM = "false";
	public static final String DEFAULT_CONJOINT = "true";
	public static final String DEFAULT_UPSTREAM = "false";
	public static final String DEFAULT_CYCLES = "1";
	public static final String DEFAULT_DEBUG = "false";
	public static final String DEFAULT_ROOT = "__undefined__";
	
	public static final int DEFAULT_SUFFICIENCY_RETRIES = 11; // 13, 17

	public static final int SILENT_SLEEPING_TRESHHOLD = 10;

	public static final String RUN_JSON_FILENAME = "%1$s.run.json";
	public static final String RUN_LOG_FILENAME = "%1$s.run.log";
	public static final String RUN_SVG_FILENAME = "%1$s.run.svg";
	public static final String JSON_FILENAME = "%1$s.%2$s.json";
	public static final String FILENAME = "%1$s.%2$s.%3$s";

	public static final String _ELEMENT_WITH_TEXT = "//%1$s[text()='%2$s']";
	public static final String _CHILD_WITH_TEXT = ".//%1$s[text()='%2$s']";
	public final static String _SECOND_BUTTON = "(.//button)[2]";
	public final static String _FIRST_BUTTON = "(.//button)[1]";
	public final static String _SECOND_SPAN = "(.//span)[2]";
	public final static String _FIRST_SPAN = "(.//span)[1]";
	public static final String _SECOND_DIV = "(.//div)[2]";
	public static final String _FIRST_DIV = "(.//div)[1]";
	public static final String _SPAN_TEXT = "//span[text()='%1$s']";
	public static final String _DIV_TEXT = "//div[text()='%1$s']";
	
	public static final String NODE_SCENARIO_MARKER = "▷";
	public static final String LEAF_SCENARIO_MARKER = "◆";
	public static final String MONO_SCENARIO_MARKER = "◼";
}
