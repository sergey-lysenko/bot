package works.lysenko.scenarios;

import works.lysenko.enums.ScenarioType;

import java.util.Set;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"InterfaceWithOnlyOneDirectInheritor", "ClassWithTooManyDependents", "ClassWithTooManyTransitiveDependents"})
public interface Scenario {

    /**
     * Container for logic of a scenario. This code executed before selecting one of
     * nested scenarios, if there are any.
     */
    void action();

    /**
     * This routine reports an amount of possible variants of executions for
     * underlying scenarios
     *
     * @param onlyConfigured or all possible scenarios from code base
     * @return number of possible combinations for underlying scenarios
     */
    @SuppressWarnings("BooleanParameter")
    int combinations(boolean onlyConfigured);

    @SuppressWarnings({"SameReturnValue", "unused"})
    Set<String> defConf();

    @SuppressWarnings("unused")
    int depth();

    /**
     * @return Current downstream weight of this Scenario
     */
    double downstream();

    /**
     * Redefine downstream weight of this Scenario
     *
     * @param w downstream weight to set
     */
    void downstream(double w);

    /**
     * @return whether this scenario is executable
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean executable();

    /**
     * Execute this scenario
     */
    @SuppressWarnings("unused")
    void execute();

    /**
     * Avoid starting of sub-scenarios.
     */
    @SuppressWarnings("unused")
    void halt();

    /**
     * Container for logic of a scenario, which is to be executed AFTER nested
     * scenarios execution.
     */
    @SuppressWarnings("unused")
    void finals();

    /**
     * @return list of nested scenarios
     */
    Set<Scenario> list();

    /**
     * @return unique name of this Scenario
     */
    String name();

    /**
     * @return shortened name of this Scenario
     */
    String shortName();

    /**
     * This should be redefined and contain the code to indicate readiness of a
     * Scenario to be executed
     *
     * @return whether this scenario meets it's prerequisites
     */
    @SuppressWarnings("unused")
    boolean isSufficed();

    /**
     * @return {@link works.lysenko.enums.ScenarioType} of Scenario
     */
    ScenarioType type();

    /**
     * @return current upstream weight of this Scenario
     */
    double upstream();

    /**
     * @return own weight of this Scenario
     */
    Double weight();

}