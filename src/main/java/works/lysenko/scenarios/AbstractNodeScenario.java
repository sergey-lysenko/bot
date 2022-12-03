package works.lysenko.scenarios;

import org.apache.commons.math3.util.Pair;
import works.lysenko.Execution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This is abstract implementation of Node Scenario, which have defined both
 * actions to be performed and the set of child scenarios to be executed
 * randomly based on their weight coefficients
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings({"unused", "UseOfConcreteClass", "AssignmentToSuperclassField", "ClassTooDeepInInheritanceTree", "ClassHasNoToStringMethod", "ChainedMethodCall", "ClassWithoutLogger", "PublicMethodWithoutLogging", "FieldHasSetterButNoGetter", "ClassWithoutNoArgConstructor", "ClassWithTooManyDependents", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "RedundantMethodOverride"})
public class AbstractNodeScenario extends AbstractScenario {

    private Scenarios scenarios;
    private boolean halted = false;

    /**
     * Default constructor adds all scenarios from properly named package as
     * sub-scenarios of one being created
     *
     * @param execution instance of Run object
     */
    @SuppressWarnings({"PublicConstructor", "ChainedMethodCall", "AutoUnboxing", "ImplicitNumericConversion", "MagicCharacter"})
    public AbstractNodeScenario(Execution execution) {
        super(execution);
        scenarios = new Scenarios(execution);
        String name = getClass().getName();
        { // Strings are immutable, we need to create a new one
            int i = name.lastIndexOf('.') + 1; // save the position of character in question
            char[] nameChars = name.toCharArray(); // create character array
            nameChars[i] = Character.toLowerCase(nameChars[i]); // Lowercase single char
            name = String.valueOf(nameChars);
        }
        scenarios.add(ScenarioLoader.read(name, execution, false), execution);
        uWeight = scenarios.upstream();
    }

    /**
     * Create an instance of Node Scenario with defined set of sub-scenarios
     *
     * @param ss        array of child scenarios with weight coefficient defined within ..
     * @param execution instance of Run object
     */
    @SuppressWarnings({"PublicConstructor", "AutoUnboxing", "OverloadedVarargsMethod"})
    public AbstractNodeScenario(Execution execution, Scenario... ss) {
        super(execution);
        scenarios = new Scenarios(execution);
        if (null != ss)
            scenarios.add(new HashSet<>(Arrays.asList(ss)), execution);
        uWeight = scenarios.upstream();
    }

    /**
     * Create a Node Scenario and add all scenarios from defined package as
     * sub-scenarios to this one
     *
     * @param s         name of package to load child scenarios from with weight coefficient
     *                  defined within ..
     * @param execution instance of Run object
     */
    @SuppressWarnings({"PublicConstructor", "AutoUnboxing"})
    public AbstractNodeScenario(Execution execution, String s) {
        super(execution);
        scenarios = new Scenarios(execution);
        scenarios.add(ScenarioLoader.read(s, execution, false), execution);
        uWeight = scenarios.upstream();
    }

    /**
     * @param onlyConfigured whether to include all available paths or only ones
     *                       currently configured for execution
     * @return calculated amount of possible execution paths of underlying scenarios
     */
    @SuppressWarnings({"BooleanParameter", "ChainedMethodCall"})
    @Override
    public int combinations(boolean onlyConfigured) {
        int combinations = 0;
        for (Pair<Scenario, Double> pair : scenarios.get())
            combinations = combinations + pair.getKey().combinations(onlyConfigured);
        return combinations;
    }

    /**
     * For a Node Scenario, execution consist of
     * <p>
     * 1) default execution code defined in super class
     * <p>
     * 2) logic defined in action()
     * <p>
     * 3) random execution of one of nested scenarios
     * <p>
     * 4) final steps defined in finals() of this scenario
     */
    @Override
    public void execute() {
        super.execute();
        action();
        if (halted) halted = false;
        else scenarios.execute();
        finals();
        done();
    }

    /**
     * Avoid starting of sub-scenarios.
     */
    @SuppressWarnings("unused")
    public void halt() {
        halted = true;
    }

    /**
     * Returns the set with names of all underlying scenarios
     *
     * @return set of scenarios
     */
    @Override
    public Set<Scenario> list() {
        Set<Scenario> list = super.list();
        list.addAll(scenarios.list());
        return list;
    }

    /**
     * @return whether this scenario meets it's prerequisites
     */
    @Override
    public boolean isSufficed() {
        return false;
    }

    /**
     * @param ss array of child scenarios with weight coefficient defined within ..
     */
    @SuppressWarnings({"PublicMethodNotExposedInInterface", "FinalMethod"})
    public final void setScenarios(Scenario... ss) {
        scenarios = new Scenarios(x);
        if (null != ss)
            scenarios.add(new HashSet<>(Arrays.asList(ss)), x);
    }

    /**
     * @return calculated pervasive weight of this scenario
     */
    @Override
    public double upstream() {
        return uWeight;
    }
}
