package works.lysenko.utils;

/**
 * Supported platforms
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings("javadoc")
public enum Platform {
	CHROME("Chrome"), FIREFOX("Firefox"), OPERA("Opera"), EDGE("Edge"), ANDROID("Android"), SAFARI("Safari"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	public static Platform get(String string) {
		switch (string) {
		case "Firefox": //$NON-NLS-1$
			return FIREFOX;
		case "Opera": //$NON-NLS-1$
			return OPERA;
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

	private String name;

	Platform(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
