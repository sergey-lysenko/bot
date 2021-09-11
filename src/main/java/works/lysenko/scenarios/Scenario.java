package works.lysenko.scenarios;

import java.util.Set;

public interface Scenario {

	public abstract void action();

	public abstract void execute();

	public abstract Set<String> list(boolean shortened, boolean decorated);

	public abstract double pervasive();

	public abstract boolean sufficed();

	public abstract double permeative();

	public abstract void permeative(double permWeight);

	public abstract int combinations(boolean onlyConfigured);

}