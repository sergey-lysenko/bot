package works.lysenko.scenarios.docCoach.useCases;

import org.junit.jupiter.api.Assertions;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class HochdruckCoach extends AbstractLeafScenario {
	public HochdruckCoach(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Selecting \"hochdruck.coach\" section");
		waitThenClick("//a[text()='hochdruck.coach']");
		waitValue("//h1[1]", "Bluthochdruck: \nIch bleib mir treu!");
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}