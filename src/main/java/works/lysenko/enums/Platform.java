package works.lysenko.enums;

/**
 * @author Sergii Lysenko
 */
public enum Platform {
	/**
	 * Chrome
	 */
	CHROME("Chrome"), //$NON-NLS-1$

	/**
	 * Firefox
	 */
	FIREFOX("Firefox"), //$NON-NLS-1$

	/**
	 * Edge
	 */
	EDGE("Edge"), //$NON-NLS-1$

	/**
	 * Android
	 */
	ANDROID("Android"), //$NON-NLS-1$

	/**
	 * Safari
	 */
	SAFARI("Safari"); //$NON-NLS-1$

	/**
	 * @param s
	 * @return Platform by String
	 */
	public static Platform get(String s) {
		switch (s) {
		case "Firefox": //$NON-NLS-1$
			return FIREFOX;
		case "Edge": //$NON-NLS-1$
			return EDGE;
		case "Safari": //$NON-NLS-1$
			return SAFARI;
		case "Chrome": //$NON-NLS-1$
			return CHROME;
		case "Android": //$NON-NLS-1$
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
