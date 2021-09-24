package works.lysenko.utils;

@SuppressWarnings("javadoc")
public enum Severity {

	S1("[SEVERE]", Ansi.RED), S2("[WARNING]", Ansi.YELLOW), S3("[NOTICE]", Ansi.CYAN), SK("[KNOWN-ISSUE]", Ansi.MAGENTA);

	private final String tag;
	private final Ansi color;

	Severity(String tag, Ansi color) {
		this.tag = tag;
		this.color = color;
	}

	public static Severity byColor(Ansi c) {
		for (Severity e : Severity.values())
			if (e.color().equals(c))
				return e;
		return null;
	}

	public static Severity byCode(String v) {
		for (Severity e : Severity.values())
			if (e.tag().equals(v))
				return e;
		return null;
	}

	public String tag() {
		return tag;
	}

	public Ansi color() {
		return color;
	}
}