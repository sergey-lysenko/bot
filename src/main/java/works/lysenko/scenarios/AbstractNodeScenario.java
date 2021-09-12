package works.lysenko.scenarios;

import static works.lysenko.Constants.NODE_SCENARIO_MARKER;

import java.util.Set;

import org.apache.commons.math3.util.Pair;

import works.lysenko.Execution;

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
	public AbstractNodeScenario(Set<Scenario> ss, Execution x) {
		super(x);
		marker(NODE_SCENARIO_MARKER);
		scenarios = new Scenarios(x);
		scenarios.add(ss, x);
		pervWeight = scenarios.pervasive();
	}

	public int combinations(boolean onlyConfigured) {
		int c = 0;
		for (Pair<Scenario, Double> s : scenarios.get()) {
			c = c + s.getKey().combinations(onlyConfigured);
		}
		return c;
	}

	public Set<String> list(boolean shortened, boolean decorated) {
		Set<String> c = super.list(shortened, decorated);
		c.addAll(scenarios.list(shortened, decorated));
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
		done();
	}

	/**
	 * @return whether this scenario meets it's prerequisites
	 */
	public boolean sufficed() {
		return false;
	}

	public double pervasive() {
		return pervWeight;
	}
}
