package works.lysenko.scenarios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import works.lysenko.Execution;

/**
 * @author Sergii Lysenko
 */
public class ScenarioLoader {

	/**
	 * Read classes from package into a set. It is assumed that package contains
	 * only valid classes.
	 * 
	 * @param s name of the package to load scenarios from
	 * @param x reference to test execution object
	 * @return set of scenarios
	 */
	public static Set<Scenario> read(String s, Execution x) {
		Set<Class<?>> zz = findAll(s);
		Set<Scenario> ss = new HashSet<Scenario>();
		for (Class<?> z : zz) {
			try {
				Class<?>[] t = { Execution.class };
				Constructor<?> c = z.getConstructor(t);
				Object[] o = { x };
				Object i = c.newInstance(o);
				ss.add((Scenario) i);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return ss;
	}

	private static Set<Class<?>> findAll(String s) {
		Set<Class<?>> a = null;
		try {
			a = new BufferedReader(new InputStreamReader(
					ClassLoader.getSystemClassLoader().getResourceAsStream(s.replaceAll("[.]", "/")))).lines()
					.filter(l -> l.endsWith(".class")).map(l -> getClass(l, s)).collect(Collectors.toSet());
		} catch (Exception e) {
			System.out.println("There are no '" + s + "' package of nested scenarios");
		}
		return a;
	}

	private static Class<?> getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

}
