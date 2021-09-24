package works.lysenko.utils;

import java.util.TreeMap;

import works.lysenko.Result;

@SuppressWarnings("javadoc")
public class SortedResultMap extends TreeMap<String, Result> {

	private static final long serialVersionUID = -8268293097575167966L;

	public SortedResultMap() {
		super((new IgnoreCaseStringComparator()));
	}
}
