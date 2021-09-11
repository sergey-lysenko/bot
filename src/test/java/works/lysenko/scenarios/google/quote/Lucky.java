package works.lysenko.scenarios.google.quote;

import works.lysenko.C;
import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Lucky extends AbstractLeafScenario {
	public Lucky(Execution x) {
		super(x);
	}

	public void action() {
		section("Pressing 'Search' button");
		waitThenClick(C.GOOGLE_LUCKY_BUTTON);
		makeScreenshot("cycle" + x.currentCycle() + "-" + name());
	}

	public boolean sufficed() {
		return C.quoteShortEnough(x);
	}
}