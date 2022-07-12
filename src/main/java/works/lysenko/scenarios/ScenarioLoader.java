package works.lysenko.scenarios;

import static works.lysenko.Constants.u002E;

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

	@SuppressWarnings({ "resource", "nls" })
	private static Set<Class<?>> findAll(String s) {
		Set<Class<?>> a = null;
		try {
			a = new BufferedReader(new InputStreamReader(
					ClassLoader.getSystemClassLoader().getResourceAsStream(s.replaceAll("[.]", "/")))).lines()
					.filter(l -> l.endsWith(".class")).map(l -> getClass(l, s)).collect(Collectors.toSet());
		} catch (@SuppressWarnings("unused") Exception e) {
			System.out.println("There are no '" + s + "' package of nested scenarios");
		}
		return a;
	}

	private static Class<?> getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + u002E + className.substring(0, className.lastIndexOf('.')));
		} catch (@SuppressWarnings("unused") ClassNotFoundException e) {
			// NOP
		}
		return null;
	}

	/**
	 * Read classes from package into a set. It is assumed that package contains
	 * only valid classes. This allows to define relations between scenarios by just
	 * placing them in properly arranged packages tree
	 *
	 * @param s name of the package to load scenarios from
	 * @param x reference to test execution object
	 * @return set of scenarios
	 */
	public static Set<Scenario> read(String s, Execution x) {
		Set<Class<?>> zz = findAll(s);
		Set<Scenario> ss = new HashSet<>();
		for (Class<?> z : zz)
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
		return ss;
	}

}
