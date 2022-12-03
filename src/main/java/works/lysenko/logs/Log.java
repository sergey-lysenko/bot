package works.lysenko.logs;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "AssignmentToSuperclassField", "ParameterHidesMemberVariable", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependents", "RedundantMethodOverride", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class Log extends AbstractLogData {

    private final int depth;
    private final int level;

    /**
     * @param depth in the output log
     * @param level of a message
     * @param text  describing the problem
     */
    @SuppressWarnings("PublicConstructor")
    public Log(int depth, int level, String text) {
        this.depth = depth;
        this.level = level;
        this.text = text;
    }

    /**
     * @return depth in the output log
     */
    @SuppressWarnings("unused")
    public int depth() {
        return depth;
    }

    /**
     * @return level
     */
    @SuppressWarnings("unused")
    public int level() {
        return level;
    }

    @Override
    public String render() {
        return LogData.renderLog(depth, level, text);
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public String toString() {
        return c("Log [depth=", depth, ", level=", level, ", text=", text, "]");
    }

}
