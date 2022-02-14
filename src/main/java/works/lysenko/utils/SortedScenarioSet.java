package works.lysenko.utils;

import java.util.TreeSet;

import works.lysenko.scenarios.Scenario;

@SuppressWarnings("javadoc")
public class SortedScenarioSet extends TreeSet<Scenario> {

	private static final long serialVersionUID = -8268293097575167966L;

	public SortedScenarioSet() {
		super((new IgnoreCaseScenarioComparator()));
	}
}
