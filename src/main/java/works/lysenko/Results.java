package works.lysenko;

import works.lysenko.logs.LogRecord;
import works.lysenko.scenarios.Scenario;
import works.lysenko.utils.SortedResultMap;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static works.lysenko.Common.c;

/**
 * This class represent results of single bot execution
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "ChainedMethodCall", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "MethodWithMultipleLoops"})
public class Results {

    /**
     *
     */
    @SuppressWarnings({"UseOfConcreteClass", "WeakerAccess"})
    public final Execution x;
    private final Map<Scenario, Result> results;

    /**
     * @param x instance of {@link Execution} object
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor", "CollectionWithoutInitialCapacity"})
    public Results(Execution x) {
        this.x = x;
        results = new HashMap<>();
    }

    @SuppressWarnings({"ChainedMethodCall", "LawOfDemeter"})
    private static String tagged(Scenario scenario) {
        return c(scenario.shortName(), " ", scenario.type().tag());
    }

    /**
     * Count in test execution (in later versions there will be collection of more
     * execution data than just times of execution)
     *
     * @param scenario scenario to count
     * @return copy of added test execution data
     */
    @SuppressWarnings("UseOfConcreteClass")
    public Result count(Scenario scenario) {
        Result r = results.getOrDefault(scenario, new Result(scenario));
        ++r.executions;
        results.put(scenario, r);
        return r;
    }

    /**
     * Sort scenario classes ignoring the case which produces more "tree-like" order
     * of items and improves readability, excluding not executed scenarios
     *
     * @return execution counters sorted properly
     */
    @SuppressWarnings("WeakerAccess")
    protected SortedMap<Scenario, Result> getSorted() {
        SortedMap<Scenario, Result> sorted = new SortedResultMap();
        sorted.putAll(results);
        return sorted;
    }

    /**
     * Sort scenario classes ignoring the case which produces more "tree-like" order
     * of items and improves readability, including not executed scenarios
     *
     * @return execution counters sorted properly
     */
    @SuppressWarnings({"ObjectAllocationInLoop", "LawOfDemeter"})
    public TreeMap<String, Result> getSortedStrings() {
        TreeMap<String, Result> sorted = new TreeMap<>();
        // Adding results of executed scenarios
        for (Map.Entry<Scenario, Result> r : results.entrySet())
            sorted.put(tagged(r.getKey()), r.getValue());
        // Adding result stubs of non-executed scenarios
        for (Scenario scenario : x.cycles.scenariosList())
            if (!sorted.containsKey(tagged(scenario))) sorted.put(tagged(scenario), new Result(scenario));
        return sorted;
    }

    /**
     * This routine stores the information about a problem in the execution results
     */
    @SuppressWarnings({"UseOfConcreteClass", "AutoUnboxing", "WeakerAccess"})
    protected void problem(LogRecord lr) {
        if (null != x.currentScenario())
            results.get(x.currentScenario()).problems.add(new Problem(lr.time(), lr.text()));
    }
}
