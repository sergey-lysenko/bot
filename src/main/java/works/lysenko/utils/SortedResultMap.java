package works.lysenko.utils;

import java.util.TreeMap;

import works.lysenko.Result;
import works.lysenko.scenarios.Scenario;

@SuppressWarnings("javadoc")
public class SortedResultMap extends TreeMap<Scenario, Result> {

	private static final long serialVersionUID = -8268293097575167966L;

	public SortedResultMap() {
		super((new ScenarioComparator()));
	}
}
