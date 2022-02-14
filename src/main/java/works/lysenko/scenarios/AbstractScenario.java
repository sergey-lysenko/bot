package works.lysenko.scenarios;

import static works.lysenko.Constants.DEFAULT_WEIGHT;
import static works.lysenko.enums.Ansi.BLUE_BOLD_BRIGHT;
import static works.lysenko.enums.Ansi.colorize;

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import works.lysenko.Common;
import works.lysenko.Execution;
import works.lysenko.enums.ScenarioType;
import works.lysenko.utils.SortedScenarioSet;

/**
 * This is basic implementation of Scenario interface
 * 
 * @author Sergii Lysenko
 *
 */
public abstract class AbstractScenario extends Common implements Scenario {

	private long startAt = 0;
	public double uWeight = 0.0;
	public double dWeight = 0.0;

	/**
	 * @param x instance of Run object associated with this scenario
	 */
	public AbstractScenario(Execution x) {
		super(x);
	}

	/**
	 * container for actual logic of a scenario
	 */
	public void action() {

	}

	/**
	 * This routine reports an amount of possible variants of executions for
	 * underlying scenarios
	 * 
	 * @param onlyConfigured or all possible scenarios from code base
	 * @return number of possible combinations for underlying scenarios
	 */
	public int combinations(boolean onlyConfigured) {
		if (onlyConfigured)
			return ((this.dWeight > 0.0) || (this.uWeight > 0.0) || (this.weight() > 0.0)) ? 1 : 0;
		return 1;
	}

	/**
	 * Shortcut for checking the presence of a data in the common test data
	 * container
	 * 
	 * @param field of test data to be verified
	 * @return true if data is present
	 */
	public boolean contains(Object field) {
		boolean b = x.data.containsKey(field);
		if (x._debug())
			log(3, "contains(" + field + ")" + "\u2192" + b);
		return b;
	}

	/**
	 * Shortcut for checking the presence of a data in the common test data
	 * container
	 * 
	 * @param field of test data to be verified
	 * @return true if data is present
	 */
	public boolean containsKey(Object field) {
		boolean b = x.data.containsKey(field);
		if (x._debug())
			log(3, "containsKey(" + field + ")" + "\u2192" + b);
		return b;
	}

	/**
	 * Shortcut for checking the presence of a data in the common test data
	 * container
	 * 
	 * @param fields of test data to be verified
	 * @return true if all data is present
	 */
	public boolean containsKeys(Object... fields) {
		boolean b = true;
		for (Object f : fields)
			if (!x.data.containsKey(f))
				b = false;
		if (x._debug())
			log(3, "containsKeys(" + Arrays.toString(fields) + ")" + "\u0336" + b);
		// + " " + LF + x.gson().toJson(x.data));
		return b;
	}

	/**
	 * This is a stub of the defConf() function which is to be redefined in
	 * descending abstract scenarios implementations
	 * 
	 * @return null
	 */
	public Set<String> defConf() {
		return null;
	}

	/**
	 * @return logical level of current scenario (number of ascendants)
	 */
	public int depth() {
		return gauge() - x.minDepth;
	}

	protected void done() {
		long r = x.timer() - startAt;
		if (standalone()) {
			l.log(0, "Standalone " + this.shortName() + " done in " + timeH(r));
			l.logln();
		} else {
			l.log(0, "'" + this.shortName() + "' done in " + timeH(r));
		}
		x.current.pop();
	}

	/**
	 * @return current downstream weight
	 */
	public double downstream() {
		return dWeight;
	}

	/**
	 * @param d downstream weight to set
	 */
	public void downstream(double d) {
		this.dWeight = d;
	}

	/**
	 * @return whether this scenario is able to be executed
	 */
	public boolean executable() {
		return !(weight().isNaN());
	}

	/**
	 * Default common code for a scenario execution
	 */
	public void execute() {
		startAt = x.timer();
		x.current.push(this);
		l.logln();
		l.log(0, colorize(type().tag() + " " + shortName(), BLUE_BOLD_BRIGHT) + " : " + x.r.count(this));
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
		if (x._debug())
			getLog(field, o);
		return o;
	}

	/**
	 * Shortcut for getting a data from the common test data container, with
	 * optional default
	 * 
	 * @param field of test data to be retrieved
	 * @param def   default value
	 * @return copy of test data
	 */
	public Object get(Object field, Object def) {
		Object o = x.data.getOrDefault(field, def);
		if (x._debug())
			getLog(field, o);
		return o;
	}

	private void getLog(Object field, Object o) {
		log(3, "get(" + field + ")" + "\u21D2" + o + " ");
	}

	/**
	 * This is a stub implementation of empty finals() routine which is to be
	 * redefined in scenarios as needed
	 */
	@Override
	public void finals() {

	}

	/**
	 * @return list of underlying scenarios
	 *
	 */
	public Set<Scenario> list() {
		Set<Scenario> c = new SortedScenarioSet();
		c.add(this);
		return c;
	}

	/**
	 * @return unique name of this scenario
	 */
	public String name() {
		return this.getClass().getName();
	}

	/**
	 * Shortcut for putting a data into the common test data container
	 * 
	 * @param field of test data to be updated
	 * @param value of this field
	 */
	public void put(Object field, Object value) {
		Object o = x.data.put(field, value);
		if (x._debug()) {
			String j = x.gson().toJson(x.data);
			log(3, "put(" + field + ")" + "\u21D0" + "[" + value + "\u2192" + o + "]");
			l.logFile(j, "data", "json");
		}
	}

	/**
	 * Shortcut for removing a data from the common test data container
	 * 
	 * @param field of test data to be updated
	 */
	public void remove(Object field) {
		x.data.remove(field);
	}

	/**
	 * @return shortened scenario name
	 */

	public String shortName() {
		String[] a;
		if (null == x.minDepth)
			gauge();
		a = this.getClass().getName().split("\\.");
		int b = x.minDepth - 1;
		int c = a.length;
		return String.join(".", Arrays.copyOfRange(a, b, c));
	}

	/**
	 * @return whether current scenario is a standalone one
	 */
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
	 * @return
	 */
	public ScenarioType type() {
		if (this instanceof AbstractLeafScenario)
			return ScenarioType.LEAF;
		if (this instanceof AbstractNodeScenario)
			return ScenarioType.NODE;
		if (this instanceof AbstractMonoScenario)
			return ScenarioType.MONO;
		return null;
	}

	/**
	 * @return current upstream weight
	 */
	public double upstream() {
		return uWeight;
	}

	/**
	 * @return current weight coefficient of this scenario acquired from run
	 *         properties
	 */
	public Double weight() {
		String w = x.prop(StringUtils.removeStart(this.getClass().getName(), x._root().concat(".")),
				DEFAULT_WEIGHT);
		if (w.equals("-"))
			return Double.NaN;
		return Double.valueOf(w);
	}

}
