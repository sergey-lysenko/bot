package works.lysenko.enums;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "PublicMethodWithoutLogging", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "ClassWithTooManyTransitiveDependencies"})
public enum Ansi {
    RESET("\033[0m"),

    BLACK("\033[0;30m"), RED("\033[0;31m"), GREEN("\033[0;32m"), YELLOW("\033[0;33m"), @SuppressWarnings("unused") BLUE("\033[0;34m"), MAGENTA("\033[0;35m"), CYAN("\033[0;36m"), @SuppressWarnings("unused") WHITE("\033[0;37m"),

    @SuppressWarnings("unused") BLACK_BOLD("\033[1;30m"), @SuppressWarnings("unused") RED_BOLD("\033[1;31m"), @SuppressWarnings("unused") GREEN_BOLD("\033[1;32m"), YELLOW_BOLD("\033[1;33m"), @SuppressWarnings("unused") BLUE_BOLD("\033[1;34m"), @SuppressWarnings("unused") MAGENTA_BOLD("\033[1;35m"), @SuppressWarnings("unused") CYAN_BOLD("\033[1;36m"), @SuppressWarnings("unused") WHITE_BOLD("\033[1;37m"), //NON-NLS

    @SuppressWarnings("unused") BLACK_UNDERLINED("\033[4;30m"), @SuppressWarnings("unused") RED_UNDERLINED("\033[4;31m"), @SuppressWarnings("unused") GREEN_UNDERLINED("\033[4;32m"), @SuppressWarnings("unused") YELLOW_UNDERLINED("\033[4;33m"), @SuppressWarnings("unused") BLUE_UNDERLINED("\033[4;34m"), @SuppressWarnings("unused") MAGENTA_UNDERLINED("\033[4;35m"), @SuppressWarnings("unused") CYAN_UNDERLINED("\033[4;36m"), @SuppressWarnings("unused") WHITE_UNDERLINED("\033[4;37m"),

    @SuppressWarnings("unused") BLACK_BACKGROUND("\033[40m"), RED_BACKGROUND("\033[41m"), GREEN_BACKGROUND("\033[42m"), @SuppressWarnings("unused") YELLOW_BACKGROUND("\033[43m"), @SuppressWarnings("unused") BLUE_BACKGROUND("\033[44m"), @SuppressWarnings("unused") MAGENTA_BACKGROUND("\033[45m"), @SuppressWarnings("unused") CYAN_BACKGROUND("\033[46m"), @SuppressWarnings("unused") WHITE_BACKGROUND("\033[47m"),

    @SuppressWarnings("unused") BLACK_BRIGHT("\033[0;90m"), @SuppressWarnings("unused") RED_BRIGHT("\033[0;91m"), @SuppressWarnings("unused") GREEN_BRIGHT("\033[0;92m"), @SuppressWarnings("unused") YELLOW_BRIGHT("\033[0;93m"), @SuppressWarnings("unused") BLUE_BRIGHT("\033[0;94m"), @SuppressWarnings("unused") MAGENTA_BRIGHT("\033[0;95m"), @SuppressWarnings("unused") CYAN_BRIGHT("\033[0;96m"), @SuppressWarnings("unused") WHITE_BRIGHT("\033[0;97m"),

    @SuppressWarnings("unused") BLACK_BOLD_BRIGHT("\033[1;90m"), @SuppressWarnings("unused") RED_BOLD_BRIGHT("\033[1;91m"), @SuppressWarnings("unused") GREEN_BOLD_BRIGHT("\033[1;92m"), @SuppressWarnings("unused") YELLOW_BOLD_BRIGHT("\033[1;93m"), BLUE_BOLD_BRIGHT("\033[1;94m"), @SuppressWarnings("unused") MAGENTA_BOLD_BRIGHT("\033[1;95m"), @SuppressWarnings("unused") CYAN_BOLD_BRIGHT("\033[1;96m"), WHITE_BOLD_BRIGHT("\033[1;97m"),

    @SuppressWarnings("unused") BLACK_BACKGROUND_BRIGHT("\033[0;100m"), @SuppressWarnings("unused") RED_BACKGROUND_BRIGHT("\033[0;101m"), @SuppressWarnings("unused") GREEN_BACKGROUND_BRIGHT("\033[0;102m"), @SuppressWarnings("unused") YELLOW_BACKGROUND_BRIGHT("\033[0;103m"), @SuppressWarnings("unused") BLUE_BACKGROUND_BRIGHT("\033[0;104m"), @SuppressWarnings("unused") MAGENTA_BACKGROUND_BRIGHT("\033[0;105m"), @SuppressWarnings("unused") CYAN_BACKGROUND_BRIGHT("\033[0;106m"), @SuppressWarnings("unused") WHITE_BACKGROUND_BRIGHT("\033[0;107m");

    private final String code;

    Ansi(String code) {
        this.code = code;
    }

    /**
     * Execute {@link Ansi#colorize(String, Ansi)} based on content of the provided string.
     * Contents - to - color mapping defined in {@link works.lysenko.enums.Severity}
     *
     * @param s string to be colored
     * @return same string with added ANSI coloring escape sequences
     */
    @SuppressWarnings({"FeatureEnvy", "MethodWithMultipleReturnPoints"})
    public static String colorize(String s) {
        for (Severity e : Severity.values())
            if (s.contains(e.tag())) return colorize(s, e.color());
        return s;
    }

    /**
     * Wrap a sting into ANSI escape sequence of defined color
     *
     * @param s    string to be colored
     * @param ansi color escape sequence to be applied
     * @return string surrounded by defined color sequence and RESET
     */
    public static String colorize(String s, Ansi ansi) {
        return null == ansi ? s : c(ansi.code() , s , RESET.code());
    }

    public static String y(Object o) {
        return colorize(String.valueOf(o), YELLOW_BOLD);
    }

    /**
     * @return ANSI code for color
     */
    private String code() {
        return code;
    }

}
