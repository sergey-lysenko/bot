package works.lysenko.scenarios.docCoach.useCases;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class AntibiotikaCoach extends AbstractLeafScenario {
	public AntibiotikaCoach(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Selecting \"antibiotika.coach\" section");
		waitThenClick("//a[text()='antibiotika.coach']");
		waitValue("//h1[1]", "Antibiotika: \nRichtig ist wichtig.");
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}