package works.lysenko.scenarios;

import java.util.Arrays;
import java.util.HashSet;
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
	private boolean halted = false;

	/**
	 * Default constructor adds all scenarios from properly named package as
	 * sub-scenarios of one being created
	 *
	 * @param x instance of Run object
	 */
	@SuppressWarnings("boxing")
	public AbstractNodeScenario(Execution x) {
		super(x);
		this.scenarios = new Scenarios(x);
		String name = this.getClass().getName();
		{ // Strings are immutable, we need to create a new one
			int i = name.lastIndexOf(".") + 1; // save the position of character in question //$NON-NLS-1$
			char[] nameChars = name.toCharArray(); // create character array
			nameChars[i] = Character.toLowerCase(nameChars[i]); // Lowercase single char
			name = String.valueOf(nameChars);
		}
		this.scenarios.add(ScenarioLoader.read(name, x), x);
		this.uWeight = this.scenarios.upstream();
	}

	/**
	 * Create an instance of Node Scenario with defined set of sub-scenarios
	 *
	 * @param ss array of child scenarios with weight coefficient defined within ..
	 * @param x  instance of Run object
	 */
	@SuppressWarnings("boxing")
	public AbstractNodeScenario(Execution x, Scenario... ss) {
		super(x);
		this.scenarios = new Scenarios(x);
		if (null != ss)
			this.scenarios.add(new HashSet<>(Arrays.asList(ss)), x);
		this.uWeight = this.scenarios.upstream();
	}

	/**
	 * Create a Node Scenario and add all scenarios from defined package as
	 * sub-scenarios to this one
	 *
	 * @param s name of package to load child scenarios from with weight coefficient
	 *          defined within ..
	 * @param x instance of Run object
	 */
	@SuppressWarnings("boxing")
	public AbstractNodeScenario(Execution x, String s) {
		super(x);
		this.scenarios = new Scenarios(x);
		this.scenarios.add(ScenarioLoader.read(s, x), x);
		this.uWeight = this.scenarios.upstream();
	}

	/**
	 * @param onlyConfigured whether to include all available paths or only ones
	 *                       currently configured for execution
	 * @return calculated amount of possible execution paths of underlying scenarios
	 */
	@Override
	public int combinations(boolean onlyConfigured) {
		int c = 0;
		for (Pair<Scenario, Double> s : this.scenarios.get())
			c = c + s.getKey().combinations(onlyConfigured);
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
	@Override
	public void execute() {
		super.execute();
		action();
		if (!this.halted)
			this.scenarios.execute();
		else
			this.halted = false;
		finals();
		done();
	}

	/**
	 * Avoid starting of sub-scenarios.
	 */
	public void halt() {
		this.halted = true;
	}

	/**
	 * Returns the set with names of all underlying scenarios
	 *
	 * @return set of scenarios
	 */
	@Override
	public Set<Scenario> list() {
		Set<Scenario> c = super.list();
		c.addAll(this.scenarios.list());
		return c;
	}

	/**
	 * @return whether this scenario meets it's prerequisites
	 */
	@Override
	public boolean sufficed() {
		return false;
	}

	/**
	 * @return calculated pervasive weight of this scenario
	 */
	@Override
	public double upstream() {
		return this.uWeight;
	}
}
