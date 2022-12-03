package works.lysenko.utils;

import works.lysenko.Result;
import works.lysenko.scenarios.Scenario;

import java.util.TreeMap;

@SuppressWarnings({"SerializableHasSerializationMethods", "CloneableClassInSecureContext", "ClassWithoutLogger", "ClassWithTooManyTransitiveDependents", "ClassExtendsConcreteCollection", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class SortedResultMap extends TreeMap<Scenario, Result> {

    private static final long serialVersionUID = -8268293097575167966L;

    @SuppressWarnings("PublicConstructor")
    public SortedResultMap() {
        super((new ScenarioComparator()));
    }
}
