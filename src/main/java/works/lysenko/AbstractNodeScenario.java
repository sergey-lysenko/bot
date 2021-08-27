package works.lysenko;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is abstract implementation of Node Scenario, which have defined both
 * actions to be performed as well as the set of child scenarios to be executed
 * randomly based on their weight coefficients
 * 
 * @author Sergii Lysenko
 *
 */
public class AbstractNodeScenario extends AbstractScenario {

	private Scenarios scenarios;

	/**
	 * @param ss set of child scenarios with weight coefficient defined within ..
	 * @param r  instance of Run object
	 */
	public AbstractNodeScenario(Set<Scenario> ss, Run r) {
		super(r);
		scenarios = new Scenarios(r);
		scenarios.add(ss, r);
	}

	/**
	 * Returns a set of strings for default property tests configuration file
	 * related to this scenario and all it's nested scenarios
	 */
	public Set<String> defConf() {
		Set<String> c = new TreeSet<String>((new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		}));
		c.add(this.getClass().getName() + " = " + Constants.DEFAULT_SCENARIO_WEIGHT);
		c.addAll(scenarios.defConf());
		return c;
	}

	/**
	 * For a Node Scenario, execution consist of
	 * 
	 * 1) default execution code defined in super class
	 * 
	 * 2) logic defined in action()
	 * 
	 * 3) random execution of one of nested scenarios
	 * 
	 * 4) final steps defined in finals() of this scenario
	 */
	public void execute() {
		super.execute();
		action();
		scenarios.execute();
		finals();
	}

	/**
	 * @return whether this scenario meets it's prerequisites
	 */
	public boolean sufficed() {
		return true;
	}
}
