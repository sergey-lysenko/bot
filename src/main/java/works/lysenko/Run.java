package works.lysenko;

import java.io.BufferedWriter;
import java.io.File;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class represent single bot execution information
 * 
 * @author Sergii Lysenko
 *
 */
public class Run extends Common {

	public WebDriverWait wait;
	public WebDriver driver;
	public Set<String> logs;
	public Properties data;
	public AbstractScenario current;
	protected Cycles cycles = null;
	protected Integer minDepth = null;
	protected Integer logCount = 0;
	private Properties prop;
	private Map<String, Result> results;
	private Stopwatch timer;
	private BufferedWriter logWriter;

	public Run(int implicitWait, int explicitWait, Set<String> logs, String name) {
		super();
		this.r = this;
		// now when you are bored, you can do
		// r.r.r.r.r.r.r.r.r.r.driver.getTitle();
		timer = new Stopwatch();
		current = null;
		this.driver = WebDrivers.get(Browser.CHROME, false);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		this.logs = (null == logs) ? new HashSet<String>() : logs;
		results = new HashMap<String, Result>();
		data = new Properties();

		// Reading properties file
		String propertiesFile = Constants.DEFAULT_PROPERTIES_LOCATION + name + ".properties";
		FileInputStream fis = null;
		prop = new Properties();
		try {
			new File(Constants.DEFAULT_RUNS_LOCATION).mkdirs();
			logWriter = new BufferedWriter(new FileWriter(Constants.DEFAULT_RUNS_LOCATION
					+ Routines.fill(Constants.RUN_LOG_FILENAME, String.valueOf(timer.startedAt()))));
		} catch (IOException e) {
			throw new RuntimeException("Unable to open log file for writting");
		}
		try {
			fis = new FileInputStream(propertiesFile);
		} catch (FileNotFoundException e) {
			problem("[SEVERE] Requested Test Run Properties file '" + propertiesFile + "' not found");
		}
		try {
			if (null != fis)
				prop.load(fis);
			else
				problem("[WARNING] Test run properties were not loaded");
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to load requested test properties file");
		}

	}

	public int currentCycle() {
		return cycles() - cycles.cycles + 1;
	}

	public int cycles() {
		return Integer.valueOf(prop.getProperty("cycles", Constants.DEFAULT_CYCLES_COUNT));
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

	/**
	 * @return true if current execution is performed in an environment with CI
	 *         variable set to any value. This is common way of determining
	 *         execution inside GitHUB actions and other pipelines
	 */
	public static Boolean insideCI() {
		return System.getenv().containsKey("CI");
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
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (insideDocker() || insideCI())
			driver.quit();
	}

	/**
	 * Count in test execution (in later versions there will be collection of more
	 * execution data then just times of execution)
	 * 
	 * @param s      tag of test execution
	 * @param weight current weight coefficient (TODO: implement dynamic weight
	 *               coefficients)
	 * @return copy of added test execution data
	 */
	public Result count(String s, double weight) {
		Result r = results.getOrDefault(s, new Result());
		{
			r.weight = weight; // base for future dynamic weight modification;
			++r.executions;
		}
		results.put(s, r);
		return r;
	}

	/**
	 * @param s line to be written to test execution text log
	 */
	public void logWrite(String s) {
		try {
			if (null == logWriter)
				Routines.log(0, colorize("[SEVERE] Log Writer is not initialised, unable to write log"));
			else
				logWriter.write(s + System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write test execution statistics only to console
	 */
	public void stats() {
		Routines.logln();
		Routines.log(0, "= Execution statistics =");
		TreeMap<String, Result> sorted = sortedCounters();
		if (sorted.entrySet().isEmpty())
			Routines.log(0, colorize("[WARNING] No Test Execution Data available"));
		else
			for (Map.Entry<String, Result> e : sorted.entrySet()) {
				Routines.log(0, e.getKey() + " : " + e.getValue().toString());
			}
		Routines.logln();
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
	 * This routine writes test execution in JSON format. File location defined by
	 * Constants.DEFAULT_RUNS_LOCATION, file name template defined by
	 * Constants.RUN_JSON_FILENAME
	 */
	public void jsonStats() {
		TreeMap<String, Result> sorted = sortedCounters();
		BufferedWriter w;
		try {
			w = new BufferedWriter(new FileWriter(Constants.DEFAULT_RUNS_LOCATION
					+ Routines.fill(Constants.RUN_JSON_FILENAME, String.valueOf(timer.startedAt()))));
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
	 * @return amount of milliseconds since start of test execution
	 */
	public Long timer() {
		return timer.millis();
	}

	/**
	 * This routine displays a problem in console, writes to log file, and stores it
	 * in the execution results
	 */
	public void problem(String s) {
		Long time = this.timer();
		this.logWrite(Routines.renderTime(time) + " " + colorize(s));
		if (null == current) {
			log(0, colorize(s));
			// Routines.logConsole(Routines.renderLog(0, 0, colorize(s), time));
		} else {
			Routines.logConsole(Routines.renderLog(current.depth(), 0, colorize(s), time));
			results.get(current.name()).problems.add(new Problem(time, s));
		}
	}

	/**
	 * @param name of run property
	 * @param def  ..ault value of run property
	 * @return value of requested property
	 */
	public String prop(String name, String def) {
		return prop.getProperty(name, def);
	}

	/**
	 * @return true is properties is empty
	 */
	public boolean propEmpty() {
		return prop.isEmpty();
	}

	/**
	 * Write default configuration to a file
	 * 
	 * @param defConf
	 * @param fileName
	 */
	public static void writeDefConf(Set<String> defConf, String fileName) {
		Routines.writeToFile(null, defConf, fileName);
	}
}
