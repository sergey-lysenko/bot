package works.lysenko.scenarios.google.character;

import static works.lysenko.C.GOOGLE_LUCKY_BUTTON;
import static works.lysenko.C.quoteShortEnough;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Lucky extends AbstractLeafScenario {
	public Lucky(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Pressing 'Search' button");
		waitThenClick(GOOGLE_LUCKY_BUTTON);
		makeScreenshot("cycle" + x.currentCycle() + "-" + name());
	}

	@Override
	public boolean sufficed() {
		return quoteShortEnough(x);
	}
}