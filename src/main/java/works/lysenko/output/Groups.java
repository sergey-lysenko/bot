package works.lysenko.output;

import works.lysenko.Result;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"SerializableHasSerializationMethods", "BoundedWildcard", "CloneableClassInSecureContext", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithTooManyTransitiveDependents", "ClassExtendsConcreteCollection", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public final class Groups extends TreeMap<String, TreeMap<String, Result>> {

    private static final long serialVersionUID = -8268293097575167966L;

    /**
     *
     */
    private Groups() {
    }

    /**
     * @param key   of new group
     * @param value of new group
     */
    @SuppressWarnings("PublicConstructor")
    public Groups(String key, TreeMap<String, Result> value) {
        put(key, value);
    }

    /**
     * @param map of groups
     */
    @SuppressWarnings({"unused", "PublicConstructor", "ParameterNameDiffersFromOverriddenParameter"})
    public Groups(SortedMap<String, TreeMap<String, Result>> map) {
        super(map);
    }

    /**
     * @param source to process
     * @return Groups
     */
    @SuppressWarnings({"TypeMayBeWeakened", "UseOfConcreteClass", "ChainedMethodCall", "ObjectAllocationInLoop"})
    public static Groups process(SortedMap<String, Result> source) {
        Groups g = new Groups();
        for (Map.Entry<String, Result> e : source.entrySet()) {
            String groupId = e.getKey().split("\\.")[0];
            String newKey = e.getKey().replace(c(groupId, "."), "");
            Result newValue = e.getValue();
            TreeMap<String, Result> newMap = null == g.get(groupId) ? new TreeMap<>() : g.get(groupId);
            newMap.put(newKey, newValue);
            g.put(groupId, newMap);
        }
        return g;
    }

}
