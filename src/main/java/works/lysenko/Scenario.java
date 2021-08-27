package works.lysenko;

import java.util.Set;

public interface Scenario {

	public abstract void execute();
	public abstract void action();
	public abstract boolean sufficed();
	public abstract Set<String> defConf();

}