package works.lysenko.scenarios;

import works.lysenko.Execution;
import works.lysenko.enums.Severity;
import works.lysenko.utils.SortedScenarioSet;

import java.util.Set;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"unused", "UseOfConcreteClass", "ClassTooDeepInInheritanceTree", "ClassHasNoToStringMethod", "BooleanVariableAlwaysNegated", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "RedundantMethodOverride"})
public class AbstractMonoScenario extends AbstractScenario {

    public static final String HALT_ISN_T_SUPPOSED_TO_BE_CALLED_FOR_MONO_SCENARIO = "halt() isn't supposed to be called for Mono Scenario";
    private boolean executed = false;

    /**
     * @param execution instance of Execution object
     */
    @SuppressWarnings({"unused", "PublicConstructor"})
    public AbstractMonoScenario(Execution execution) {
        super(execution);
    }

    /**
     * @return whether this scenario still runnable or it had been executed already
     */
    @Override
    public boolean executable() {
        return !executed && super.executable();
    }

    /**
     * For a Mono Scenario, execution consist of
     * <p>
     * 1) default execution code defined in super class
     * 2) logic defined in action()
     * 3) final steps defined in finals() of this scenario
     */
    @SuppressWarnings("FinalMethod")
    @Override
    public final void execute() {
        super.execute();
        action();
        finals();
        done();
        executed = true;
    }

    /**
     * In case of Leaf scenario, there's nothing to do there
     */
    @Override
    public void halt() {
        logProblem(Severity.S2, HALT_ISN_T_SUPPOSED_TO_BE_CALLED_FOR_MONO_SCENARIO);
    }

    /**
     * Returns the set with one element - name of the scenario in requested format
     *
     * @return set object with this scenario as single element
     */
    @Override
    public Set<Scenario> list() {
        Set<Scenario> scenarios = new SortedScenarioSet();
        scenarios.add(this);
        return scenarios;
    }
}