package works.lysenko.scenarios.docCoach;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractNodeScenario;

@SuppressWarnings("javadoc")
public class UseCases extends AbstractNodeScenario {
	public UseCases(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Selecting \"use cases\" section");
		waitThenClick("//a[text()='Use Cases']");
		sleep(500);
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}