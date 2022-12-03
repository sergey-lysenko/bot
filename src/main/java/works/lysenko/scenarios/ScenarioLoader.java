package works.lysenko.scenarios;

import works.lysenko.Execution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static works.lysenko.Common.c;
import static works.lysenko.enums.Severity.S1;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"UtilityClass", "UtilityClassCanBeEnum", "PublicConstructor", "UtilityClassWithoutPrivateConstructor", "ReturnOfNull", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutConstructor", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency"})
public final class ScenarioLoader {

    @SuppressWarnings({"UseOfConcreteClass", "ChainedMethodCall", "DynamicRegexReplaceableByCompiledPattern", "StandardVariableNames", "DuplicateStringLiteralInspection", "ImplicitDefaultCharsetUsage", "OverlyBroadCatchBlock"})
    private static Set<Class<?>> findAll(Execution x, String s, boolean silent) {
        Set<Class<?>> a = null;
        try {
            a = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(s.replaceAll("[.]", "/"))))).lines()
                    .filter(l -> l.endsWith(".class")).map(l -> getClass(l, s)).collect(Collectors.toSet());
        } catch (Exception e) {
            if (!silent)
                x.log(3, c("To test this page logic, add scenarios to '", s, "' package"));
        }
        return a;
    }

    @SuppressWarnings({"ReturnOfNull", "ImplicitNumericConversion", "MethodWithMultipleReturnPoints", "MagicCharacter"})
    private static Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            //
        }
        return null;
    }

    /**
     * Read classes from package into a set. It is assumed that package contains
     * only valid classes. This allows to define relations between scenarios by just
     * placing them in properly arranged packages tree
     *
     * @param s      name of the package to load scenarios from
     * @param x      reference to test execution object
     * @param silent to hide text output or not
     * @return set of scenarios
     */
    @SuppressWarnings({"BooleanParameter", "UseOfConcreteClass", "ChainedMethodCall", "ObjectAllocationInLoop", "CollectionWithoutInitialCapacity", "LawOfDemeter"})
    public static Set<Scenario> read(String s, Execution x, boolean silent) {
        Set<Class<?>> zz = findAll(x, s, silent);
        Set<Scenario> ss = new HashSet<>();
        if (null != zz)
            for (Class<?> z : zz)
                try {
                    Class<?>[] t = {Execution.class};
                    Constructor<?> constructor = z.getConstructor(t);
                    Object[] objects = {x};
                    Object instance = constructor.newInstance(objects);
                    ss.add((Scenario) instance);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                         | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    if (!silent)
                        x.l.logProblem(S1, c(e.getClass().getName() + " caught: ", e.toString()));
                }
        return ss;
    }

}
