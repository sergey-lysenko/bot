package works.lysenko;

import org.apache.commons.lang3.StringUtils;
import works.lysenko.scenarios.Scenario;
import works.lysenko.scenarios.ScenarioLoader;
import works.lysenko.scenarios.Scenarios;
import works.lysenko.utils.SortedStringSet;

import java.util.Set;

import static works.lysenko.Common.c;
import static works.lysenko.Constants.CONFIGURATION_CONJOINT;
import static works.lysenko.Constants.CONFIGURATION_CYCLES;
import static works.lysenko.Constants.CONFIGURATION_DEBUG;
import static works.lysenko.Constants.CONFIGURATION_DOWNSTREAM;
import static works.lysenko.Constants.CONFIGURATION_ROOT;
import static works.lysenko.Constants.CONFIGURATION_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_CONJOINT;
import static works.lysenko.Constants.DEFAULT_CYCLES;
import static works.lysenko.Constants.DEFAULT_DEBUG;
import static works.lysenko.Constants.DEFAULT_DOWNSTREAM;
import static works.lysenko.Constants.DEFAULT_ROOT;
import static works.lysenko.Constants.DEFAULT_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_WEIGHT;
import static works.lysenko.Constants.GENERATED_CONFIG_FILE;
import static works.lysenko.enums.Ansi.y;
import static works.lysenko.enums.Severity.S1;
import static works.lysenko.enums.Severity.S2;
import static works.lysenko.enums.Severity.S3;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyDependencies", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "PackageVisibleField"})
public class Cycles {

    @SuppressWarnings("UseOfConcreteClass")
    final Scenarios scenarios;
    /**
     * Associated Execution object
     */
    @SuppressWarnings("UseOfConcreteClass")
    private final Execution x;
    /**
     * Amount of test cycles left to be executed in current run
     */
    public int cyclesToDo;
    private String root = DEFAULT_ROOT;

    /**
     * @param x reference to {@link Execution} instance
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor", "ThisEscapedInObjectConstruction"})
    public Cycles(Execution x) {
        this.x = x;
        root = x._root();
        scenarios = new Scenarios(x);
        scenarios.add(ScenarioLoader.read(root, x, false), x);
        cyclesToDo = x._cycles();
        x.cycles = this;
    }

    /**
     * @param ss set of Scenarios to be set as child nodes
     * @param x  reference to {@link Execution} instance
     */
    @SuppressWarnings({"UseOfConcreteClass", "TypeMayBeWeakened", "PublicConstructor", "ThisEscapedInObjectConstruction"})
    public Cycles(Set<Scenario> ss, Execution x) {
        this.x = x;
        scenarios = new Scenarios(x);
        scenarios.add(ss, x);
        cyclesToDo = x._cycles();
        x.cycles = this;
    }

    /**
     * @param s name of package to load child nodes from
     * @param x reference to {@link Execution} instance
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor", "ThisEscapedInObjectConstruction"})
    public Cycles(String s, Execution x) {
        this.x = x;
        scenarios = new Scenarios(x);
        scenarios.add(ScenarioLoader.read(s, x, false), x);
        cyclesToDo = x._cycles();
        x.cycles = this;
    }

    /**
     * @return default configuration properties
     */
    @SuppressWarnings({"ChainedMethodCall", "FeatureEnvy"})
    private Set<String> defConf() {
        Set<String> configuration = new SortedStringSet();
        configuration.add(c("_", CONFIGURATION_ROOT, " = ", root));
        configuration.add(c("_", CONFIGURATION_DEBUG, " = ", DEFAULT_DEBUG));
        configuration.add(c("_", CONFIGURATION_CYCLES, " = ", DEFAULT_CYCLES));
        configuration.add(c("_", CONFIGURATION_CONJOINT, " = ", DEFAULT_CONJOINT));
        configuration.add(c("_", CONFIGURATION_UPSTREAM, " = ", DEFAULT_UPSTREAM));
        configuration.add(c("_", CONFIGURATION_DOWNSTREAM, " = ", DEFAULT_DOWNSTREAM));
        scenarios.list().forEach(scenario -> configuration.add(c(StringUtils.removeStart(scenario.name(), c(x._root(), ".")), " = ", DEFAULT_WEIGHT)));
        return configuration;
    }

    /**
     * Execute configured cycles
     */
    @SuppressWarnings({"static-access", "resource", "FeatureEnvy", "ValueOfIncrementOrDecrementUsed", "ChainedMethodCall", "ObjectAllocationInLoop", "AutoBoxing", "DuplicateStringLiteralInspection", "OverlyBroadCatchBlock", "ProhibitedExceptionThrown", "LawOfDemeter"})
    public void execute() {
        try {
            if (0 == cyclesToDo) x.l.logProblem(S2, "No test cycles were performed");
            x.l.log(0, c("Executing ", y(x._cycles()), " cycle", (1 == x._cycles() ? "" : "s"), " of ", x.testDescription()));
            while (0 < cyclesToDo) {
                scenarios.execute();
                x.l.logln();
                if (0 < --cyclesToDo) x.l.log(0, c("Cycles to do: ", y(cyclesToDo)));
            }
            if (x.propEmpty())
                x.l.logProblem(S3, c("Test properties are absent. Wrong configuration? Template of Test Run Properties file '", GENERATED_CONFIG_FILE, "' was updated"));
            if (null != x.service()) {
                x.l.log("Closing test service ...");
                x.service().close();
                x.l.log(" ... done.");
            }
        } catch (Exception e) {
            x.exception = e;
            x.l.logProblem(S1, c("Uncaught exception during cycles execution: ", e.getMessage()));
            throw e;
        } finally {
            x.o.writeDefConf(defConf());
        }
    }

    Set<Scenario> scenariosList() {
        return scenarios.list();
    }
}
