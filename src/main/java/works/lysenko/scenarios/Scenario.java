package works.lysenko.scenarios;

import java.util.Set;

import works.lysenko.enums.ScenarioType;

@SuppressWarnings("javadoc") 
public interface Scenario {
	
	public abstract void action();

	public abstract int combinations(boolean onlyConfigured);

	public abstract double downstream();

	public abstract void downstream(double permWeight);

	public abstract boolean executable();

	public abstract void execute();

	public abstract void finals();

	public abstract Set<Scenario> list();

	public abstract String name();

	public abstract String shortName();

	public abstract boolean sufficed();

	public abstract ScenarioType type();
	
	public abstract double upstream();

	public abstract Double weight();

	
}