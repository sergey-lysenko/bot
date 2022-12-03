package works.lysenko.utils;

import java.util.Comparator;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"ComparatorNotSerializable", "PublicConstructor", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutConstructor", "ClassWithTooManyTransitiveDependents", "WeakerAccess"})
public class IgnoreCaseStringComparator implements Comparator<String> {

    @SuppressWarnings("CallToSuspiciousStringMethod")
    @Override
    public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
    }
}
