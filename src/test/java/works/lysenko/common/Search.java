package works.lysenko.common;

import static works.lysenko.bot.Constants.quoteShortEnough;

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
		findThenClick("Search button");
		makeScreenshot("cycle" + x.currentCycle() + "-" + name());
	}

	@Override
	public boolean isSufficed() {
		return quoteShortEnough(x);
	}
}