package works.lysenko.scenarios;

import static works.lysenko.Constants.DEFAULT_SCENARIO_WEIGHT;

import java.util.Arrays;
import java.util.Set;

import works.lysenko.Common;
import works.lysenko.Execution;
import works.lysenko.ScenarioType;
import works.lysenko.utils.Ansi;
import works.lysenko.utils.SortedStringSet;

/**
 * This is basic implementation of Scenario interface
 * 
 * @author Sergii Lysenko
 *
 */
public class AbstractScenario extends Common implements Scenario {

	protected double pervWeight = 0.0;
	private double permWeight = 0.0;
	private String marker = "?";

	/**
	 * @param r instance of Run object associated with this scenario
	 */
	public AbstractScenario(Execution x) {
		super(x);
	}

	/**
	 * container for actual logic of a scenario
	 */
	public void action() {

	}

	public int combinations(boolean onlyConfigured) {
		if (onlyConfigured)
			return ((this.permWeight > 0.0) || (this.pervWeight > 0.0) || (this.weight() > 0.0)) ? 1 : 0;
		return 1;
	}

	/**
	 * Shortcut for checking the presence of a data in the common test data
	 * container
	 * 
	 * @param field of test data to be verified
	 */
	public boolean containsKey(Object field) {
		boolean b = x.data.containsKey(field);
		if (x.debug())
			log(3, "containsKey(" + field + ")>" + b + " " + x.data.toString());
		return b;
	}

	/**
	 * Shortcut for checking the presence of a data in the common test data
	 * container
	 * 
	 * @param fields of test data to be verified
	 */
	public boolean containsKeys(Object... fields) {
		boolean b = true;
		for (Object f : fields)
			if (!x.data.containsKey(f))
				b = false;
		if (x.debug())
			log(3, "containsKeys(" + Arrays.toString(fields) + ")>" + b + " " + x.data.toString());
		return b;
	}

	/**
	 * This is a stub of the defConf() function which is to be redefined in
	 * descending abstract scenarios implementations
	 */
	public Set<String> defConf() {
		return null;
	}

	public int depth() {
		return gauge() - x.minDepth;
	}

	protected void done() {
		if (standalone()) {
			l.log(0, "Standalone " + this.name() + " done");
			l.logln();
		} else {
			l.log(0, this.name() + " done");
		}
		x.current.pop();
	}

	/**
	 * Default common code for a scenario execution
	 */
	public void execute() {
		x.current.push(this);
		l.logln();
		l.log(0, Ansi.colorize(name(true), Ansi.BLUE_BOLD_BRIGHT) + " : "
				+ x.r.countScenario(shortName(true),
						((this instanceof AbstractNodeScenario) ? ScenarioType.NODE : ScenarioType.LEAF), weight(),
						pervWeight, permWeight));
	}

	/**
	 * container for non-optional final action to be performed after execution of
	 * main scenario logic
	 */
	public void finals() {

	}

	private int gauge() {
		int g = this.name().split("\\.").length;
		if (null == x.minDepth)
			x.minDepth = g;
		return g;
	}

	/**
	 * Shortcut for getting a data from the common test data container
	 * 
	 * @param field of test data to be retrieved
	 * @return copy of test data
	 */
	public Object get(Object field) {
		Object o = x.data.get(field);
		if (x.debug())
			log(3, "get(" + field + ")>" + o + " " + x.data.toString());
		return o;
	}

	public Set<String> list(boolean shortened, boolean decorated) {
		Set<String> c = new SortedStringSet();
		if (shortened)
			c.add(this.shortName(decorated));
		else
			c.add(this.name(decorated));
		return c;
	}

	public void marker(String marker) {
		this.marker = marker;
	}

	/**
	 * @return Unique name of this scenario
	 */
	public String name() {
		return this.getClass().getName();
	}

	public String name(boolean decorated) {
		String s = name();
		if (decorated)
			return s + " " + marker;
		return s;
	}

	public double permeative() {
		return permWeight;
	}

	public void permeative(double permWeight) {
		this.permWeight = permWeight;
	}

	public double pervasive() {
		return pervWeight;
	}

	/**
	 * Shortcut for putting a data into the common test data container
	 * 
	 * @param field of test data to be updated
	 */
	public void put(Object field, Object value) {
		Object o = x.data.put(field, value);
		if (x.debug())
			log(3, "put(" + field + ")<" + o + " " + x.data.toString());
	}

	/**
	 * Shortcut for removing a data from the common test data container
	 * 
	 * @param field of test data to be updated
	 */
	public void remove(Object field) {
		x.data.remove(field);
	}

	public String shortName() {
		String[] a;
		if (null == x.minDepth)
			gauge();
		a = this.getClass().getName().split("\\.");
		int b = x.minDepth - 1;
		int c = a.length;
		return String.join(".", Arrays.copyOfRange(a, b, c));
	}

	public String shortName(boolean decorated) {
		String s = shortName();
		if (decorated)
			return s + " " + marker;
		return s;
	}

	public boolean standalone() {
		return x.currentCycle() == 0;
	}

	/**
	 * @return whether this scenario meets it's prerequisites
	 */
	public boolean sufficed() {
		return false;
	}

	/**
	 * @return current weight coefficient of this scenario acquired from run
	 *         properties
	 */
	private Double weight() {
		return Double.valueOf(x.prop(this.getClass().getName(), DEFAULT_SCENARIO_WEIGHT));
	}
}
