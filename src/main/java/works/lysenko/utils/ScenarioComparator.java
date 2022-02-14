package works.lysenko.utils;

import java.util.Comparator;

import works.lysenko.scenarios.Scenario;

@SuppressWarnings("javadoc")
public class ScenarioComparator implements Comparator<Scenario> {

	public ScenarioComparator() {
		super();
	}

	public int compare(Scenario s1, Scenario s2) {
		return s1.name().compareTo(s2.name());
	}
}
