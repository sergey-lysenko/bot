package works.lysenko.logs;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"AssignmentToSuperclassField", "ClassTooDeepInInheritanceTree", "ParameterHidesMemberVariable", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependents", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class KnownIssue extends AbstractProblemLogData {

    /**
     * @param depth in the output log
     * @param text  describing the problem
     */
    @SuppressWarnings({"PublicConstructor", "DuplicateStringLiteralInspection"})
    public KnownIssue(int depth, String text) {
        super(depth, text);
        tag = "[KNOWN-ISSUE]";
    }

    @Override
    public String toString() {
        return c("Known Issue [depth=", depth, ", tag=", tag, ", text=", text, "]");
    }

}
