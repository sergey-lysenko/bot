package works.lysenko.utils;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings("javadoc")
public enum Browser {
	CHROME("Chrome"), FIREFOX("Firefox"), OPERA("Opera"), EDGE("Edge"),
	SAFARI("Safari");

	private String name;

	Browser(String name) {
		this.name = name;
	}

	public static Browser get(String string) {
		switch (string) {
		case "Firefox":
			return FIREFOX;
		case "Opera":
			return OPERA;
		case "Edge":
			return EDGE;
		case "Safari":
			return SAFARI;
		case "Chrome":
		default:
			return CHROME;
		}
	}

	public String getName() {
		return name;
	}
}
