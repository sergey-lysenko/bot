package works.lysenko.scenarios;

import static works.lysenko.Constants.DATA;
import static works.lysenko.Constants.DEFAULT_WEIGHT;
import static works.lysenko.Constants.DONE_IN;
import static works.lysenko.Constants.JSON;
import static works.lysenko.Constants.MASKED_DOT;
import static works.lysenko.Constants.STANDALONE;
import static works.lysenko.Constants.u0020;
import static works.lysenko.Constants.u0027;
import static works.lysenko.Constants.u0028;
import static works.lysenko.Constants.u0029;
import static works.lysenko.Constants.u002D;
import static works.lysenko.Constants.u002E;
import static works.lysenko.Constants.u003A;
import static works.lysenko.Constants.u005B;
import static works.lysenko.Constants.u005D;
import static works.lysenko.Constants.u2192;
import static works.lysenko.Constants.u21D0;
import static works.lysenko.Constants.u21D2;
import static works.lysenko.enums.Ansi.BLUE_BOLD_BRIGHT;
import static works.lysenko.enums.Ansi.colorize;

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import works.lysenko.Common;
import works.lysenko.Execution;
import works.lysenko.enums.ScenarioType;
import works.lysenko.utils.SortedScenarioSet;

/**
 * This is basic implementation of {@link works.lysenko.scenarios.Scenario}
 * interface
 *
 * @author Sergii Lysenko
 *
 */
public abstract class AbstractScenario extends Common implements Scenario {

	private long startAt = 0;

	/**
	 * current upstream weight
	 */
	public double uWeight = 0.0;

	/**
	 * current downstream weight
	 */
	public double dWeight = 0.0;

	/**
	 * @param x instance of {#link works.lysenko.Execution} object associated with
	 *          this scenario
	 */
	public AbstractScenario(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		// this is a stub for code to be executed before sub-scenarios
	}

	@SuppressWarnings("boxing")
	@Override
	public int combinations(boolean onlyConfigured) {
		if (onlyConfigured)
			return this.dWeight > 0.0 || this.uWeight > 0.0 || weight() > 0.0 ? 1 : 0;
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
		boolean b = this.x.data.containsKey(field);
		if (this.x._debug())
			log(3, Thread.currentThread().getStackTrace()[1].getMethodName() + u0028 + field + u0029 + u2192 + b);
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
		boolean b = this.x.data.containsKey(field);
		if (this.x._debug())
			log(3, Thread.currentThread().getStackTrace()[1].getMethodName() + u0028 + field + u0029 + u2192 + b);
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
			if (!this.x.data.containsKey(f))
				b = false;
		if (this.x._debug())
			log(3, Thread.currentThread().getStackTrace()[1].getMethodName() + u0028 + Arrays.toString(fields) + u0029
					+ u2192 + b);
		return b;
	}

	/**
	 * This is a stub of the defConf() function which is to be redefined in
	 * descending abstract scenarios implementations
	 *
	 * @return null
	 */
	public static Set<String> defConf() {
		return null;
	}

	/**
	 * @return logical level of current scenario (number of ascendants)
	 */
	@SuppressWarnings("boxing")
	public int depth() {
		return gauge() - this.x.minDepth;
	}

	protected void done() {
		@SuppressWarnings("boxing")
		long r1 = this.x.timer() - this.startAt;
		this.l.log(0, u0027 + shortName() + u0027 + u0020 + DONE_IN + u0020 + timeH(r1));
		this.x.current.pop();
	}

	@Override
	public double downstream() {
		return this.dWeight;
	}

	@Override
	public void downstream(double dw) {
		this.dWeight = dw;
	}

	@Override
	public boolean executable() {
		return !weight().isNaN();
	}

	@SuppressWarnings("boxing")
	protected void execute() {
		this.startAt = this.x.timer();
		this.x.current.push(this);
		this.l.logln();
		this.l.log(0, colorize(type().tag() + u0020 + shortName(), BLUE_BOLD_BRIGHT) + u0020 + u003A + u0020
				+ this.x.r.count(this));
	}

	@Override
	public void finals() {
		// this is a stub for code to be executed after sub-scenarios
	}

	@SuppressWarnings("boxing")
	private int gauge() {
		int g = name().split(MASKED_DOT).length;
		if (null == this.x.minDepth)
			this.x.minDepth = g;
		return g;
	}

	/**
	 * Shortcut for getting a data from the common test data container
	 *
	 * @param field of test data to be retrieved
	 * @return copy of test data
	 */
	public Object get(Object field) {
		Object obj = this.x.data.get(field);
		if (this.x._debug())
			getLog(field, obj);
		return obj;
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
		Object obj = this.x.data.getOrDefault(field, def);
		if (this.x._debug())
			getLog(field, obj);
		return obj;
	}

	private void getLog(Object field, Object obj) {
		log(3, Thread.currentThread().getStackTrace()[2].getMethodName() + u0028 + field + u0029 + u21D2 + obj + u0020);
	}

	@Override
	public Set<Scenario> list() {
		Set<Scenario> c = new SortedScenarioSet();
		c.add(this);
		return c;
	}

	@Override
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
		Object obj = this.x.data.put(field, value);
		if (this.x._debug()) {
			JSONObject j = new JSONObject(this.x.data);
			log(3, Thread.currentThread().getStackTrace()[1].getMethodName() + u0028 + field + u0029 + u21D0 + u005B
					+ value + u2192 + obj + u005D);
			this.l.logFile(j.toString(), DATA, JSON);
		}
	}

	/**
	 * Shortcut for removing a data from the common test data container
	 *
	 * @param field of test data to be updated
	 */
	public void remove(Object field) {
		this.x.data.remove(field);
	}

	@Override
	public String shortName() {
		String[] a;
		if (null == this.x.minDepth)
			gauge();
		a = this.getClass().getName().split(MASKED_DOT);
		@SuppressWarnings("boxing")
		int b = this.x.minDepth - 1;
		int c = a.length;
		return String.join(u002E, Arrays.copyOfRange(a, b, c));
	}

	@Override
	public boolean sufficed() {
		return false;
	}

	@Override
	public ScenarioType type() {
		if (this instanceof AbstractLeafScenario)
			return ScenarioType.LEAF;
		if (this instanceof AbstractNodeScenario)
			return ScenarioType.NODE;
		if (this instanceof AbstractMonoScenario)
			return ScenarioType.MONO;
		return null;
	}

	@Override
	public double upstream() {
		return this.uWeight;
	}

	@SuppressWarnings("boxing")
	@Override
	public Double weight() {
		String wh = this.x.prop(StringUtils.removeStart(this.getClass().getName(), this.x._root().concat(u002E)),
				DEFAULT_WEIGHT);
		if (wh.equals(u002D))
			return Double.NaN;
		return Double.valueOf(wh);
	}
}
