package works.lysenko.utils;

import java.util.Comparator;

import works.lysenko.scenarios.Scenario;

/**
 * @author Sergii Lysenko
 */
public class ScenarioComparator implements Comparator<Scenario> {

	/**
	 * {@link java.util.Comparator} for sorting of
	 * {@link works.lysenko.scenarios.Scenario} objects by
	 * {@link works.lysenko.scenarios.Scenario#name()}
	 */
	public ScenarioComparator() {
		super();
	}

	@Override
	public int compare(Scenario s1, Scenario s2) {
		return s1.name().compareTo(s2.name());
	}
}
