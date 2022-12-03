package works.lysenko;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.json.JSONObject;
import org.openqa.selenium.Point;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import works.lysenko.enums.Platform;
import works.lysenko.scenarios.AbstractScenario;
import works.lysenko.scenarios.Scenario;
import works.lysenko.utils.Stopwatch;
import works.lysenko.utils.WebDrivers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.openqa.selenium.logging.LogType.BROWSER;
import static org.openqa.selenium.logging.LogType.CLIENT;
import static org.openqa.selenium.logging.LogType.DRIVER;
import static org.openqa.selenium.logging.LogType.PERFORMANCE;
import static org.openqa.selenium.logging.LogType.PROFILER;
import static org.openqa.selenium.logging.LogType.SERVER;
import static works.lysenko.Constants.CONFIGURATION_ADEBUG;
import static works.lysenko.Constants.CONFIGURATION_APP;
import static works.lysenko.Constants.CONFIGURATION_BUNDLE_ID;
import static works.lysenko.Constants.CONFIGURATION_CONJOINT;
import static works.lysenko.Constants.CONFIGURATION_CYCLES;
import static works.lysenko.Constants.CONFIGURATION_DEBUG;
import static works.lysenko.Constants.CONFIGURATION_DIR;
import static works.lysenko.Constants.CONFIGURATION_DOWNSTREAM;
import static works.lysenko.Constants.CONFIGURATION_EWAIT;
import static works.lysenko.Constants.CONFIGURATION_IWAIT;
import static works.lysenko.Constants.CONFIGURATION_PERSIST;
import static works.lysenko.Constants.CONFIGURATION_ROOT;
import static works.lysenko.Constants.CONFIGURATION_UPSTREAM;
import static works.lysenko.Constants.DEFAULT_ADEBUG;
import static works.lysenko.Constants.DEFAULT_APP;
import static works.lysenko.Constants.DEFAULT_BUNDLE_ID;
import static works.lysenko.Constants.DEFAULT_CONJOINT;
import static works.lysenko.Constants.DEFAULT_CYCLES;
import static works.lysenko.Constants.DEFAULT_DEBUG;
import static works.lysenko.Constants.DEFAULT_DIR;
import static works.lysenko.Constants.DEFAULT_DOWNSTREAM;
import static works.lysenko.Constants.DEFAULT_EWAIT;
import static works.lysenko.Constants.DEFAULT_IWAIT;
import static works.lysenko.Constants.DEFAULT_PERSIST;
import static works.lysenko.Constants.DEFAULT_ROOT;
import static works.lysenko.Constants.DEFAULT_UPSTREAM;
import static works.lysenko.Constants.KNOWN_ISSUES;
import static works.lysenko.Constants.TEST;
import static works.lysenko.Constants.TESTS;
import static works.lysenko.enums.Ansi.y;

/**
 * This class represent single bot execution information
 *
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "AssignmentToSuperclassField", "ClassWithTooManyMethods", "ClassWithTooManyFields", "ClassHasNoToStringMethod", "UseOfSystemOutOrSystemErr", "ReturnOfNull", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithTooManyDependencies", "ClassWithTooManyDependents", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency"})
public final class Execution extends Common {

    /**
     * Stack of currently executed scenarios
     */
    @SuppressWarnings("CollectionWithoutInitialCapacity")
    public final ArrayDeque<AbstractScenario> current = new ArrayDeque<>();
    // TODO: [framework] rework this implementation to avoid usage of public access
    // to this field from single external code location
    @SuppressWarnings({"UseOfConcreteClass", "PackageVisibleField"})
    final Stopwatch t;
    @SuppressWarnings({"CollectionWithoutInitialCapacity", "PackageVisibleField"})
    final Set<String> newIssues = Collections.newSetFromMap(new ConcurrentHashMap<>());
    @SuppressWarnings({"CollectionWithoutInitialCapacity", "PackageVisibleField"})
    final Collection<String> knownIssues = new HashSet<>();
    /**
     * Linked Cycles object
     */
    @SuppressWarnings("UseOfConcreteClass")
    public Cycles cycles = null;
    /**
     * This information is shared between Scenarios
     */
    public Integer minDepth = null;
    @SuppressWarnings("UseOfConcreteClass")
    public Parameters parameters = null;
    public Properties prop = null;
    /**
     * Exception happened during execution
     */
    @SuppressWarnings("unused")
    public Exception exception = null;
    @SuppressWarnings("PackageVisibleField")
    Set<String> notReproduced = null;
    private long st = System.currentTimeMillis();
    private Properties know = null;
    /**
     * Shared test data storage
     */
    @SuppressWarnings("UseOfConcreteClass")
    private Data data = null;
    /**
     *
     */
    private AppiumDriverLocalService service = null;

    /**
     *
     */
    @SuppressWarnings("PublicConstructor")
    public Execution() {
        this(null);
    }

    /**
     * Start legacy-compatible execution with defined Implicit and Explicit waits
     *
     * @param iwait    implicit wait
     * @param ewait    explicit wait
     * @param platform to execute
     */
    @SuppressWarnings({"PublicConstructor", "ChainedMethodCall", "ThisEscapedInObjectConstruction", "ImplicitNumericConversion"})
    public Execution(int iwait, int ewait, String platform) {
        x = this;
        Set<String> logsToRead = Set.of(BROWSER, CLIENT, DRIVER, PERFORMANCE, PROFILER, SERVER);

        // Bot components
        t = new Stopwatch();
        r = new Results(this);
        l = new Logger(this, logsToRead);
        o = new Output(this);

        // Web driver components
        d = WebDrivers.get(Platform.get(platform), false);
        d.manage().window().setPosition(new Point(0, 0));

        // Web driver parameters
        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(iwait));
        w = new WebDriverWait(d, Duration.ofSeconds(ewait));
    }

    /**
     * Start bot-compatible execution
     *
     * @param parametersList additional parameters list
     */
    @SuppressWarnings({"unchecked", "resource", "PublicConstructor", "ChainedMethodCall", "AccessOfSystemProperties", "UseOfPropertiesAsHashtable", "ConstantConditions", "HardcodedFileSeparator", "ThisEscapedInObjectConstruction", "ImplicitNumericConversion", "OverlyLongMethod", "DuplicateStringLiteralInspection", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    public Execution(String parametersList) {
        x = this;
        Set<String> logsToRead = Set.of(BROWSER, CLIENT, DRIVER, PERFORMANCE, PROFILER, SERVER);

        // Parameters
        System.out.print("Creating parameters ... ");
        parameters = new Parameters(parametersList);
        tick();
        // Bot components
        System.out.print("stopwatch ... ");
        t = new Stopwatch();
        tick();
        System.out.print("results ... ");
        r = new Results(this);
        tick();
        System.out.print("logger ... "); //NON-NLS
        l = new Logger(this, logsToRead);
        tick();
        System.out.print("output ... ");
        o = new Output(this);
        tick();
        System.out.println("done.");

        // Test properties
        System.out.print("Reading known issues ... ");
        know = readProperties(KNOWN_ISSUES);
        tick();
        System.out.print("test properties ... ");
        prop = readProperties(c(TESTS, parameters.get("TEST"), TEST));
        tick();

        // Test data
        System.out.print("test data ... ");
        data = new Data(this);
        tick();
        System.out.println("done.");

        // Web driver components
        System.out.print("Initializing ... ");
        String platform = parameters.string("PLATFORM");
        if (Platform.ANDROID == Platform.get(platform)) {

            tick();
            System.out.print("creating Appium Service ... ");
            if (_adebug())
                service = new AppiumServiceBuilder().withArgument(() -> "--base-path", "/wd/hub/").withLogFile(new File("appium.log")).build();
            else
                service = new AppiumServiceBuilder().withArgument(() -> "--base-path", "/wd/hub/").withArgument(() -> "--log-level", "warn").withLogFile(new File("appium.log")).build();

            tick();
            System.out.print("starting Appium Service ... ");
            service().start();

            tick();
            System.out.print("configuring Driver ... ");
            File appDir = new File(new File(System.getProperty("user.dir")), _dir());
            File app = null;
            try {
                app = new File(appDir.getCanonicalPath(), _app());
            } catch (IOException e) {
                throw new IllegalArgumentException(c("Unable to load app from '", app.toString(), "'"));
            }
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("appium:automationName", "UiAutomator2");
            if (null != app) capabilities.setCapability("app", app.getAbsolutePath());

            tick();
            System.out.print("creating AndroidDriver ... ");

            try {
                d = new AndroidDriver(service().getUrl(), capabilities);
            } catch (SessionNotCreatedException e) {
                throw new IllegalStateException("Unable to instantiate Appium Driver");
            }

        } else {
            tick();
            System.out.print("creating WebDriver ... ");
            d = WebDrivers.get(Platform.get(parameters.string("PLATFORM")), false);

            tick();
            System.out.print("rearranging browser window ... ");
            d.manage().window().setPosition(new Point(0, 0));
            d.manage().window().maximize();
        }
        tick();
        System.out.print("creating additional components ... ");
        // Web driver parameters
        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(_iwait()));
        w = new WebDriverWait(d, Duration.ofSeconds(_ewait()));

        // That's one dirty trick :)
        notReproduced = new HashSet<>((Collection<String>) (Set<?>) know.keySet());
        tick();
        System.out.println("done.");

    }

    /**
     * @return true if current execution is performed in an environment with CI
     * variable set to any value. This is a common way of determining
     * execution inside GitHUB actions and other pipelines
     */
    @SuppressWarnings({"ChainedMethodCall", "BooleanMethodNameMustStartWithQuestion"})
    public static boolean insideCI() {
        return System.getenv().containsKey("CI");
    }

    /**
     * @return true if current execution is performed in a Docker container
     */
    @SuppressWarnings({"ChainedMethodCall", "HardcodedFileSeparator", "AutoBoxing", "BooleanMethodNameMustStartWithQuestion", "MethodWithMultipleReturnPoints", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    public static Boolean insideDocker() {
        try {
            return Files.readString(Paths.get("/proc/1/cgroup")).contains("/docker");
        } catch (NoSuchFileException e) {
            // most probably that is caused by being executed on Windows;
            return false;
        } catch (IOException e) {
            throw new IllegalStateException(c("Unable to determine Docker presence"));
        }
    }

    /**
     * @return _adebug parameter
     */
    @SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "CallToSuspiciousStringMethod"})
    private boolean _adebug() {
        return Boolean.parseBoolean(prop.getProperty("_" + CONFIGURATION_ADEBUG, DEFAULT_ADEBUG).trim());
    }

    /**
     * @return app parameter
     */
    @SuppressWarnings("CallToSuspiciousStringMethod")
    private String _app() {
        return prop.getProperty("_" + CONFIGURATION_APP, DEFAULT_APP).trim();
    }

    /**
     * @return bundleId parameter
     */
    @SuppressWarnings("CallToSuspiciousStringMethod")
    public String _bundleId() {
        return prop.getProperty("_" + CONFIGURATION_BUNDLE_ID, DEFAULT_BUNDLE_ID).trim();
    }

    /**
     * @return whether current test execution have "conjoint" mode active
     */
    @SuppressWarnings({"unused", "BooleanMethodNameMustStartWithQuestion", "CallToSuspiciousStringMethod"})
    public boolean _conjoint() {
        return Boolean.parseBoolean(prop.getProperty("_" + CONFIGURATION_CONJOINT, DEFAULT_CONJOINT).trim());
    }

    /**
     * @return configured number of cycles to be executed
     */
    @SuppressWarnings("CallToSuspiciousStringMethod")
    public int _cycles() {
        return Integer.parseInt(prop.getProperty("_" + CONFIGURATION_CYCLES, DEFAULT_CYCLES).trim());
    }

    /**
     * @return true if debug mode is configured in current test run
     */
    @SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "CallToSuspiciousStringMethod"})
    public boolean _debug() {
        return Boolean.parseBoolean(prop.getProperty("_" + CONFIGURATION_DEBUG, DEFAULT_DEBUG).trim());
    }

    /**
     * @return dir parameter
     */
    @SuppressWarnings("CallToSuspiciousStringMethod")
    private String _dir() {
        return prop.getProperty("_" + CONFIGURATION_DIR, DEFAULT_DIR).trim();
    }

    /**
     * @return true if downstream weight distribution is activated in configuration
     */
    @SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "CallToSuspiciousStringMethod"})
    public boolean _downstream() {
        return Boolean.parseBoolean(prop.getProperty("_" + CONFIGURATION_DOWNSTREAM, DEFAULT_DOWNSTREAM).trim());
    }

    /**
     * @return configured number of cycles to be executed
     */
    @SuppressWarnings("CallToSuspiciousStringMethod")
    private int _ewait() {
        return Integer.parseInt(prop.getProperty("_" + CONFIGURATION_EWAIT, DEFAULT_EWAIT).trim());
    }

    /**
     * @return configured number of cycles to be executed
     */
    @SuppressWarnings("CallToSuspiciousStringMethod")
    private int _iwait() {
        return Integer.parseInt(prop.getProperty("_" + CONFIGURATION_IWAIT, DEFAULT_IWAIT).trim());
    }

    /**
     * @return configured persisting of test data
     */
    @SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "CallToSuspiciousStringMethod"})
    public boolean _persist() {
        return Boolean.parseBoolean(prop.getProperty("_" + CONFIGURATION_PERSIST, DEFAULT_PERSIST).trim());
    }

    /**
     * @return configured root of scenarios
     */
    @SuppressWarnings("CallToSuspiciousStringMethod")
    public String _root() {
        return prop.getProperty("_" + CONFIGURATION_ROOT, DEFAULT_ROOT).trim();
    }

    /**
     * @return true if upstream weight distribution is activated in configuration
     */
    @SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "CallToSuspiciousStringMethod"})
    public boolean _upstream() {
        return Boolean.parseBoolean(prop.getProperty("_" + CONFIGURATION_UPSTREAM, DEFAULT_UPSTREAM).trim());
    }

    /**
     * Performs following routines before ending test execution: - stats() - write
     * execution summary to the console - jsonStats() - write test execution summary
     * in JSON format (for following import into ELK) - conditionalClose() - close
     * browser window if not in CI
     */
    @SuppressWarnings({"FeatureEnvy", "LawOfDemeter"})
    public void complete() {
        o.stats();
        o.jsonStats();
        o.svgStats();
        conditionalClose();
    }

    /**
     * Close browser window in case of execution in CI, but keep it open in case of
     * local execution, which come in handy for development of scenarios
     */
    @SuppressWarnings({"OverlyBroadCatchBlock", "ThrowInsideCatchBlockWhichIgnoresCaughtException", "LawOfDemeter", "AutoUnboxing"})
    private void conditionalClose() {
        try {
            l.logWriter.close();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to close log writer");
        }
        if (insideDocker() || insideCI()) try {
            d.quit();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to close WebDriver");
        }
    }

    /**
     * @return number of current cycle
     */
    public int currentCycle() {
        return _cycles() - cycles.cyclesToDo;
    }

    /**
     * @return logical depth of current scenario
     */
    @SuppressWarnings({"ChainedMethodCall", "ConstantConditions"})
    public int currentDepth() {
        return null == x.currentScenario() ? 0 : x.currentScenario().depth();
    }

    /**
     * @return link to currently executed Scenario
     */
    @SuppressWarnings({"UseOfConcreteClass", "ReturnOfNull"})
    public AbstractScenario currentScenario() {
        return current.isEmpty() ? null : current.peek();
    }

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean dataContainsKey(Object field) {
        return data.containsKey(field);
    }

    @SuppressWarnings("UseOfPropertiesAsHashtable")
    public Object dataGet(Object field) {
        return data.get(field);
    }

    public Object dataGetOrDefault(Object field, Object def) {
        return data.getOrDefault(field, def);
    }

    public Object dataPut(Object field, Object value) {
        return data.put(field, value);
    }

    public void dataRemove(Object field) {
        data.remove(field);
    }

    /**
     * Calculate downstream weights of given scenario from current execution
     * parameters
     *
     * @param scenario Scenario to calculate downstream weights
     * @return cumulative downstream weight
     */
    @SuppressWarnings({"ChainedMethodCall", "AutoBoxing", "StandardVariableNames", "ImplicitNumericConversion", "MagicCharacter", "StringToUpperCaseOrToLowerCaseWithoutLocale"})
    public Double downstream(Scenario scenario) {
        // TODO: [framework] move this to Scenario class(es) ?
        double v = 0.0;
        for (Map.Entry<Object, Object> p : prop.entrySet()) {
            String key = (String) p.getKey();
            if ('_' != key.charAt(0)) {
                String a = scenario.getClass().getName().toLowerCase();
                String b = key.toLowerCase();
                if (a.contains(b)) if (!p.getValue().equals("-")) v = v + Double.parseDouble((String) p.getValue());
            }
        }
        scenario.downstream(v);
        return v;
    }

    @SuppressWarnings({"AccessingNonPublicFieldOfAnotherObject", "LawOfDemeter"})
    public JSONObject getJsonData() {
        return new JSONObject(x.data);
    }

    /**
     * @param p string to search Known Issues
     * @return Set of KnownIssues for given query
     */
    @SuppressWarnings({"CollectionWithoutInitialCapacity", "StandardVariableNames", "OverlyLongLambda"})
    public Set<String> getKnownIssue(String p) {
        Set<String> ki = new HashSet<>();
        if (null != know) know.forEach((k, v) -> {
            if (p.contains((CharSequence) v)) {
                ki.add((String) k);
                if (notReproduced.contains(k)) {
                    notReproduced.remove(k);
                    knownIssues.add((String) k);
                }
            }
        });
        return ki;
    }

    /**
     * @return {@link Platform} in which current execution takes place
     */
    @SuppressWarnings({"DuplicateStringLiteralInspection", "LawOfDemeter"})
    @Override
    public Platform in() {
        return Platform.get(x.parameters.string("PLATFORM"));
    }

    /**
     * Define whether current {@link Execution} is happening in defined {@link Platform}
     *
     * @param platform {@link Platform} to check
     * @return true if current is in defined browser
     */
    @SuppressWarnings({"ChainedMethodCall", "UnnecessaryJavaDocLink", "DuplicateStringLiteralInspection", "CallToSuspiciousStringMethod", "LawOfDemeter"})
    @Override
    public boolean in(Platform platform) {
        return x.parameters.string("PLATFORM").equals(platform.title());
    }

    /**
     * @param name of run property
     * @param def  default value of run property
     * @return value of requested property
     */
    public String prop(String name, String def) {
        return prop.getProperty(name, def);
    }

    /**
     * @return true is properties is empty
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean propEmpty() {
        return prop.isEmpty();
    }

    public AppiumDriverLocalService service() {
        return service;
    }

    /**
     * @return test description
     */
    @SuppressWarnings({"ChainedMethodCall", "UseOfPropertiesAsHashtable", "DuplicateStringLiteralInspection", "LawOfDemeter"})
    public String testDescription() {
        return c(y(x.parameters.get("TEST")), c((x.parameters.get("DOMAIN").toString().isBlank()) ? "" : " on ", y(x.parameters.get("DOMAIN"))));
    }

    private void tick() {
        long now = System.currentTimeMillis();
        String length = String.valueOf(now - st);
        System.out.print(c(length, ", "));
        st = now;
    }

    /**
     * @return amount of milliseconds since start of test execution
     */
    @SuppressWarnings("AutoBoxing")
    public Long timer() {
        return t.millis();
    }
}
