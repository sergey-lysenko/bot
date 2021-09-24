package works.lysenko.utils;

import java.util.TreeSet;

@SuppressWarnings("javadoc")
public class SortedStringSet extends TreeSet<String> {

	private static final long serialVersionUID = -8268293097575167966L;

	public SortedStringSet() {
		super((new IgnoreCaseStringComparator()));
	}
}
