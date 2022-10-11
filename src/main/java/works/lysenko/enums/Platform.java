package works.lysenko.enums;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings("javadoc")
public enum Platform {
	CHROME("Chrome"), FIREFOX("Firefox"), EDGE("Edge"), ANDROID("Android"), SAFARI("Safari");

	private String title;

	Platform(String title) {
		this.title = title;
	}

	public static Platform get(String string) {
		switch (string) {
		case "Firefox":
			return FIREFOX;
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

	public String title() {
		return title;
	}
}
