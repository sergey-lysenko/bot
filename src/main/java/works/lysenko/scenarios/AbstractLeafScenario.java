package works.lysenko.scenarios;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import works.lysenko.Constants;
import works.lysenko.Run;

public class AbstractLeafScenario extends AbstractScenario {
	public AbstractLeafScenario(Run r) {
		super(r);
	}

	/**
	 * Returns a set of string for default property file for tests configuration
	 * related to this scenario
	 */
	public final Set<String> defConf() {
		Set<String> c = new TreeSet<String>((new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		}));
		c.add(this.getClass().getName() + " = " + Constants.DEFAULT_SCENARIO_WEIGHT);
		return c;
	}

	/**
	 * For a Leaf Scenario, execution consist of
	 * 
	 * 1) default execution code defined in super class
	 * 
	 * 2) logic defined in action()
	 * 
	 * 3) final steps defined in finals() of this scenario
	 */
	public final void execute() {
		super.execute();
		action();
		finals();
	}
}
