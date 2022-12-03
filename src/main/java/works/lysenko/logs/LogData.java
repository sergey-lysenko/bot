package works.lysenko.logs;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"InterfaceWithOnlyOneDirectInheritor", "PublicMethodWithoutLogging", "ClassWithTooManyTransitiveDependents", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public interface LogData {

    /**
     * Render log data
     *
     * @param depth  of log (number of added spaces)
     * @param level  of log (number of dots)
     * @param string message
     * @return rendered log message
     */
    @SuppressWarnings("QuestionableName")
    static String renderLog(int depth, int level, String string) {
        return c(" ".repeat(Math.abs(depth)), // logical scenario level
                "•".repeat(Math.abs(level)), // logical message level
                (0 == level ? "" : " "), // optional space
                string);
    }

    /**
     * @return rendered representation of Log Data
     */
    String render();

    /**
     * @return text of log date
     */
    @SuppressWarnings("unused")
    String text();
}
