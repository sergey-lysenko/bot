package works.lysenko.scenarios.docCoach;

import org.junit.jupiter.api.Assertions;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class Philosophie extends AbstractLeafScenario {
	public Philosophie(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Selecting \"philosophie\" section");
		waitThenClick("//a[text()='Philosophie']");
		sleep(500);
		Assertions.assertEquals("Digital mit Herz. \nAnalog mit Köpfchen.", find("//h1[1]").getText());
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}