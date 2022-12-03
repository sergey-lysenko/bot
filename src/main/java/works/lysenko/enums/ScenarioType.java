package works.lysenko.enums;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "PublicMethodWithoutLogging", "ClassWithTooManyTransitiveDependents"})
public enum ScenarioType {

    /**
     * Leaf
     */
    LEAF("◆"),

    /**
     * Node
     */
    NODE("▷"),

    /**
     * Mono
     */
    MONO("◼");

    private final String tag;

    ScenarioType(String tag) {
        this.tag = tag;
    }

    /**
     * @return tag of this Type
     */
    public String tag() {
        return tag;
    }
}
