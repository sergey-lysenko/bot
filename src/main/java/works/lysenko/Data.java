package works.lysenko;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 * <p>
 * Wrapper for Properties class. Adds sorting of elements and persisting
 * data to a file
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "SynchronizeOnThis", "MethodMayBeSynchronized", "CloneableClassInSecureContext", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "ClassExtendsConcreteCollection"})
final class Data extends Properties {

    private static final long serialVersionUID = -1L;
    private final String path;
    @SuppressWarnings("UseOfConcreteClass")
    private final Execution x;

    /**
     * @param x reference to {@link works.lysenko.Execution} object
     */
    @SuppressWarnings({"UseOfConcreteClass", "ChainedMethodCall", "UseOfPropertiesAsHashtable", "HardcodedFileSeparator", "OverlyBroadCatchBlock", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    Data(Execution x) {
        path = c("var/", x.parameters.get("TEST"), ".data.properties");
        if (x._persist() && new File(path).exists())
            try (FileInputStream stream = new FileInputStream(path)) {
                load(stream);
            } catch (IOException e) {
                throw new IllegalArgumentException(c("Unable to load data from '", path, "'"));
            }
        this.x = x;
    }

    @SuppressWarnings({"ChainedMethodCall", "NullableProblems"})
    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {

        Set<Map.Entry<Object, Object>> set1 = super.entrySet();
        Set<Map.Entry<Object, Object>> set2 = new LinkedHashSet<>(set1.size());

        Iterator<Map.Entry<Object, Object>> iterator = set1.stream()
                .sorted(Comparator.comparing(o -> o.getKey().toString())).iterator();

        while (iterator.hasNext())
            set2.add(iterator.next());

        return set2;
    }

    @Override
    public Enumeration<Object> keys() {
        synchronized (this) {
            return Collections.enumeration(new TreeSet<>(super.keySet()));
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Set<Object> keySet() {
        return Collections.unmodifiableSet(new TreeSet<>(super.keySet()));
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        Object o = super.put(String.valueOf(key), String.valueOf(value));
        store();
        return o;
    }

    @Override
    public synchronized Object remove(Object key) {
        Object o = super.remove(String.valueOf(key));
        store();
        return o;
    }

    @SuppressWarnings({"OverlyBroadCatchBlock", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    private void store() {
        if (null != x) {
            if (x._persist()) {
                //noinspection ResultOfMethodCallIgnored
                new File(path).getParentFile().mkdirs(); // Create parent directory
                try (FileOutputStream stream = new FileOutputStream(path)) {
                    store(stream, path);
                } catch (IOException e) {
                    throw new IllegalArgumentException(c("Unable to write data to '", path, "'"));
                }
            }
        }
    }

    @SuppressWarnings({"DuplicateStringLiteralInspection", "OverlyBroadThrowsClause", "unused"})
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new java.io.NotSerializableException("works.lysenko.Data");
    }

    @SuppressWarnings({"DuplicateStringLiteralInspection", "OverlyBroadThrowsClause", "unused"})
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        throw new java.io.NotSerializableException("works.lysenko.Data");
    }
}
