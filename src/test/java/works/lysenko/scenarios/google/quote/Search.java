package works.lysenko.scenarios.google.quote;

import static works.lysenko.C.GOOGLE_SEARCH_BUTTON;
import static works.lysenko.C.quoteShortEnough;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class Search extends AbstractLeafScenario {
	public Search(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Pressing 'Search' button");
		waitThenClick(GOOGLE_SEARCH_BUTTON);
		makeScreenshot("cycle" + x.currentCycle() + "-" + name());
	}

	@Override
	public boolean sufficed() {
		return quoteShortEnough(x);
	}
}