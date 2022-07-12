package works.lysenko.scenarios;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.utils.SortedScenarioSet;

/**
 * @author Sergii Lysenko
 */
public class AbstractMonoScenario extends AbstractScenario {

	private boolean executed = false;

	/**
	 * @param x instance of Execution object
	 */
	public AbstractMonoScenario(Execution x) {
		super(x);
	}

	/**
	 * @return whether this scenario still runnable or it had been executed already
	 */
	@Override
	public boolean executable() {
		return !this.executed && super.executable();
	}

	/**
	 * For a Mono Scenario, execution consist of
	 *
	 * 1) default execution code defined in super class
	 * 2) logic defined in action()
	 * 3) final steps defined in finals() of this scenario
	 */
	@Override
	public final void execute() {
		super.execute();
		action();
		finals();
		done();
		this.executed = true;
	}

	/**
	 * Returns the set with one element - name of the scenario in requested format
	 *
	 * @return set object with this scenario as single element
	 */
	@Override
	public Set<Scenario> list() {
		Set<Scenario> c = new SortedScenarioSet();
		c.add(this);
		return c;
	}
}