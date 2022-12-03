package works.lysenko.logs;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"AssignmentToSuperclassField", "ClassTooDeepInInheritanceTree", "ParameterHidesMemberVariable", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependents", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class Severe extends AbstractProblemLogData {

    /**
     * @param depth in the output log
     * @param text  describing the problem
     */
    @SuppressWarnings({"PublicConstructor", "DuplicateStringLiteralInspection"})
    public Severe(int depth, String text) {
        super(depth, text);
        tag = "[SEVERE]";
    }

    @SuppressWarnings("DuplicateStringLiteralInspection")
    @Override
    public String toString() {
        return c("Severe [depth=", depth, ", tag=", tag, ", text=", text, "]");
    }

}
