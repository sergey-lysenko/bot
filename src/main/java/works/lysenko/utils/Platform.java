package works.lysenko.utils;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings("javadoc")
public enum Platform {
	CHROME("Chrome"), FIREFOX("Firefox"), OPERA("Opera"), EDGE("Edge"), ANDROID("Android"), SAFARI("Safari");

	private String name;

	Platform(String name) {
		this.name = name;
	}

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

	public String getName() {
		return name;
	}
}
