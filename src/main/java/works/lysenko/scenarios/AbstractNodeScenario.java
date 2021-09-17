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

	/**
	 * @param ss name of package to load child scenarios from with weight
	 *           coefficient defined within ..
	 * @param r  instance of Run object
	 */
	public AbstractNodeScenario(String s, Execution x) {
		super(x);
		marker(NODE_SCENARIO_MARKER);
		scenarios = new Scenarios(x);
		scenarios.add((Set<Scenario>) ScenarioLoader.read(s, x), x);
		pervWeight = scenarios.pervasive();
	}
	
	/**
	 * @param onlyConfigured whether to include all available paths or only ones
	 *                       currently configured for execution
	 * 
	 * @return calculated amount of possible execution paths of underlying scenarios
	 */
	public int combinations(boolean onlyConfigured) {
		int c = 0;
		for (Pair<Scenario, Double> s : scenarios.get()) {
			c = c + s.getKey().combinations(onlyConfigured);
		}
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
	 * Returns the set with names of all underlying  
	 * 
	 * @param shortened or not by removing the common part of package names
	 * @param decorated or not by scenario type marker
	 * @return set object with name of this scenario as single element
	 */
	public Set<String> list(boolean shortened, boolean decorated) {
		Set<String> c = super.list(shortened, decorated);
		c.addAll(scenarios.list(shortened, decorated));
		return c;
	}

	/**
	 * @return calculated pervasive weight of this scenario
	 */
	public double pervasive() {
		return pervWeight;
	}

	/**
	 * @return whether this scenario meets it's prerequisites
	 */
	public boolean sufficed() {
		return false;
	}
}
