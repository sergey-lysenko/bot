package works.lysenko.utils;

import works.lysenko.scenarios.Scenario;

import java.util.Comparator;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"ComparatorNotSerializable", "PublicConstructor", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutConstructor", "ClassWithTooManyTransitiveDependents", "WeakerAccess"})
public class IgnoreCaseScenarioComparator implements Comparator<Scenario> {

    @SuppressWarnings({"ChainedMethodCall", "CallToSuspiciousStringMethod"})
    @Override
    public int compare(Scenario o1, Scenario o2) {
        return o1.name().compareToIgnoreCase(o2.name());
    }
}
