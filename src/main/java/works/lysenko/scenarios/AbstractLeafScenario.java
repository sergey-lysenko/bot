package works.lysenko.scenarios;

import static works.lysenko.Constants.LEAF_SCENARIO_MARKER;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.utils.SortedStringSet;

/**
 * @author Sergii Lysenko
 *
 */
public class AbstractLeafScenario extends AbstractScenario {

	/**
	 * Abstract constructor of Leaf Scenario
	 * 
	 * @param x reference to Execution object
	 */
	public AbstractLeafScenario(Execution x) {
		super(x);
		marker(LEAF_SCENARIO_MARKER);
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

	/**
	 * This is a stub implementation of empty finals() routine which is to be
	 * redefined in scenarios as needed
	 */
	@Override
	public void finals() {
		
	}

	/**
	 * Returns the set with one element - name of the scenario in requested format
	 * 
	 * @param shortened or not by removing the common part of package names
	 * @param decorated or not by scenario type marker
	 * @return set object with name of this scenario as single element
	 */
	public Set<String> list(boolean shortened, boolean decorated) {
		Set<String> c = new SortedStringSet();
		if (shortened)
			c.add(this.shortName(decorated));
		else
			c.add(this.name(decorated));
		return c;
	}
}
