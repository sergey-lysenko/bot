package works.lysenko.scenarios.docCoach;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class Kontakt extends AbstractLeafScenario {
	public Kontakt(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Selecting \"kontakt\" section");
		waitThenClick("//a[text()='Kontakt']");
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}