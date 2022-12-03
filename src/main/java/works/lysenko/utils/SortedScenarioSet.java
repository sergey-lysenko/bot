package works.lysenko.utils;

import works.lysenko.scenarios.Scenario;

import java.util.TreeSet;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"SerializableHasSerializationMethods", "CloneableClassInSecureContext", "ClassWithoutLogger", "ClassWithTooManyTransitiveDependents", "ClassExtendsConcreteCollection"})
public class SortedScenarioSet extends TreeSet<Scenario> {

    private static final long serialVersionUID = -8268293097575167966L;

    /**
     * {@link java.util.TreeSet} of {@link works.lysenko.scenarios.Scenario} objects
     * sorted by {@link works.lysenko.scenarios.Scenario#name()} ignoring the
     * character case. This is used in various
     * {@link works.lysenko.scenarios.AbstractScenario} implementations
     */
    @SuppressWarnings("PublicConstructor")
    public SortedScenarioSet() {
        super(new IgnoreCaseScenarioComparator());
    }
}
