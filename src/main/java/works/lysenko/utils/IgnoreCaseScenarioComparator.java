package works.lysenko.utils;

import java.util.Comparator;

import works.lysenko.scenarios.Scenario;

/**
 * @author Sergii Lysenko
 */
public class IgnoreCaseScenarioComparator implements Comparator<Scenario> {

	/**
	 * {@link java.util.Comparator} for sorting of
	 * {@link works.lysenko.scenarios.Scenario} objects by
	 * {@link works.lysenko.scenarios.Scenario#name()} ignoring the character case
	 */
	public IgnoreCaseScenarioComparator() {
		super();
	}

	@Override
	public int compare(Scenario s1, Scenario s2) {
		return s1.name().compareToIgnoreCase(s2.name());
	}
}
