package works.lysenko;

import java.util.Set;

/**
 * This is basic implementation of Scenario interface
 * 
 * @author Sergii Lysenko
 *
 */
public class AbstractScenario extends Common implements Scenario {

	/**
	 * @param r instance of Run object associated with this scenario
	 */
	public AbstractScenario(Run r) {
		super(r);
	}

	/**
	 * @return Unique name of this scenario
	 */
	public String name() {
		return this.getClass().getName();
	}

	/**
	 * Default common code for a scenario execution
	 */
	public void execute() {
		r.current = this;
		logln();
		log(0, colorize(name(), Color.BLUE_BOLD_BRIGHT) + " : " + r.count(name(), weight()));
	}

	/**
	 * @return current weight coefficient of this scenario acquired from run
	 *         properties
	 */
	private Double weight() {
		return Double.valueOf(r.prop(this.getClass().getName(), Constants.DEFAULT_SCENARIO_WEIGHT));
	}

	/**
	 * @return whether this scenario meets it's prerequisites
	 */
	public boolean sufficed() {
		return false;
	}

	/**
	 * container for actual logic of a scenario
	 */
	public void action() {

	}

	/**
	 * container for non-optional final action to be performed after execution of
	 * main scenario logic
	 */
	public void finals() {

	}

	/**
	 * Shortcut for getting a data from the common test data container
	 * 
	 * @param field of test data to be retrieved
	 * @return copy of test data
	 */
	public Object get(Object field) {
		return r.data.get(field);
	}

	/**
	 * Shortcut for putting a data into the common test data container
	 * 
	 * @param field of test data to be updated
	 */
	public void put(Object field, Object value) {
		r.data.put(field, value);
	}

	/**
	 * Shortcut for removing a data from the common test data container
	 * 
	 * @param field of test data to be updated
	 */
	public void remove(Object field) {
		r.data.remove(field);
	}
	
	/**
	 * Shortcut for checking the presence of a data in the common test data
	 * container
	 * 
	 * @param field of test data to be verified
	 */
	public boolean containsKey(Object field) {
		return r.data.containsKey(field);
	}

	/**
	 * This is a stub of the defConf() function which is to be redefined in
	 * descending abstract scenarios implementations
	 */
	public Set<String> defConf() {
		return null;
	}

	public int depth() {
		int depth = this.name().split("\\.").length;
		if (null == r.minDepth)
			r.minDepth = depth;
		return depth - r.minDepth;
	}
}
