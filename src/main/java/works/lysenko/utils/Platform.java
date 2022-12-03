package works.lysenko.utils;

/**
 * Supported platforms
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "PublicMethodWithoutLogging", "ClassIndependentOfModule"})
public enum Platform {
    @SuppressWarnings("DuplicateStringLiteralInspection") CHROME("Chrome"), @SuppressWarnings("DuplicateStringLiteralInspection") FIREFOX("Firefox"), @SuppressWarnings("DuplicateStringLiteralInspection") OPERA("Opera"), EDGE("Edge"), @SuppressWarnings("DuplicateStringLiteralInspection") ANDROID("Android"), @SuppressWarnings("DuplicateStringLiteralInspection") SAFARI("Safari");

    private final String name;

    @SuppressWarnings("unused")
    Platform(String name) {
        this.name = name;
    }

    @SuppressWarnings({"unused", "SwitchStatement", "QuestionableName", "MethodWithMultipleReturnPoints", "DuplicateStringLiteralInspection"})
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
            case "Android":
                return ANDROID;
            default:
                return CHROME;
        }
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }
}
