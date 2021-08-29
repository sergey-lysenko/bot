package works.lysenko.scenarios.google.buttons;

import works.lysenko.C;
import works.lysenko.Run;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Search extends AbstractLeafScenario {
	public Search(Run r) {
		super(r);
	}

	public void action() {
		section("Pressing 'Search' button");
		waitClick(C.GOOGLE_SEARCH_BUTTON);
		makeScreenshot("cycle" + r.currentCycle() + "-" + name());
	}

	public boolean sufficed() {
		return C.quoteShortEnough(r);
	}
}