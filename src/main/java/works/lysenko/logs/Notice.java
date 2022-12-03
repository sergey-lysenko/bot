package works.lysenko.logs;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"AssignmentToSuperclassField", "ClassTooDeepInInheritanceTree", "ParameterHidesMemberVariable", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependents", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class Notice extends AbstractProblemLogData {

    /**
     * @param depth in the output log
     * @param text  describing the problem
     */
    @SuppressWarnings({"PublicConstructor", "DuplicateStringLiteralInspection"})
    public Notice(int depth, String text) {
        super(depth, text);
        tag = "[NOTICE]";
    }

    @Override
    public String toString() {
        return c("Notice [depth=", depth, ", tag=", tag, ", text=", text, "]");
    }

}
