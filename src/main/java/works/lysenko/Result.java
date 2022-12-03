package works.lysenko;

import works.lysenko.enums.ScenarioType;
import works.lysenko.scenarios.Scenario;

import java.util.ArrayList;
import java.util.List;

import static works.lysenko.Common.c;

/**
 * Result record
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings({"LocalVariableHidesMemberVariable", "FieldNotUsedInToString", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependents", "ClassWithTooManyTransitiveDependencies", "CyclicClassDependency"})
public class Result {

    /**
     * type of the Scenario
     */
    @SuppressWarnings("unused")
    public final ScenarioType type;
    /**
     *
     */
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    public final List<Problem> problems = new ArrayList<>();
    /**
     * Weight of the Scenario defined in configuration
     */
    public final double cWeight;
    /**
     * Upstream weight of the Scenario
     */
    public final double uWeight;
    /**
     * Downstream weight of the Scenario
     */
    public final double dWeight;
    /**
     * Number of Scenario executions
     */
    public int executions = 0;

    /**
     * @param scenario Scenario for this Result
     */
    @SuppressWarnings({"FeatureEnvy", "PublicConstructor", "AutoUnboxing"})
    public Result(Scenario scenario) {
        type = scenario.type();
        cWeight = scenario.weight();
        uWeight = scenario.upstream();
        dWeight = scenario.downstream();
    }

    @SuppressWarnings({"NestedConditionalExpression", "UnnecessaryUnicodeEscape", "FeatureEnvy"})
    @Override
    public String toString() {
        String weight = c((0.0 < dWeight ? c("(", dWeight, ")", "\u2192") : ""), "(", cWeight, ")", (0.0 < uWeight ? c("\u2190", "(", uWeight, ")") : ""));
        String problems = c(((this.problems.isEmpty()) ? "" : " ("), this.problems.size(), ((1 == this.problems.size() ? " problem)" : " problems)")));
        return c(weight, " ", executions, problems);
    }
}
