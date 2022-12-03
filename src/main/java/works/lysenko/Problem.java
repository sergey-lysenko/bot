package works.lysenko;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependents", "WeakerAccess", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class Problem {
    /**
     * Timestamp of the problem occurrence
     */
    public final long time;

    /**
     * Description of the problem
     */
    public final String text;

    /**
     * @param m time
     * @param s description
     */
    @SuppressWarnings({"PublicConstructor", "StandardVariableNames"})
    public Problem(long m, String s) {
        time = m;
        text = s;
    }

    @Override
    public String toString() {
        return c("(", time, ") ", text);
    }
}
