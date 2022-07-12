package works.lysenko.utils;

import java.util.TreeSet;

/**
 * @author Sergii Lysenko
 */
public class SortedStringSet extends TreeSet<String> {

	private static final long serialVersionUID = -8268293097575167966L;

	/**
	 * {@link java.util.TreeSet} of sorted {@link java.lang.String} objects. This is
	 * used in {@link works.lysenko.Cycles} implementation
	 */
	public SortedStringSet() {
		super(new IgnoreCaseStringComparator());
	}
}
