package works.lysenko.scenarios;

import java.util.Set;

public interface Scenario {

	public abstract void action();

	public abstract int combinations(boolean onlyConfigured);

	public abstract boolean executable();

	public abstract void execute();

	public abstract Set<String> list(boolean shortened, boolean decorated);

	public abstract String name();

	public abstract double permeative();

	public abstract void permeative(double permWeight);

	public abstract double pervasive();

	public abstract boolean sufficed();
}