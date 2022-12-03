package works.lysenko.scenarios;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.Pair;
import works.lysenko.Common;
import works.lysenko.Execution;
import works.lysenko.utils.SortedScenarioSet;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import static works.lysenko.Constants.DEFAULT_SUFFICIENCY_RETRIES;
import static works.lysenko.Constants.DEFAULT_WEIGHT;
import static works.lysenko.enums.Severity.S2;
import static works.lysenko.enums.Severity.S3;

/**
 * This class used for managing of nested scenarios of Abstract Node Scenario
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "LocalVariableHidesMemberVariable", "BoundedWildcard", "BreakStatement", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassNamePrefixedWithPackageName", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "MethodWithMultipleLoops"})
public class Scenarios extends Common {

    private final List<Pair<Scenario, Double>> scenarios;
    private final int retries;

    /**
     * Construct new {@link works.lysenko.scenarios.Scenarios} instance
     *
     * @param execution reference to active {@link works.lysenko.Execution} instance
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor", "UnnecessaryJavaDocLink"})
    public Scenarios(Execution execution) {
        this(DEFAULT_SUFFICIENCY_RETRIES, execution);
    }

    /**
     * Construct new {@link works.lysenko.scenarios.Scenarios} instance
     *
     * @param sufficiencyRetries number of retries while trying to select a scenario
     *                           to be executed
     * @param execution          reference to active {@link works.lysenko.Execution}
     *                           instance
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor", "UnnecessaryJavaDocLink", "WeakerAccess"})
    public Scenarios(int sufficiencyRetries, Execution execution) {
        super(execution);
        scenarios = new LinkedList<>();
        retries = sufficiencyRetries;
    }

    @SuppressWarnings("AutoBoxing")
    private static Pair<Scenario, Double> pair(Scenario scenario, double d) {
        return new Pair<>(scenario, d);
    }

    /**
     * @param scenario {@link works.lysenko.scenarios.Scenario} of interest
     * @param x        reference to active {@link works.lysenko.Execution} instance
     * @return weight coefficient for defined Scenario in scope of the Execution
     */
    @SuppressWarnings({"UseOfConcreteClass", "ChainedMethodCall", "AutoBoxing", "AutoUnboxing", "CallToSuspiciousStringMethod"})
    private static double weight(Scenario scenario, Execution x) {
        String propName = StringUtils.removeStart(scenario.getClass().getName(), c(x._root(), "."));
        String propValue = x.prop(propName, DEFAULT_WEIGHT);
        if ("-".equals(propValue)) propValue = "NaN";
        Double d = Double.valueOf(propValue);
        return d.equals(Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : d;
    }

    @SuppressWarnings({"ChainedMethodCall", "AutoUnboxing", "ImplicitNumericConversion", "MethodWithMultipleReturnPoints"})
    private static String list(Iterable<Pair<Scenario, Double>> list, boolean includeZeroWeight) {
        try {
            String qualifiedName = list.iterator().next().getKey().getClass().getName();
            String[] nameParts = qualifiedName.split("\\."); // this is just twice escaped dot symbol
            String scenarioClassName = nameParts[nameParts.length - 1]; // last one
            String packagePart = qualifiedName.replace(scenarioClassName, "");
            // Creating the set of Strings representing nested scenarios
            Collection<String> set = new TreeSet<>();
            list.forEach(pair -> {
                if (0 < pair.getValue() || includeZeroWeight)
                    set.add(pair.getKey().getClass().getName().replace(packagePart, ""));
            });
            return c(packagePart, set);
        } catch (NoSuchElementException e) {
            return "Ø";
        }
    }

    /**
     * Add map of {@link works.lysenko.scenarios.Scenario} and weights defined per
     * Scenario
     *
     * @param map map of Scenarios and their weights
     */
    @SuppressWarnings({"unused", "AutoUnboxing", "StandardVariableNames"})
    public void add(Map<Scenario, Double> map) {
        map.forEach((k, v) -> scenarios.add(pair(k, v)));
    }

    /**
     * Add single {@link works.lysenko.scenarios.Scenario} with defined weight
     *
     * @param scenario Scenario to be added
     * @param d        weight coefficient for this Scenario
     */
    @SuppressWarnings("WeakerAccess")
    public void add(Scenario scenario, double d) {
        scenarios.add(pair(scenario, d));
    }

    /**
     * Add single {@link works.lysenko.scenarios.Scenario} and read its weight from
     * execution configuration file
     *
     * @param scenario Scenario to be added
     * @param x        reference to active {@link works.lysenko.Execution} instance
     */
    @SuppressWarnings({"unused", "UseOfConcreteClass"})
    public void add(Scenario scenario, Execution x) {
        add(scenario, weight(scenario, x));
    }

    /**
     * Add set of {@link works.lysenko.scenarios.Scenario} with same weight
     *
     * @param ss set of Scenarios to be added
     * @param d  [same] weight of all Scenarios of this set
     */
    @SuppressWarnings("unused")
    public void add(Iterable<Scenario> ss, double d) {
        ss.forEach(scenario -> scenarios.add(pair(scenario, d)));
    }

    /**
     * Add set of {@link works.lysenko.scenarios.Scenario} and read their weights
     * from execution configuration file
     *
     * @param ss set of Scenarios to be added
     * @param x  reference to active {@link works.lysenko.Execution} instance
     */
    @SuppressWarnings("UseOfConcreteClass")
    public void add(Iterable<Scenario> ss, Execution x) {
        if (null != ss) ss.forEach(scenario -> scenarios.add(pair(scenario, weight(scenario, x))));
    }

    /**
     * Calculate number of possible execution paths
     *
     * @param onlyConfigured defines whether to include only scenarios configured to
     *                       start, or all available ones
     * @return number of possible execution paths
     */
    @SuppressWarnings({"BooleanParameter", "ChainedMethodCall"})
    public int combinations(boolean onlyConfigured) {
        int combinations = 0;
        for (Pair<Scenario, Double> pair : scenarios)
            combinations = combinations + pair.getKey().combinations(onlyConfigured);
        return combinations;
    }

    @SuppressWarnings("LawOfDemeter")
    private Double downstream(Scenario scenario) {
        return x.downstream(scenario);
    }

    /**
     * Execute one of the defined scenarios.
     * <p>
     * As the first step, a copy of scenarios list is created, which is later
     * referenced as list of candidates. Then,
     * {@link org.apache.commons.math3.distribution.EnumeratedDistribution} is used
     * to choose a scenario based on weight coefficients.
     * <p>
     * If sum of weights of all scenarios is zero, no scenario will be selected and
     * no action will be performed except posting the error message to the test log.
     * <p>
     * Negative weight of any scenario considered as critical exception and leads to
     * stop of the test.
     * <p>
     * Afterwards the {@link works.lysenko.scenarios.Scenario#isSufficed()} method of
     * the selected scenario is called to verify compliance to scenario's
     * prerequisites. In case of false result, warning message posted to the test
     * log and scenario is removed from the list of candidates.
     * <p>
     * Selection is stopped in one of three cases:
     * <p>
     * - {@link works.lysenko.scenarios.Scenario#isSufficed()} returns {@code true}
     * <p>
     * - there were more selection attempts than configured retries amount
     * <p>
     * - list of candidates become empty (because of all original candidates being
     * removed due to their {@link works.lysenko.scenarios.Scenario#isSufficed()}
     * returning {@code false})
     * <p>
     * Failure to select a scenario due to insufficient prerequisites is not
     * considered exceptional. In this case, parent scenario execution counted in
     * (as long as its {@link works.lysenko.scenarios.Scenario#action()} had already
     * been executed before attempt of nested scenario selection)
     */
    @SuppressWarnings({"FeatureEnvy", "UseOfConcreteClass", "ValueOfIncrementOrDecrementUsed", "ChainedMethodCall", "ObjectAllocationInLoop", "MethodCallInLoopCondition", "AutoBoxing", "AutoUnboxing", "ImplicitNumericConversion", "OverlyLongMethod", "OverlyComplexMethod", "MethodWithMoreThanThreeNegations", "DuplicateStringLiteralInspection", "ThrowInsideCatchBlockWhichIgnoresCaughtException", "LawOfDemeter"})
    public void execute() {
        int retries = this.retries;
        double downstreamWeight;
        if (scenarios.isEmpty())
            l.logProblem(S2, "Current Node scenario have no nested scenarios. Is it just a Leaf scenario?");
        else {
            section(c("Selecting scenario to execute among ", list(scenarios, x._upstream() || x._downstream())));
            List<Pair<Scenario, Double>> candidates = new LinkedList<>();
            { // Cloning of scenarios into candidates and populating upstream weights
                for (Pair<Scenario, Double> pair : scenarios) {
                    Scenario scenario = pair.getKey();
                    Double v = pair.getValue();
                    if (x._upstream()) v = v + scenario.upstream();
                    if (scenario.executable()) {
                        Pair<Scenario, Double> newPair = new Pair<>(scenario, Double.POSITIVE_INFINITY == v ? Double.MAX_VALUE : v);
                        candidates.add(newPair);
                    } else l.logProblem(S3, c(scenario.name(), " is not executable"));
                }
            }
            AbstractScenario scenario = null;
            boolean sufficed = false;
            do { // Main selection cycle
                // boolean doubleBreak = false;
                try { // Select a scenario among candidates
                    scenario = (AbstractScenario) new EnumeratedDistribution<>(candidates).sample();
                } catch (MathArithmeticException e) { // Most probably - all weights are zero
                    if (x._downstream()) {
                        // Cloning candidates into permeates with same default weight
                        List<Pair<Scenario, Double>> permeates = new LinkedList<>();
                        for (Pair<Scenario, Double> pair : candidates) {
                            Scenario scenario1 = pair.getKey();
                            downstreamWeight = downstream(scenario1);
                            Pair<Scenario, Double> newPair = new Pair<>(scenario1, Double.POSITIVE_INFINITY == downstreamWeight ? Double.MAX_VALUE : downstreamWeight);
                            permeates.add(newPair);
                        }
                        try { // Select a scenario among permeates
                            scenario = (AbstractScenario) new EnumeratedDistribution<>(permeates).sample();
                        } catch (MathArithmeticException e1) {
                            l.logProblem(S3, c("Unable to select a scenario among ", list(candidates, true)));
                            // doubleBreak = true;
                            break;
                        }
                    } else {
                        l.logProblem(S3, c("Unable to select a scenario among ", list(candidates, true), (x._downstream() ? " and all nested scenarios" : ""), ": cumulative probability is zero"));
                        break;
                    }
                } catch (NotPositiveException e) {
                    throw new IllegalStateException(c("Unable to select a scenario among ", list(candidates, true), ": negative weights are not allowed"));
                }
                if (null != scenario) sufficed = scenario.isSufficed(); // to eliminate triple call
                if (!sufficed && null != scenario) {
                    l.logProblem(S3, c("Scenario '", scenario.shortName(), "' not sufficed"));
                    Pair<Scenario, Double> toBeRemoved = null;
                    for (Pair<Scenario, Double> pair : candidates)
                        if (pair.getKey() == scenario) toBeRemoved = pair;
                    candidates.remove(toBeRemoved);
                }
            } while (!sufficed && 0 <= --retries && !candidates.isEmpty());
            sleep(1, true); // tiny holdup in order to avoid having warnings sorted up above retry message
            if (sufficed) {
                l.log(0, c("Scenario '", scenario.shortName(), "' sufficed, executing ..."));
                scenario.execute();
            }
            if (candidates.isEmpty())
                l.logProblem(S2, c("Unable to suffice a scenario among ", list(scenarios, false), ": all scenarios have unmet requirements or are not executable"));
            if (!(0 <= retries)) l.logProblem(S2, c("Scenario selection exhausted after ", this.retries, " retries"));
        }
    }

    @SuppressWarnings({"AssignmentOrReturnOfFieldWithMutableType", "WeakerAccess"})
    protected List<Pair<Scenario, Double>> get() {
        return scenarios;
    }

    /**
     * @return list of nested scenarios
     */
    @SuppressWarnings("ChainedMethodCall")
    public Set<Scenario> list() {
        Set<Scenario> set = new SortedScenarioSet();
        for (Pair<Scenario, Double> pair : scenarios)
            set.addAll(pair.getKey().list());
        return set;
    }

    /**
     * Calculate upstream weight of all nested scenarios
     *
     * @return calculated upstream weight
     */
    @SuppressWarnings({"ChainedMethodCall", "AutoBoxing", "AutoUnboxing"})
    public Double upstream() {
        double p = 0.0;
        for (Pair<Scenario, Double> pair : scenarios)
            // if (!(s.getValue()).isNaN()) {
            if (pair.getKey().executable()) {
                p = p + pair.getValue(); // weight of a scenario
                p = p + pair.getKey().upstream(); // weight of underlying
            }
        return p;
    }
}
