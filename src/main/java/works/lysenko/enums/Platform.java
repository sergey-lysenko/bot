package works.lysenko.enums;

/**
 * @author Sergii Lysenko
 */
public enum Platform {
	/**
	 * Chrome
	 */
	CHROME("Chrome"),

	/**
	 * Firefox
	 */
	FIREFOX("Firefox"),

	/**
	 * Edge
	 */
	EDGE("Edge"),

	/**
	 * Android
	 */
	ANDROID("Android"),

	/**
	 * Safari
	 */
	SAFARI("Safari");

	/**
	 * @param s
	 * @return Platform by String
	 */
	public static Platform get(String s) {
		switch (s) {
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

	private String title;

	Platform(String title) {
		this.title = title;
	}

	/**
	 * @return title of this Platform
	 */
	public String title() {
		return this.title;
	}
}
