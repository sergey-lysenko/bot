package works.lysenko.utils;

public enum Severity {

	S1("[SEVERE]", Color.RED), S2("[WARNING]", Color.YELLOW), S3("[NOTICE]", Color.CYAN);

	private final String tag;
	private final Color color;

	Severity(String tag, Color color) {
		this.tag = tag;
		this.color = color;
	}

	public static Severity byColor(Color c) {
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

	public Color color() {
		return color;
	}
}