package works.lysenko.enums;

/**
 * Severity of indicated problems
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "ReturnOfNull", "PublicMethodWithoutLogging", "ClassWithTooManyDependents", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "ClassWithTooManyTransitiveDependencies"})
public enum Severity {

    /**
     * Severe
     */
    @SuppressWarnings("DuplicateStringLiteralInspection") S1("[SEVERE]", Ansi.RED),

    /**
     * Warning
     */
    @SuppressWarnings("DuplicateStringLiteralInspection") S2("[WARNING]", Ansi.YELLOW),

    /**
     * Notice
     */
    @SuppressWarnings("DuplicateStringLiteralInspection") S3("[NOTICE]", Ansi.CYAN),

    /**
     * Known Issue
     */
    @SuppressWarnings("DuplicateStringLiteralInspection") SK("[KNOWN-ISSUE]", Ansi.MAGENTA);

    private final String tag;
    private final Ansi color;

    Severity(String tag, Ansi color) {
        this.tag = tag;
        this.color = color;
    }

    /**
     * @param s Code of Severity
     * @return Severity for this Code
     */
    @SuppressWarnings({"ChainedMethodCall", "ReturnOfNull", "MethodWithMultipleReturnPoints", "CallToSuspiciousStringMethod"})
    public static Severity byCode(String s) {
        for (Severity e : values())
            if (e.tag().equals(s))
                return e;
        return null;
    }

    /**
     * @param ansi Color of Severity
     * @return Severity for this Color
     */
    @SuppressWarnings({"unused", "ReturnOfNull", "MethodWithMultipleReturnPoints"})
    public static Severity byColor(Ansi ansi) {
        for (Severity e : values())
            if (e.color() == ansi)
                return e;
        return null;
    }

    /**
     * @return Color of this Severity
     */
    public Ansi color() {
        return color;
    }

    /**
     * @return Tag of this Severity
     */
    public String tag() {
        return tag;
    }
}
