package works.lysenko.utils;

import java.util.Comparator;

/**
 * @author Sergii Lysenko
 */
public class IgnoreCaseStringComparator implements Comparator<String> {

	/**
	 * {@link java.util.Comparator} for sorting of Strings ignoring the character
	 * case
	 */
	public IgnoreCaseStringComparator() {
		super();
	}

	@Override
	public int compare(String s1, String s2) {
		return s1.compareToIgnoreCase(s2);
	}
}
