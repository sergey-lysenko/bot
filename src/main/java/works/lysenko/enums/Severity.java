package works.lysenko.enums;

/**
 * Severity of indicated problems
 * 
 * @author Sergii Lysenko
 */
public enum Severity {

	/**
	 * Severe
	 */
	S1("[SEVERE]", Ansi.RED),

	/**
	 * Warning
	 */
	S2("[WARNING]", Ansi.YELLOW),

	/**
	 * Notice
	 */
	S3("[NOTICE]", Ansi.CYAN),

	/**
	 * Known Issue
	 */
	SK("[KNOWN-ISSUE]", Ansi.MAGENTA);

	/**
	 * @param s Code of Severity
	 * @return Severity for this Code
	 */
	public static Severity byCode(String s) {
		for (Severity e : Severity.values())
			if (e.tag().equals(s))
				return e;
		return null;
	}

	/**
	 * @param c Color of Severity
	 * @return Severity for this Color
	 */
	public static Severity byColor(Ansi c) {
		for (Severity e : Severity.values())
			if (e.color().equals(c))
				return e;
		return null;
	}

	private final String tag;

	private final Ansi color;

	Severity(String tag, Ansi color) {
		this.tag = tag;
		this.color = color;
	}

	/**
	 * @return Color of this Severity
	 */
	public Ansi color() {
		return color;
	}

	/**
	 * @return Tag of this Severity
	 */
	public String tag() {
		return tag;
	}
}