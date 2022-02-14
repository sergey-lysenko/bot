package works.lysenko.utils;

import java.util.Comparator;

import works.lysenko.scenarios.Scenario;

@SuppressWarnings("javadoc")
public class IgnoreCaseScenarioComparator implements Comparator<Scenario> {

	public IgnoreCaseScenarioComparator() {
		super();
	}

	public int compare(Scenario s1, Scenario s2) {
		return s1.name().compareToIgnoreCase(s2.name());
	}
}
