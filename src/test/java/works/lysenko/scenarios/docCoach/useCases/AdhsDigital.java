package works.lysenko.scenarios.docCoach.useCases;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class AdhsDigital extends AbstractLeafScenario {
	public AdhsDigital(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Selecting \"ADHS Digital\" section");
		waitThenClick("//a[text()='adhs digital']");
		waitValue("//h1[1]", "ADHS Digital:\nAufmerksamkeit \nwo sie am meisten fehlt.");
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}