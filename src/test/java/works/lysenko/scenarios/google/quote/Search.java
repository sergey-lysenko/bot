package works.lysenko.scenarios.google.quote;

import works.lysenko.C;
import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Search extends AbstractLeafScenario {
	public Search(Execution x) {
		super(x);
	}

	public void action() {
		section("Pressing 'Search' button");
		waitThenClick(C.GOOGLE_SEARCH_BUTTON);
		makeScreenshot("cycle" + x.currentCycle() + "-" + name());
	}

	public boolean sufficed() {
		return C.quoteShortEnough(x);
	}
}