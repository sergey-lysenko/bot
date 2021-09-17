package works.lysenko;

public class Constants {

	public final static String DEFAULT_SHOTS_LOCATION = "target/screenshots/";
	public final static String DEFAULT_RUNS_LOCATION = "target/runs/";
	public final static String DEFAULT_PROPERTIES_LOCATION = "src/test/resources/";
	public static final String GENERATED_CONFIG_FILE = "target/generated-sources/generated.properties";

	public static final String CONFIGURATION_CYCLES = "cycles";
	public static final String CONFIGURATION_DEBUG = "debug";
	public static final String CONFIGURATION_CONJOINT = "conjoint";
	public static final String CONFIGURATION_PERVASIVE = "include_upstream";
	public static final String CONFIGURATION_PERMEATIVE = "include_downstream";

	public static final String DEFAULT_CYCLES = "1";
	public static final String DEFAULT_SCENARIO_WEIGHT = "0.0";
	public static final String DEFAULT_PERVASIVE = "false";
	public static final String DEFAULT_PERMEATIVE = "false";
	public static final String DEFAULT_DEBUG = "false";
	public static final String DEFAULT_CONJOINT = "true";
	public static final int DEFAULT_SUFFICIENCY_RETRIES = 11; // 13, 17

	public static final int SILENT_SLEEPING_TRESHHOLD = 10;

	public static final String RUN_LOG_FILENAME = "%1$s.run.log";
	public static final String RUN_JSON_FILENAME = "%1$s.run.json";
	public static final String RUN_SVG_FILENAME = "%1$s.run.svg";

	public final static String _FIRST_SPAN = "(.//span)[1]";
	public final static String _SECOND_SPAN = "(.//span)[2]";
	public final static String _FIRST_BUTTON = "(.//button)[1]";
	public final static String _SECOND_BUTTON = "(.//button)[2]";
	public static final String _FIRST_DIV = "(.//div)[1]";
	public static final String _SECOND_DIV = "(.//div)[2]";
	public static final String _ELEMENT_WITH_TEXT = "//%1$s[text()='%2$s']";
	public static final String _CHILD_WITH_TEXT = ".//%1$s[text()='%2$s']";

	public static final String FULLWIDTH_QUOTATION_MARK = "＂";

	public static final String NODE_SCENARIO_MARKER = "▷";
	public static final String LEAF_SCENARIO_MARKER = "◆";
	public static final String MONO_SCENARIO_MARKER = "◼";
}
