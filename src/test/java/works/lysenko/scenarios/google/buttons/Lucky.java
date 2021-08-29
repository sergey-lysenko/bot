package works.lysenko.scenarios.google.buttons;

import works.lysenko.C;
import works.lysenko.Run;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Lucky extends AbstractLeafScenario {
	public Lucky(Run r) {
		super(r);
	}

	public void action() {
		section("Pressing 'Search' button");
		waitClick(C.GOOGLE_LUCKY_BUTTON);
		makeScreenshot("cycle" + r.currentCycle() + "-" + name());
	}

	public boolean sufficed() {
		return C.quoteShortEnough(r);
	}
}