package works.lysenko.enums;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "PublicMethodWithoutLogging", "ClassWithTooManyTransitiveDependents"})
public enum Platform {
    /**
     * Chrome
     */
    @SuppressWarnings("DuplicateStringLiteralInspection") CHROME("Chrome"),

    /**
     * Firefox
     */
    @SuppressWarnings("DuplicateStringLiteralInspection") FIREFOX("Firefox"),

    /**
     * Edge
     */
    EDGE("Edge"),

    /**
     * Android
     */
    @SuppressWarnings("DuplicateStringLiteralInspection") ANDROID("Android"),

    /**
     * Safari
     */
    @SuppressWarnings("DuplicateStringLiteralInspection") SAFARI("Safari");

    private final String title;

    Platform(String title) {
        this.title = title;
    }

    /**
     * @param string to search by
     * @return {@link Platform} by {@link String} value
     */
    @SuppressWarnings({"SwitchStatement", "QuestionableName", "UnnecessaryJavaDocLink", "MethodWithMultipleReturnPoints", "DuplicateStringLiteralInspection"})
    public static Platform get(String string) {
        switch (string) {
            case "Firefox":
                return FIREFOX;
            case "Edge":
                return EDGE;
            case "Safari":
                return SAFARI;
            case "Android":
                return ANDROID;
            default:
                return CHROME;
        }
    }

    /**
     * @return title of this Platform
     */
    public String title() {
        return title;
    }
}
