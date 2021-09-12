package works.lysenko.scenarios;

import static works.lysenko.Constants.LEAF_SCENARIO_MARKER;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.utils.SortedStringSet;

public class AbstractLeafScenario extends AbstractScenario {

	public AbstractLeafScenario(Execution x) {
		super(x);
		marker(LEAF_SCENARIO_MARKER);
	}

	public Set<String> list(boolean shortened, boolean decorated) {
		Set<String> c = new SortedStringSet();
		if (shortened)
			c.add(this.shortName(decorated));
		else
			c.add(this.name(decorated));
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
		done();
	}
}
