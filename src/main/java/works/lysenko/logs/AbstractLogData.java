package works.lysenko.logs;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutConstructor", "ClassWithTooManyTransitiveDependents", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public abstract class AbstractLogData implements LogData {

    @SuppressWarnings("PackageVisibleField")
    String text = null;

    /**
     * @return value of text field
     */
    public String text() {
        return text;
    }
}
