package works.lysenko.logs;

import works.lysenko.enums.Ansi;
import works.lysenko.enums.Severity;

import java.util.Objects;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"AssignmentToSuperclassField", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependents", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public abstract class AbstractProblemLogData extends AbstractLogData {

    @SuppressWarnings("PackageVisibleField")
    final int depth;
    @SuppressWarnings("PackageVisibleField")
    String tag = null;

    /**
     * @param depth of message shift in log
     * @param text  of message
     */
    AbstractProblemLogData(int depth, String text) {
        this.depth = depth;
        this.text = text;
    }

    /**
     * @return default text representation of abstract Log Data
     */
    @SuppressWarnings("ChainedMethodCall")
    @Override
    public String render() {
        return LogData.renderLog(depth, 0, Ansi.colorize(text, Objects.requireNonNull(Severity.byCode(tag)).color()));
    }

}
