package works.lysenko;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.support.ui.WebDriverWait;

import works.lysenko.logs.LogRecord;
import works.lysenko.scenarios.AbstractScenario;
import works.lysenko.utils.Browser;
import works.lysenko.utils.Color;
import works.lysenko.utils.Stopwatch;
import works.lysenko.utils.WebDrivers;

/**
 * This class represent single bot execution information
 * 
 * @author Sergii Lysenko
 *
 */
public class Run extends Common {

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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Properties data;
	public AbstractScenario current;
	public Cycles cycles = null;
	public Integer minDepth = null;
	protected Stopwatch timer;
	private Properties prop;
	private Map<String, Result> results;

	public Run(int implicitWait, int explicitWait, Set<String> logs, String name) {
		super();
		timer = new Stopwatch();
		this.r = this;
		// now when you are bored, you can do
		// r.r.r.r.r.r.r.r.r.r.driver.getTitle();
		l = new Logger(logs, this);
		d = WebDrivers.get(Browser.CHROME, false);
		d.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		w = new WebDriverWait(d, Duration.ofSeconds(explicitWait));
		current = null;
		results = new HashMap<String, Result>();
		data = new Properties();

		// Reading properties file
		String propertiesFile = Constants.DEFAULT_PROPERTIES_LOCATION + name + ".properties";
		FileInputStream fis = null;
		prop = new Properties();
		try {
			fis = new FileInputStream(propertiesFile);
		} catch (FileNotFoundException e) {
			l.logProblem("[SEVERE] Requested Test Run Properties file '" + propertiesFile + "' not found");
		}
		try {
			if (null != fis)
				prop.load(fis);
			else
				l.logProblem("[WARNING] Test run properties were not loaded");
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to load requested test properties file");
		}

	}

	/**
	 * Performs following routines before ending test execution: - stats() - write
	 * execution summary to the console - jsonStats() - write test execution summary
	 * in JSON format (for following import into ELK) - conditionalClose() - close
	 * browser window if not in CI
	 */
	public void complete() {
		stats();
		jsonStats();
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
	 * Count in test execution (in later versions there will be collection of more
	 * execution data then just times of execution)
	 * 
	 * @param s tag of test execution
	 * @param w current weight coefficient (TODO: implement dynamic weight
	 *          coefficients)
	 * @return copy of added test execution data
	 */
	public Result count(String s, double w) {
		Result r = results.getOrDefault(s, new Result());
		{
			r.weight = w; // base for future dynamic weight modification;
			++r.executions;
		}
		results.put(s, r);
		return r;
	}

	public int currentCycle() {
		return cycles() - cycles.cycles + 1;
	}

	public int cycles() {
		return Integer.valueOf(prop.getProperty("_cycles", Constants.DEFAULT_CYCLES_COUNT));
	}

	public boolean pervasive() {
		return Boolean.valueOf(prop.getProperty("_pervasive", Constants.DEFAULT_PERVASIVE_WEIGHT));
	}

	/**
	 * This routine writes test execution in JSON format. File location defined by
	 * Constants.DEFAULT_RUNS_LOCATION, file name template defined by
	 * Constants.RUN_JSON_FILENAME
	 */
	public void jsonStats() {
		TreeMap<String, Result> sorted = sortedCounters();
		BufferedWriter w;
		try {
			w = new BufferedWriter(new FileWriter(Constants.DEFAULT_RUNS_LOCATION
					+ fill(Constants.RUN_JSON_FILENAME, String.valueOf(timer.startedAt()))));
			w.write("{\"startAt\":" + timer.startedAt() + ",\"run\":[");
			int i = sorted.size();
			for (Entry<String, Result> s : sorted.entrySet()) {
				w.write("{\"scenario\":\"" + s.getKey() + "\",\"weight\": "
						+ ((s.getValue().weight == Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : s.getValue().weight)
						+ ",\"executions\":" + s.getValue().executions);
				if ((s.getValue().problems != null) && (s.getValue().problems.size() > 0)) {
					w.write(",\"problems\":[");
					int j = s.getValue().problems.size();
					for (Problem p : s.getValue().problems) {
						w.write("{\"time\":" + p.time + ",");
						String type = p.text.split(" ")[0];
						w.write("\"type\":\"" + type + "\",");
						w.write("\"text\":\"");
						w.write(p.text.substring(type.length() + 1).replace("\"", Constants.FULLWIDTH_QUOTATION_MARK));
						w.write("\"}");
						if (j-- > 1)
							w.write(",");
					}
					w.write("]");
				}
				w.write("}");
				if (i-- > 1)
					w.write(",");
			}
			w.write("]}");
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This routine stores the information about a problem in the execution results
	 */
	public void problem(LogRecord lr) {
		if (null != current)
			results.get(current.name()).problems.add(new Problem(lr.time(), lr.text()));
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

	/**
	 * Sort scenario classes ignoring the case which produces more "tree-like" order
	 * of items and improves readability
	 * 
	 * @return execution counters sorted properly
	 */
	private TreeMap<String, Result> sortedCounters() {
		TreeMap<String, Result> sorted = new TreeMap<String, Result>((new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		}));
		sorted.putAll(this.results);
		return sorted;
	}

	/**
	 * Write test execution statistics only to console
	 */
	public void stats() {
		l.log(0, "= Execution statistics =");
		TreeMap<String, Result> sorted = sortedCounters();
		if (sorted.entrySet().isEmpty())
			l.log(0, Color.colorize("[WARNING] No Test Execution Data available"));
		else
			for (Map.Entry<String, Result> e : sorted.entrySet()) {
				l.log(0, e.getKey() + " : " + e.getValue().toString());
			}
		l.logln();
	}

	/**
	 * @return amount of milliseconds since start of test execution
	 */
	public Long timer() {
		return timer.millis();
	}

	/**
	 * Write default configuration to a file
	 * 
	 * @param defConf
	 * @param fileName
	 */
	public void writeDefConf(Set<String> defConf, String fileName) {
		writeToFile(null, defConf, fileName);
	}
}
