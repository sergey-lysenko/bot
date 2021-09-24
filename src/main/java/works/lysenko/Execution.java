package works.lysenko;

import static works.lysenko.Constants.CONFIGURATION_CONJOINT;
import static works.lysenko.Constants.CONFIGURATION_CYCLES;
import static works.lysenko.Constants.CONFIGURATION_DEBUG;
import static works.lysenko.Constants.CONFIGURATION_DOWNSTREAM;
import static works.lysenko.Constants.CONFIGURATION_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_CONJOINT;
import static works.lysenko.Constants.DEFAULT_CYCLES;
import static works.lysenko.Constants.DEFAULT_DEBUG;
import static works.lysenko.Constants.DEFAULT_DOWNSTREAM;
import static works.lysenko.Constants.DEFAULT_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_PROPERTIES_LOCATION;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import works.lysenko.scenarios.AbstractScenario;
import works.lysenko.scenarios.Scenario;
import works.lysenko.utils.Browser;
import works.lysenko.utils.Severity;
import works.lysenko.utils.Stopwatch;
import works.lysenko.utils.WebDrivers;

/**
 * This class represent single bot execution information
 * 
 * @author Sergii Lysenko
 *
 */
public class Execution extends Common {

	/**
	 * @return true if current execution is performed in an environment with CI
	 *         variable set to any value. This is common way of determining
	 *         execution inside GitHUB actions and other pipelines
	 */
	public static Boolean insideCI() {
		return System.getenv().containsKey("CI");
	}

	/**
	 * @return true if current execution is performed in a Docker container
	 */
	public static Boolean insideDocker() {
		try {
			return Files.readString(Paths.get("/proc/1/cgroup")).contains("/docker");
		} catch (NoSuchFileException e) {
			// most probably that is caused by being executed on Windows;
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Stack of currently executed scenarios
	 */
	public Stack<AbstractScenario> current = new Stack<AbstractScenario>();
	/**
	 * Linked Cycles object
	 */
	public Cycles cycles = null;

	/**
	 * This information is shared between Scenarios
	 */
	public Integer minDepth = null; // TODO: rework this implementation to avoid usage of public access to this
									// field from single external code location
	protected Stopwatch t;

	/**
	 * Cached GsonBuilder object to share between calls
	 */
	public GsonBuilder gsonBuilder = new GsonBuilder();
	private Properties prop;
	private Properties know;
	/**
	 * Shared data storage (for prerequisites management)
	 */
	public Properties data;

	/**
	 * Name of the test execution for logging purposes
	 */
	public String name;
	/**
	 * Target domain name of current execution
	 */
	public String domain;

	/**
	 * @param implicitWait
	 * @param explicitWait
	 * @param logsToRead
	 * @param name
	 */
	public Execution(int implicitWait, int explicitWait, Set<String> logsToRead, String name) {
		this(implicitWait, explicitWait, logsToRead, name, null);
	}

	/**
	 * @param implicitWait
	 * @param explicitWait
	 * @param logsToRead
	 * @param name
	 * @param domain
	 */
	public Execution(int implicitWait, int explicitWait, Set<String> logsToRead, String name, String domain) {
		super();
		this.x = this;

		// Bot components
		t = new Stopwatch();
		o = new Output(this);
		r = new Results(this);
		l = new Logger(this, logsToRead);

		// Web driver components
		d = WebDrivers.get(Browser.CHROME, false);
		d.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		w = new WebDriverWait(d, Duration.ofSeconds(explicitWait));

		// Properties
		data = new Properties();
		prop = readProperties(name);
		know = readProperties("_knownIssues");

		// Information
		this.name = name;
		this.domain = domain;
	}

	/**
	 * @return whether current test execution have "conjoint" mode active
	 */
	public boolean _conjoint() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_CONJOINT, DEFAULT_CONJOINT));
	}

	/**
	 * @return configured number of cycles to be executed
	 */
	public int _cycles() {
		return Integer.valueOf(prop.getProperty("_" + CONFIGURATION_CYCLES, DEFAULT_CYCLES));
	}

	/**
	 * @return true if debug mode is configured in current test run
	 */
	public boolean _debug() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_DEBUG, DEFAULT_DEBUG));
	}

	/**
	 * @return true if downstream weight distribution is activated in configuration
	 */
	public boolean _downstream() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_DOWNSTREAM, DEFAULT_DOWNSTREAM));
	}

	/**
	 * @return true if upstream weight distribution is activated in configuration
	 */
	public boolean _upstream() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_UPSTREAM, DEFAULT_UPSTREAM));
	}

	/**
	 * Performs following routines before ending test execution: - stats() - write
	 * execution summary to the console - jsonStats() - write test execution summary
	 * in JSON format (for following import into ELK) - conditionalClose() - close
	 * browser window if not in CI
	 */
	public void complete() {
		o.stats();
		o.jsonStats();
		o.svgStats();
		conditionalClose();
	}

	/**
	 * Close browser window in case of execution in CI, but keep it open in case of
	 * local execution, which come in handy for development of scenarios
	 */
	public void conditionalClose() {
		try {
			l.logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (insideDocker() || insideCI())
			d.quit();
	}

	/**
	 * @return number of current cycle
	 */
	public int currentCycle() {
		return _cycles() - cycles.cyclesToDo;
	}

	/**
	 * @return logical depth of current scenario
	 */
	public int currentDepth() {
		return (null == x.currentScenario()) ? 0 : x.currentScenario().depth();
	}

	/**
	 * @return link to currently executed Scenario
	 */
	public AbstractScenario currentScenario() {
		return current.empty() ? null : current.peek();
	}

	/**
	 * @param p string to search Known Issues
	 * @return Set of KnownIssues for given query
	 */
	public Set<String> getKnownIssue(String p) {
		// TODO: either add ability to have several known issues for one query, or
		// change this routine to return single known issue
		HashSet<String> knownIssues = new HashSet<String>();
		if (null != know)
			know.forEach((k, v) -> {
				if (p.contains((String) v)) {
					knownIssues.add((String) k);
				}
			});
		return knownIssues;
	}

	/**
	 * @return Gson object built by cached GsonBuilder
	 */
	public Gson gson() {
		return gsonBuilder.create();
	}

	/**
	 * Calculate downstream weights of given scenario from current execution parameters
	 * 
	 * @param s Scenario to calculate downstream weights
	 * @return cumulative downstream weight 
	 */
	public Double downstream(Scenario s) {
		// TODO: move this to Scenario class(es) ?
		for (Entry<Object, Object> p : prop.entrySet()) {
			String key = (String) p.getKey();
			if (!(key.charAt(0) == '_')) {
				if (s.getClass().getName().toLowerCase().contains(key.toLowerCase())) {
					Double v = Double.valueOf((String) p.getValue());
					s.downstream(v);
					return v;
				}
			}
		}
		return 0.0;
	}

	/**
	 * @param n name of run property
	 * @param d default value of run property
	 * @return value of requested property
	 */
	public String prop(String n, String d) {
		return prop.getProperty(n, d);
	}

	/**
	 * @return true is properties is empty
	 */
	public boolean propEmpty() {
		return prop.isEmpty();
	}

	private Properties readProperties(String name) {
		Properties prop = new Properties();
		String propertiesFile = DEFAULT_PROPERTIES_LOCATION + name + ".properties";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(propertiesFile);
		} catch (FileNotFoundException e) {
			l.logProblem(Severity.S1, "Requested properties file '" + propertiesFile + "' not found");
		}
		try {
			if (null != fis)
				prop.load(fis);
			else
				l.logProblem(Severity.S2, "Unable to read properties from '" + propertiesFile + "'");
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to load requested properties file");
		}
		return prop;
	}

	/**
	 * @return amount of milliseconds since start of test execution
	 */
	public Long timer() {
		return t.millis();
	}
}
