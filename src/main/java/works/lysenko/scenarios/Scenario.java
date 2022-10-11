package works.lysenko.scenarios;

import java.util.Set;

import works.lysenko.enums.ScenarioType;

/**
 * @author Sergii Lysenko
 */
public interface Scenario {

	/**
	 * Container for logic of a scenario. This code executed before selecting one of
	 * nested scenarios, if there are any.
	 */
	public abstract void action();

	/**
	 * This routine reports an amount of possible variants of executions for
	 * underlying scenarios
	 *
	 * @param onlyConfigured or all possible scenarios from code base
	 * @return number of possible combinations for underlying scenarios
	 */
	public abstract int combinations(boolean onlyConfigured);

	/**
	 * @return Current downstream weight of this Scenario
	 */
	public abstract double downstream();

	/**
	 * Redefine downstream weight of this Scenario
	 * 
	 * @param w downstream weight to set
	 */
	public abstract void downstream(double w);

	/**
	 * @return whether this scenario is able to be executed
	 */
	public abstract boolean executable();

	/**
	 * Container for logic of a scenario, which is to be executed AFTER nested
	 * scenarios execution.
	 */
	public abstract void finals();

	/**
	 * @return list of nested scenarios
	 */
	public abstract Set<Scenario> list();

	/**
	 * @return unique name of this Scenario
	 */
	public abstract String name();

	/**
	 * @return shortened name of this Scenario
	 */
	public abstract String shortName();

	/**
	 * This should be redefined and contain the code to indicate readiness of a
	 * Scenario to be executed
	 * 
	 * @return whether this scenario meets it's prerequisites
	 */
	public abstract boolean sufficed();

	/**
	 * @return {@link works.lysenko.enums.ScenarioType} of Scenario
	 */
	public abstract ScenarioType type();

	/**
	 * @return current upstream weight of this Scenario
	 */
	public abstract double upstream();

	/**
	 * @return own weight of this Scenario
	 */
	public abstract Double weight();

}