package works.lysenko;

import static works.lysenko.Constants.CONFIGURATION_CONJOINT;
import static works.lysenko.Constants.CONFIGURATION_CYCLES;
import static works.lysenko.Constants.CONFIGURATION_DEBUG;
import static works.lysenko.Constants.CONFIGURATION_PERMEATIVE;
import static works.lysenko.Constants.CONFIGURATION_PERVASIVE;
import static works.lysenko.Constants.DEFAULT_CONJOINT;
import static works.lysenko.Constants.DEFAULT_CYCLES;
import static works.lysenko.Constants.DEFAULT_DEBUG;
import static works.lysenko.Constants.DEFAULT_PERMEATIVE;
import static works.lysenko.Constants.DEFAULT_PERVASIVE;
import static works.lysenko.Constants.DEFAULT_PROPERTIES_LOCATION;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

import org.openqa.selenium.support.ui.WebDriverWait;

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

	public Stack<AbstractScenario> current = new Stack<AbstractScenario>();
	public Cycles cycles = null;

	public Integer minDepth = null;
	protected Stopwatch t;
	private Properties prop;
	public Properties data;

	public String name;
	public String domain;

	public Execution(int implicitWait, int explicitWait, Set<String> logsToRead, String name) {
		this(implicitWait, explicitWait, logsToRead, name, null);
	}
	
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

		// Information
		this.name = name;
		this.domain = domain;
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

	public int currentCycle() {
		return _cycles() - cycles.cyclesToDo;
	}

	public int currentDepth() {
		return (null == x.currentScenario()) ? 0 : x.currentScenario().depth();
	}

	public AbstractScenario currentScenario() {
		return current.empty() ? null : current.peek();
	}

	public boolean _conjoint() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_CONJOINT, DEFAULT_CONJOINT));
	}

	public int _cycles() {
		return Integer.valueOf(prop.getProperty("_" + CONFIGURATION_CYCLES, DEFAULT_CYCLES));
	}

	public boolean _permeative() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_PERMEATIVE, DEFAULT_PERMEATIVE));
	}

	public Double permeative(Scenario s) {
		for (Entry<Object, Object> p : prop.entrySet()) {
			String key = (String) p.getKey();
			if (!(key.charAt(0) == '_')) {
				if (s.getClass().getName().toLowerCase().contains(key.toLowerCase())) {
					Double v = Double.valueOf((String) p.getValue());
					s.permeative(v);
					return v;
				}
			}
		}
		return 0.0;
	}

	public boolean _pervasive() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_PERVASIVE, DEFAULT_PERVASIVE));
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
			l.logProblem(Severity.S1, "Requested Test Run Properties file '" + propertiesFile + "' not found");
		}
		try {
			if (null != fis)
				prop.load(fis);
			else
				l.logProblem(Severity.S2,
						"No Test Run Properties were defined. Only initialization routine will be executed.");
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to load requested test properties file");
		}
		return prop;
	}

	/**
	 * @return amount of milliseconds since start of test execution
	 */
	public Long timer() {
		return t.millis();
	}

	public boolean debug() {
		return Boolean.valueOf(prop.getProperty("_" + CONFIGURATION_DEBUG, DEFAULT_DEBUG));
	}

	public Set<Scenario> getScenarios() {
		for (Entry<Object, Object> p : prop.entrySet()) {
			String key = (String) p.getKey();
			if (!(key.charAt(0) == '_')) {
				/*
				 * if (s.getClass().getName().toLowerCase().contains(key.toLowerCase())) {
				 * Double v = Double.valueOf((String) p.getValue()); s.permeative(v); return v;
				 */
			}
		}
		return null;
	}
}
