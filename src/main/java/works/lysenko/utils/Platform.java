package works.lysenko.utils;

/**
 * Supported platforms
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings("javadoc")
public enum Platform {
	CHROME("Chrome"), FIREFOX("Firefox"), OPERA("Opera"), EDGE("Edge"), ANDROID("Android"), SAFARI("Safari");

	public static Platform get(String string) {
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
			return CHROME;
		case "Android":
			return ANDROID;
		default:
			return CHROME;
		}
	}

	private String name;

	Platform(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
