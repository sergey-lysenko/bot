package works.lysenko.scenarios;

import java.util.Set;

@SuppressWarnings("javadoc") // TODO: Add javadoc 
public interface Scenario {

	public abstract void action();

	public abstract int combinations(boolean onlyConfigured);

	public abstract double downstream();

	public abstract void downstream(double permWeight);

	public abstract boolean executable();

	public abstract void execute();

	public abstract void finals();

	public abstract Set<String> list(boolean shortened, boolean decorated);

	public abstract String name();

	public abstract String shortName();

	public abstract boolean sufficed();

	public abstract double upstream();
}