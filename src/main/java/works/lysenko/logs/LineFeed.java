package works.lysenko.logs;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"AssignmentToSuperclassField", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithTooManyTransitiveDependents", "RedundantMethodOverride", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class LineFeed extends AbstractLogData {

    /**
     * Line Feed
     */
    @SuppressWarnings("PublicConstructor")
    public LineFeed() {
        text = "";
    }

    @Override
    public String render() {
        return text;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public String toString() {
        return "LineFeed";
    }

}
