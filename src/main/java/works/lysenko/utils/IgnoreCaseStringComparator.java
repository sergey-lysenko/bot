package works.lysenko.utils;

import java.util.Comparator;

@SuppressWarnings("javadoc")
public class IgnoreCaseStringComparator implements Comparator<String> {

	public IgnoreCaseStringComparator() {
		super();
	}

	public int compare(String s1, String s2) {
		return s1.compareToIgnoreCase(s2);
	}
}
