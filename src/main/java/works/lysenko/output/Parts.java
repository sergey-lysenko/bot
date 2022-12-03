package works.lysenko.output;

import works.lysenko.Result;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithTooManyTransitiveDependents", "WeakerAccess", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class Parts {
    /**
     *
     */
    public final SortedMap<String, Result> head;
    /**
     *
     */
    public final SortedMap<String, Result> body;

    /**
     *
     */
    @SuppressWarnings({"unused", "PublicConstructor"})
    public Parts() {
        head = new TreeMap<>();
        body = new TreeMap<>();
    }

    /**
     * @param head of results tree
     * @param body of results tree
     */
    @SuppressWarnings({"PublicConstructor", "AssignmentOrReturnOfFieldWithMutableType"})
    public Parts(SortedMap<String, Result> head, SortedMap<String, Result> body) {
        this.head = head;
        this.body = body;
    }

    /**
     * @param source to process
     * @return Parts
     */
    @SuppressWarnings({"TypeMayBeWeakened", "UseOfConcreteClass", "ChainedMethodCall"})
    public static Parts process(SortedMap<String, Result> source) {
        TreeMap<String, Result> head = new TreeMap<>();
        TreeMap<String, Result> body = new TreeMap<>();
        for (Map.Entry<String, Result> e : source.entrySet())
            if (Character.isUpperCase(e.getKey().charAt(0)))
                head.put(e.getKey(), e.getValue());
            else
                body.put(e.getKey(), e.getValue());
        return new Parts(head, body);
    }

}
