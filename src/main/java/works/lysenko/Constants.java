package works.lysenko;

public class Constants {

	public final static String DEFAULT_SHOTS_LOCATION = "target/screenshots/";
	public final static String DEFAULT_RUNS_LOCATION = "target/runs/";
	public final static String DEFAULT_PROPERTIES_LOCATION = "src/test/resources/";
	public static final String GENERATED_CONFIG_FILE = "target/generated-sources/generated.properties";

	public static final String DEFAULT_CYCLES_COUNT = "0";
	public static final String DEFAULT_SCENARIO_WEIGHT = "0.0";
	public static final int DEFAULT_SUFFICIENCY_RETRIES = 5; // 7, 11, 13, 17

	public static final int SILENT_SLEEPING_TRESHHOLD = 100;

	public static final String RUN_LOG_FILENAME = "%1$s.run.log";
	public static final String RUN_JSON_FILENAME = "%1$s.run.json";

	public final static String _FIRST_SPAN = "(.//span)[1]";
	public final static String _SECOND_SPAN = "(.//span)[2]";
	public final static String _FIRST_BUTTON = "(.//button)[1]";
	public final static String _SECOND_BUTTON = "(.//button)[2]";
	public static final String _FIRST_DIV = "(.//div)[1]";
	public static final String _SECOND_DIV = "(.//div)[2]";
	public static final String _ELEMENT_WITH_TEXT = "//%1$s[text()='%2$s']";
	public static final String _CHILD_WITH_TEXT = ".//%1$s[text()='%2$s']";

	public static final String FULLWIDTH_QUOTATION_MARK = "＂";

}
