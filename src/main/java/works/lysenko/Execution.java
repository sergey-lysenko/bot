package works.lysenko;

import static org.openqa.selenium.logging.LogType.BROWSER;
import static org.openqa.selenium.logging.LogType.CLIENT;
import static org.openqa.selenium.logging.LogType.DRIVER;
import static org.openqa.selenium.logging.LogType.PERFORMANCE;
import static org.openqa.selenium.logging.LogType.PROFILER;
import static org.openqa.selenium.logging.LogType.SERVER;
import static works.lysenko.Constants.CI;
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
import static works.lysenko.Constants.DOMAIN;
import static works.lysenko.Constants.KNOWN_ISSUES;
import static works.lysenko.Constants.PLATFORM;
import static works.lysenko.Constants.TEST;
import static works.lysenko.Constants.TESTS;
import static works.lysenko.Constants.USER_DIR;
import static works.lysenko.Constants._ON_;
import static works.lysenko.Constants.u002D;
import static works.lysenko.Constants.u002E;
import static works.lysenko.Constants.u005F;
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

// import com.google.gson.Gson;
// import com.google.gson.GsonBuilder;

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
	public static boolean insideCI() {
		return System.getenv().containsKey(CI);
	}

	/**
	 * @return true if current execution is performed in a Docker container
	 */
	public static boolean insideDocker() {
		try {
			return Files.readString(Paths.get("/proc/1/cgroup")).contains("/docker"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (@SuppressWarnings("unused") NoSuchFileException e) {
			// most probably that is caused by being executed on Windows;
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Stack of currently executed scenarios
	 */
	public Stack<AbstractScenario> current = new Stack<>();
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

	@SuppressWarnings("javadoc")
	public Parameters parameters;
	private Properties know;
	private Properties prop;
	protected Set<String> newIssues = Collections.newSetFromMap(new ConcurrentHashMap<>());
	protected Set<String> knownIssues = new HashSet<>();
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
	 *
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
		this.t = new Stopwatch();
		this.r = new Results(this);
		this.l = new Logger(this, logsToRead);
		this.o = new Output(this);

		// Web driver components
		this.d = WebDrivers.get(Platform.get(browser), false);
		this.d.manage().window().setPosition(new Point(0, 0));

		// Web driver parameters
		this.d.manage().timeouts().implicitlyWait(Duration.ofSeconds(iwait));
		this.w = new WebDriverWait(this.d, Duration.ofSeconds(ewait));
	}

	/**
	 *
	 * Start bot-compatible execution
	 *
	 * @param parametersList
	 */
	@SuppressWarnings({ "unchecked", "nls" })
	public Execution(String parametersList) {
		super();
		this.x = this;
		Set<String> logsToRead = Set.of(BROWSER, CLIENT, DRIVER, PERFORMANCE, PROFILER, SERVER);

		// Parameters
		this.parameters = new Parameters(parametersList);

		// Bot components
		this.t = new Stopwatch();
		this.r = new Results(this);
		this.l = new Logger(this, logsToRead);
		this.o = new Output(this);

		// Test properties
		this.data = new Properties();
		this.know = readProperties(KNOWN_ISSUES);
		this.prop = readProperties(TESTS + (String) this.parameters.get(TEST.toUpperCase()) + u002E + TEST);

		// Web driver components
		String platform = this.parameters.string(PLATFORM);
		if (Platform.get(platform).equals(Platform.ANDROID)) {

			if (_adebug())
				this.service = new AppiumServiceBuilder().withArgument(() -> "--base-path", "/wd/hub/")
						.withLogFile(new File("appium.log")).build();
			else
				this.service = new AppiumServiceBuilder().withArgument(() -> "--base-path", "/wd/hub/")
						.withArgument(() -> "--log-level", "warn").withLogFile(new File("appium.log")).build();

			this.service.start();
			File appDir = new File(new File(System.getProperty(USER_DIR)), _dir());
			File app = null;
			try {
				app = new File(appDir.getCanonicalPath(), _app());
			} catch (IOException e) {
				e.printStackTrace();
			}
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("appium:automationName", "UiAutomator2");
			if (null != app)
				capabilities.setCapability("app", app.getAbsolutePath());
			this.d = new AndroidDriver(this.service.getUrl(), capabilities);

		} else {
			this.d = WebDrivers.get(Platform.get(this.parameters.string(PLATFORM)), false);
			this.d.manage().window().setPosition(new Point(0, 0));
			this.d.manage().window().maximize();
		}

		// Web driver parameters
		this.d.manage().timeouts().implicitlyWait(Duration.ofSeconds(_iwait()));
		this.w = new WebDriverWait(this.d, Duration.ofSeconds(_ewait()));

		// That's one dirty trick :)
		this.notReproduced = new HashSet<>((Set<String>) (Set<?>) this.know.keySet());
	}

	/**
	 * @return _adebug parameter
	 */
	@SuppressWarnings("boxing")
	public boolean _adebug() {
		return Boolean.valueOf(this.prop.getProperty(u005F + CONFIGURATION_ADEBUG, DEFAULT_ADEBUG).trim());
	}

	/**
	 * @return app parameter
	 */
	public String _app() {
		return String.valueOf(this.prop.getProperty(u005F + CONFIGURATION_APP, DEFAULT_APP).trim());
	}

	/**
	 * @return whether current test execution have "conjoint" mode active
	 */
	public Boolean _conjoint() {
		return Boolean.valueOf(this.prop.getProperty(u005F + CONFIGURATION_CONJOINT, DEFAULT_CONJOINT).trim());
	}

	/**
	 * @return configured number of cycles to be executed
	 */
	@SuppressWarnings("boxing")
	public int _cycles() {
		return Integer.valueOf(this.prop.getProperty(u005F + CONFIGURATION_CYCLES, DEFAULT_CYCLES).trim());
	}

	/**
	 * @return true if debug mode is configured in current test run
	 */
	@SuppressWarnings("boxing")
	public boolean _debug() {
		return Boolean.valueOf(this.prop.getProperty(u005F + CONFIGURATION_DEBUG, DEFAULT_DEBUG).trim());
	}

	/**
	 * @return dir parameter
	 */
	public String _dir() {
		return String.valueOf(this.prop.getProperty(u005F + CONFIGURATION_DIR, DEFAULT_DIR).trim());
	}

	/**
	 * @return true if downstream weight distribution is activated in configuration
	 */
	@SuppressWarnings("boxing")
	public boolean _downstream() {
		return Boolean.valueOf(this.prop.getProperty(u005F + CONFIGURATION_DOWNSTREAM, DEFAULT_DOWNSTREAM).trim());
	}

	/**
	 * @return configured number of cycles to be executed
	 */
	@SuppressWarnings("boxing")
	private int _ewait() {
		return Integer.valueOf(this.prop.getProperty(u005F + CONFIGURATION_EWAIT, DEFAULT_EWAIT).trim());
	}

	/**
	 * @return configured number of cycles to be executed
	 */
	@SuppressWarnings("boxing")
	private int _iwait() {
		return Integer.valueOf(this.prop.getProperty(u005F + CONFIGURATION_IWAIT, DEFAULT_IWAIT).trim());
	}

	/**
	 * @return configured root of scenarios
	 */
	public String _root() {
		return this.prop.getProperty(u005F + CONFIGURATION_ROOT, DEFAULT_ROOT).trim();
	}

	/**
	 * @return true if upstream weight distribution is activated in configuration
	 */
	@SuppressWarnings("boxing")
	public boolean _upstream() {
		return Boolean.valueOf(this.prop.getProperty(u005F + CONFIGURATION_UPSTREAM, DEFAULT_UPSTREAM).trim());
	}

	/**
	 * Performs following routines before ending test execution: - stats() - write
	 * execution summary to the console - jsonStats() - write test execution summary
	 * in JSON format (for following import into ELK) - conditionalClose() - close
	 * browser window if not in CI
	 */
	public void complete() {
		this.o.stats();
		this.o.jsonStats();
		this.o.svgStats();
		conditionalClose();
	}

	/**
	 * Close browser window in case of execution in CI, but keep it open in case of
	 * local execution, which come in handy for development of scenarios
	 */
	public void conditionalClose() {
		try {
			this.l.logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (insideDocker() || insideCI())
			try {
				this.d.quit();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * @return number of current cycle
	 */
	public int currentCycle() {
		return _cycles() - this.cycles.cyclesToDo;
	}

	/**
	 * @return logical depth of current scenario
	 */
	public int currentDepth() {
		return null == this.x.currentScenario() ? 0 : this.x.currentScenario().depth();
	}

	/**
	 * @return link to currently executed Scenario
	 */
	public AbstractScenario currentScenario() {
		return this.current.empty() ? null : this.current.peek();
	}

	/**
	 * Calculate downstream weights of given scenario from current execution
	 * parameters
	 *
	 * @param s Scenario to calculate downstream weights
	 * @return cumulative downstream weight
	 */
	@SuppressWarnings("boxing")
	public double downstream(Scenario s) {
		// TODO: move this to Scenario class(es) ?
		double v = 0.0;
		for (Entry<Object, Object> p : this.prop.entrySet()) {
			String k = (String) p.getKey();
			if (!(k.charAt(0) == '_')) {
				String a = s.getClass().getName().toLowerCase();
				String b = k.toLowerCase();
				if (a.contains(b))
					if (!p.getValue().equals(u002D))
						v = v + Double.valueOf((String) p.getValue());
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
		HashSet<String> ki = new HashSet<>();
		if (null != this.know)
			this.know.forEach((k, v) -> {
				if (p.contains((String) v)) {
					ki.add((String) k);
					if (this.notReproduced.contains(k)) {
						this.notReproduced.remove(k);
						this.knownIssues.add((String) k);
					}
				}
			});
		return ki;
	}

	/**
	 * @return browser in which current execution takes place
	 */
	@Override
	public Platform in() {
		return Platform.get(this.x.parameters.string(PLATFORM));
	}

	/**
	 * @param b
	 * @return true if current is in defined browser
	 */
	@Override
	public boolean in(Platform b) {
		return this.x.parameters.string(PLATFORM).equals(b.title());
	}

	/**
	 * @param n   name of run property
	 * @param def default value of run property
	 * @return value of requested property
	 */
	public String prop(String n, String def) {
		return this.prop.getProperty(n, def);
	}

	/**
	 * @return true is properties is empty
	 */
	public boolean propEmpty() {
		return this.prop.isEmpty();
	}

	@SuppressWarnings({ "resource", "nls" })
	private Properties readProperties(String name) {
		Properties properties = new Properties();
		if (null != name) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(name);
			} catch (@SuppressWarnings("unused") FileNotFoundException e) {
				this.l.logProblem(Severity.S1, "Requested properties file '" + name + "' not found");
			}
			try {
				if (null != fis)
					properties.load(fis);
				else
					this.l.logProblem(Severity.S2, "Unable to read properties from '" + name + "'");
			} catch (@SuppressWarnings("unused") IOException e) {
				throw new IllegalArgumentException("Unable to load requested properties file");
			}
		}
		return properties;
	}

	/**
	 * @return test description
	 */
	public String testDescription() {
		return y(this.x.parameters.get(TEST.toUpperCase()) + _ON_ + y(this.x.parameters.get(DOMAIN)));
	}

	/**
	 * @return amount of milliseconds since start of test execution
	 */
	@SuppressWarnings("boxing")
	public Long timer() {
		return this.t.millis();
	}
}
