package works.lysenko.scenarios;

import static works.lysenko.Constants.MONO_SCENARIO_MARKER;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.utils.SortedStringSet;

public class AbstractMonoScenario extends AbstractScenario {

	private boolean executed = false;

	public AbstractMonoScenario(Execution x) {
		super(x);
		marker(MONO_SCENARIO_MARKER);
	}

	/**
	 * @return whether this scenario still runnable or it had been executed already
	 */
	public boolean executable() {
		return !executed;
	}

	/**
	 * For a Mono Scenario, execution consist of
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
		executed = true;
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