package works.lysenko;

import static org.openqa.selenium.logging.LogType.BROWSER;
import static org.openqa.selenium.logging.LogType.CLIENT;
import static org.openqa.selenium.logging.LogType.DRIVER;
import static org.openqa.selenium.logging.LogType.PERFORMANCE;
import static org.openqa.selenium.logging.LogType.PROFILER;
import static org.openqa.selenium.logging.LogType.SERVER;
import static works.lysenko.Constants.CONFIGURATION_ADEBUG;
import static works.lysenko.Constants.CONFIGURATION_APP;
import static works.lysenko.Constants.CONFIGURATION_CONJOINT;
import static works.lysenko.Constants.CONFIGURATION_CYCLES;
import static works.lysenko.Constants.CONFIGURATION_DEBUG;
import static works.lysenko.Constants.CONFIGURATION_DIR;
import static works.lysenko.Constants.CONFIGURATION_DOWNSTREAM;
import static works.lysenko.Constants.CONFIGURATION_EWAIT;
import static works.lysenko.Constants.CONFIGURATION_IWAIT;
import static works.lysenko.Constants.CONFIGURATION_ROOT;
import static works.lysenko.Constants.CONFIGURATION_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_ADEBUG;
import static works.lysenko.Constants.DEFAULT_APP;
import static works.lysenko.Constants.DEFAULT_CONJOINT;
import static works.lysenko.Constants.DEFAULT_CYCLES;
import static works.lysenko.Constants.DEFAULT_DEBUG;
import static works.lysenko.Constants.DEFAULT_DIR;
import static works.lysenko.Constants.DEFAULT_DOWNSTREAM;
import static works.lysenko.Constants.DEFAULT_EWAIT;
import static works.lysenko.Constants.DEFAULT_IWAIT;
import static works.lysenko.Constants.DEFAULT_ROOT;
import static works.lysenko.Constants.DEFAULT_UPSTREAM;
import static works.lysenko.Constants.KNOWN_ISSUES;
import static works.lysenko.Constants.TEST;
import static works.lysenko.Constants.TESTS;
import static works.lysenko.enums.Ansi.y;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.Point;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import works.lysenko.enums.Platform;
import works.lysenko.enums.Severity;
import works.lysenko.scenarios.AbstractScenario;
import works.lysenko.scenarios.Scenario;
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
	@SuppressWarnings("javadoc")
	public Parameters parameters;
	private Properties know;
	private Properties prop;
	protected Set<String> newIssues = Collections.newSetFromMap(new ConcurrentHashMap<>());
	protected Set<String> knownIssues = new HashSet<String>();
	protected Set<String> notReproduced;
	/**
	 * Shared data storage (for prerequisites management)
	 */
	public Properties data;

	/**
	 * Exception happened during execution
	 */
	public Exception exception;

	/**
	 * 
	 */
	public Execution() {
		this(null);
	}

	/**
	 * Start legacy-compatible execution with defined Implicit and Explicit waits
	 * @param iwait 
	 * @param ewait 
	 * 
	 * @param browser
	 * 
	 * @param parametersList
	 */

	public Execution(int iwait, int ewait, String browser) {
		super();
		this.x = this;
		Set<String> logsToRead = Set.of(BROWSER, CLIENT, DRIVER, PERFORMANCE, PROFILER, SERVER);

		// Bot components
		t = new Stopwatch();
		r = new Results(this);
		l = new Logger(this, logsToRead);
		o = new Output(this);

		// Web driver components
		d = WebDrivers.get(Platform.get(browser), false);
		d.manage().window().setPosition(new Point(0, 0));

		// Web driver parameters
		d.manage().timeouts().implicitlyWait(Duration.ofSeconds(iwait));
		w = new WebDriverWait(d, Duration.ofSeconds(ewait));
	}

	/**
	 * 
	 * Start bot-compatible execution
	 * 
	 * @param parametersList
	 */
	@SuppressWarnings("unchecked")
	public Execution(String parametersList) {
		super();
		this.x = this;
		Set<String> logsToRead = Set.of(BROWSER, CLIENT, DRIVER, PERFORMANCE, PROFILER, SERVER);

		// Parameters
		parameters = new Parameters(parametersList);

		// Bot components
		t = new Stopwatch();
		r = new Results(this);
		l = new Logger(this, logsToRead);
		o = new Output(this);

		// Test properties
		data = new Properties();
		know = readProperties(KNOWN_ISSUES);
		prop = readProperties(TESTS + (String) parameters.get("TEST") + TEST);

		// Web driver components
		String platform = parameters.string("PLATFORM");
		if (Platform.get(platform).equals(Platform.ANDROID)) {

			if (_adebug())
				service = new AppiumServiceBuilder().withArgument(() -> "--base-path", "/wd/hub/")
						.withLogFile(new File("appium.log")).build();
			else
				service = new AppiumServiceBuilder().withArgument(() -> "--base-path", "/wd/hub/")
						.withArgument(() -> "--log-level", "warn").withLogFile(new File("appium.log")).build();

			service.start();
			File appDir = new File(new File(System.getProperty("user.dir")), _dir());
			File app = null;
			try {
				app = new File(appDir.getCanonicalPath(), _app());
			} catch (IOException e) {
				e.printStackTrace();
			}
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("appium:automationName", "UiAutomator2");
			capabilities.setCapability("app", app.getAbsolutePath());
			d = new AndroidDriver(service.getUrl(), capabilities);

		} else {
			d = WebDrivers.get(Platform.get(parameters.string("PLATFORM")), false);
			d.manage().window().setPosition(new Point(0, 0));
			d.manage().window().maximize();
		}

		// Web driver parameters
		d.manage().timeouts().implicitlyWait(Duration.ofSeconds(_iwait()));
		w = new WebDriverWait(d, Duration.ofSeconds(_ewait()));

		// That's one dirty trick :)
		notReproduced = new HashSet<String>((Set<String>) (Set<?>) know.keySet());
	}

	/**
	 * @return _adebug parameter 
	 */
	public boolean _adebug() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_ADEBUG, DEFAULT_ADEBUG).trim());
	}

	/**
	 * @return app parameter
	 */
	public String _app() {
		return String.valueOf(prop.getProperty("_" + CONFIGURATION_APP, DEFAULT_APP).trim());
	}

	/**
	 * @return whether current test execution have "conjoint" mode active
	 */
	public boolean _conjoint() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_CONJOINT, DEFAULT_CONJOINT).trim());
	}

	/**
	 * @return configured number of cycles to be executed
	 */
	public int _cycles() {
		return Integer.valueOf(prop.getProperty("_" + CONFIGURATION_CYCLES, DEFAULT_CYCLES).trim());
	}

	/**
	 * @return true if debug mode is configured in current test run
	 */
	public boolean _debug() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_DEBUG, DEFAULT_DEBUG).trim());
	}

	/**
	 * @return dir parameter
	 */
	public String _dir() {
		return String.valueOf(prop.getProperty("_" + CONFIGURATION_DIR, DEFAULT_DIR).trim());
	}

	/**
	 * @return true if downstream weight distribution is activated in configuration
	 */
	public boolean _downstream() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_DOWNSTREAM, DEFAULT_DOWNSTREAM).trim());
	}

	/**
	 * @return configured number of cycles to be executed
	 */
	private int _ewait() {
		return Integer.valueOf(prop.getProperty("_" + CONFIGURATION_EWAIT, DEFAULT_EWAIT).trim());
	}

	/**
	 * @return configured number of cycles to be executed
	 */
	private int _iwait() {
		return Integer.valueOf(prop.getProperty("_" + CONFIGURATION_IWAIT, DEFAULT_IWAIT).trim());
	}

	/**
	 * @return configured root of scenarios
	 */
	public String _root() {
		return prop.getProperty("_" + CONFIGURATION_ROOT, DEFAULT_ROOT).trim();
	}

	/**
	 * @return true if upstream weight distribution is activated in configuration
	 */
	public boolean _upstream() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_UPSTREAM, DEFAULT_UPSTREAM).trim());
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
	 * Calculate downstream weights of given scenario from current execution
	 * parameters
	 * 
	 * @param s Scenario to calculate downstream weights
	 * @return cumulative downstream weight
	 */
	public Double downstream(Scenario s) {
		// TODO: move this to Scenario class(es) ?
		double v = 0.0;
		for (Entry<Object, Object> p : prop.entrySet()) {
			String k = (String) p.getKey();
			if (!(k.charAt(0) == '_')) {
				String a = s.getClass().getName().toLowerCase();
				String b = k.toLowerCase();
				if (a.contains(b)) {
					if (!(p.getValue().equals("-")))
						v = v + Double.valueOf((String) p.getValue());
				}
			}
		}
		s.downstream(v);
		return v;
	}

	/**
	 * @param p string to search Known Issues
	 * @return Set of KnownIssues for given query
	 */
	public Set<String> getKnownIssue(String p) {
		HashSet<String> ki = new HashSet<String>();
		if (null != know)
			know.forEach((k, v) -> {
				if (p.contains((String) v)) {
					ki.add((String) k);
					if (notReproduced.contains(k)) {
						notReproduced.remove(k);
						knownIssues.add((String) k);
					}
				}
			});
		return ki;
	}

	/**
	 * @return Gson object built by cached GsonBuilder
	 */
	public Gson gson() {
		return gsonBuilder.create();
	}

	/**
	 * @return browser in which current execution takes place
	 */
	public Platform in() {
		return Platform.get(x.parameters.string("PLATFORM"));
	}

	/**
	 * @param b
	 * @return true if current is in defined browser
	 */
	public boolean in(Platform b) {
		return (x.parameters.string("PLATFORM").equals(b.title()));
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
		if (null != name) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(name);
			} catch (FileNotFoundException e) {
				l.logProblem(Severity.S1, "Requested properties file '" + name + "' not found");
			}
			try {
				if (null != fis)
					prop.load(fis);
				else
					l.logProblem(Severity.S2, "Unable to read properties from '" + name + "'");
			} catch (IOException e) {
				throw new IllegalArgumentException("Unable to load requested properties file");
			}
		}
		return prop;
	}

	/**
	 * @return amount of milliseconds since start of test execution
	 */
	public Long timer() {
		return t.millis();
	}

	/**
	 * @return test description
	 */
	public String testDescription() {
		return (y(x.parameters.get("TEST") + " on " + y(x.parameters.get("DOMAIN"))));
	}
}
