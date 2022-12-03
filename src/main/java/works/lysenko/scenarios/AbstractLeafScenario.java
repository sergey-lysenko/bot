package works.lysenko.scenarios;

import works.lysenko.Constants;
import works.lysenko.Execution;
import works.lysenko.enums.Severity;
import works.lysenko.utils.SortedScenarioSet;

import java.util.Set;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"UseOfConcreteClass", "ClassTooDeepInInheritanceTree", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyDependents", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "RedundantMethodOverride"})
public class AbstractLeafScenario extends AbstractScenario {

    /**
     * Abstract constructor of Leaf Scenario
     *
     * @param execution reference to Execution object
     */
    @SuppressWarnings("PublicConstructor")
    public AbstractLeafScenario(Execution execution) {
        super(execution);
    }

    /**
     * For a Leaf Scenario, execution consist of
     * <p>
     * 1) default execution code defined in super class
     * 2) logic defined in action()
     * 3) final steps defined in finals() of this scenario
     */
    @SuppressWarnings({"unused", "FinalMethod"})
    @Override
    public final void execute() {
        super.execute();
        action();
        finals();
        done();
    }

    /**
     * In case of Leaf scenario, there's nothing to do there
     */
    @SuppressWarnings("unused")
    @Override
    public void halt() {
        logProblem(Severity.S2, Constants.HALT_ISN_T_SUPPOSED_TO_BE_CALLED_FOR_LEAF_SCENARIO);
    }

    /**
     * Returns the set with one element - name of the scenario in requested format
     *
     * @return set object with this scenario as single element
     */
    @SuppressWarnings("unused")
    @Override
    public Set<Scenario> list() {
        Set<Scenario> scenarios = new SortedScenarioSet();
        scenarios.add(this);
        return scenarios;
    }
}
