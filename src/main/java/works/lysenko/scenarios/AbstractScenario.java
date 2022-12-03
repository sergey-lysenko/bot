package works.lysenko.scenarios;

import org.apache.commons.lang3.StringUtils;
import works.lysenko.Common;
import works.lysenko.Execution;
import works.lysenko.enums.ScenarioType;
import works.lysenko.utils.SortedScenarioSet;

import java.util.Arrays;
import java.util.Set;

import static works.lysenko.Constants.ACTION_NO_IMPLEMENTATION;
import static works.lysenko.Constants.DEFAULT_WEIGHT;
import static works.lysenko.Constants.FINALS_NO_IMPLEMENTATION;
import static works.lysenko.enums.Ansi.BLUE_BOLD_BRIGHT;
import static works.lysenko.enums.Ansi.colorize;

/**
 * This is basic implementation of {@link works.lysenko.scenarios.Scenario}
 * interface
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings({"unused", "UseOfConcreteClass", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "LocalVariableHidesMemberVariable", "ReturnOfNull", "ChainedMethodCall", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyDependencies", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "AbstractClassExtendsConcreteClass"})
public abstract class AbstractScenario extends Common implements Scenario {

    /**
     * current upstream weight
     */
    public double uWeight = 0.0;
    /**
     * current downstream weight
     */
    public double dWeight = 0.0;
    private long startAt = 0L;

    /**
     * @param execution instance of {#link works.lysenko.Execution} object associated with
     *                  this scenario
     */
    @SuppressWarnings("ConstructorNotProtectedInAbstractClass")
    public AbstractScenario(Execution execution) {
        super(execution);
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void action() {
        if (x._debug()) log(3, ACTION_NO_IMPLEMENTATION);
    }

    @SuppressWarnings({"BooleanParameter", "AutoUnboxing", "MethodWithMultipleReturnPoints"})
    @Override
    public int combinations(boolean onlyConfigured) {
        if (onlyConfigured)
            return 0.0 < dWeight || 0.0 < uWeight || 0.0 < weight() ? 1 : 0;
        return 1;
    }

    /**
     * This is a stub of the defConf() function, which is to be redefined in
     * descending abstract scenarios implementations.
     *
     * @return null
     */
    @SuppressWarnings({"static-method", "SameReturnValue", "unused", "ReturnOfNull"})
    public Set<String> defConf() {
        return null;
    }

    /**
     * @return logical level of current scenario (number of ascendants)
     */
    @SuppressWarnings({"AutoUnboxing", "LawOfDemeter"})
    public int depth() {
        return gauge() - x.minDepth;
    }

    @SuppressWarnings({"AutoUnboxing", "LawOfDemeter"})
    protected void done() {
        long r = x.timer() - startAt;
        l.log(0, c("'", shortName(), "' done in ", timeH(r)));
        x.current.pop();
    }

    @Override
    public double downstream() {
        return dWeight;
    }

    @Override
    public void downstream(double w) {
        dWeight = w;
    }

    @SuppressWarnings("ChainedMethodCall")
    @Override
    public boolean executable() {
        return !weight().isNaN();
    }

    @SuppressWarnings({"ChainedMethodCall", "AutoUnboxing", "LawOfDemeter"})
    public void execute() {
        startAt = x.timer();
        x.current.push(this);
        l.logln();
        l.log(0, c(colorize(c(type().tag(), " ", shortName()), BLUE_BOLD_BRIGHT), " : ", x.r.count(this)));
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void finals() {
        if (x._debug()) log(3, FINALS_NO_IMPLEMENTATION);
    }

    @SuppressWarnings({"ChainedMethodCall", "AutoBoxing", "LawOfDemeter"})
    private int gauge() {
        int g = name().split("\\.").length;
        if (null == x.minDepth)
            x.minDepth = g;
        return g;
    }

    @Override
    public Set<Scenario> list() {
        Set<Scenario> scenarios = new SortedScenarioSet();
        scenarios.add(this);
        return scenarios;
    }

    @SuppressWarnings("ChainedMethodCall")
    @Override
    public String name() {
        return getClass().getName();
    }

    @SuppressWarnings({"ChainedMethodCall", "AutoUnboxing", "StandardVariableNames", "LawOfDemeter"})
    @Override
    public String shortName() {
        String[] a;
        if (null == x.minDepth)
            gauge();
        a = getClass().getName().split("\\.");
        int b = x.minDepth - 1;
        int c = a.length;
        return String.join(".", Arrays.copyOfRange(a, b, c));
    }

    @Override
    public boolean isSufficed() {
        return false;
    }

    @SuppressWarnings({"ChainOfInstanceofChecks", "ClassReferencesSubclass", "InstanceofThis", "ReturnOfNull", "MethodWithMultipleReturnPoints"})
    @Override
    public ScenarioType type() { // TODO: [framework] eliminate subclass reference
        if (this instanceof AbstractLeafScenario)
            return ScenarioType.LEAF;
        if (this instanceof AbstractNodeScenario)
            return ScenarioType.NODE;
        if (this instanceof AbstractMonoScenario)
            return ScenarioType.MONO;
        return null;
    }

    @Override
    public double upstream() {
        return uWeight;
    }

    @SuppressWarnings({"ChainedMethodCall", "AutoBoxing", "MethodWithMultipleReturnPoints", "CallToSuspiciousStringMethod", "LawOfDemeter"})
    @Override
    public Double weight() {
        String w = x.prop(StringUtils.removeStart(getClass().getName(), c(x._root(), ".")), DEFAULT_WEIGHT);
        if ("-".equals(w))
            return Double.NaN;
        return Double.valueOf(w);
    }
}
