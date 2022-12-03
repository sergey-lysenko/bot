package works.lysenko;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static works.lysenko.Common.c;
import static works.lysenko.Common.loadLinesFromFile;

@SuppressWarnings({"PublicConstructor", "UtilityClass", "UtilityClassWithoutPrivateConstructor", "HardcodedFileSeparator", "DynamicRegexReplaceableByCompiledPattern", "StaticCollection", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutConstructor", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency"})
public final class Descriptors {

    @SuppressWarnings("CollectionWithoutInitialCapacity")
    private static final Map<String, String> descriptors = new HashMap<>();

    static {
        List<String> lines = loadLinesFromFile(Path.of("etc/descriptors"));
        for (String line : lines) {
            String[] a = line.split("==");
            descriptors.put(a[0], a[1]);
        }
    }

    public static void put(String key, String value) {
        descriptors.put(key, value);
    }

    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    public static String locatorOf(String key) {
        String result = descriptors.get(key);
        if (null == result) 
            throw new IllegalArgumentException(c("Descriptor for '", key, "' not found"));
    	return result;
    }
}