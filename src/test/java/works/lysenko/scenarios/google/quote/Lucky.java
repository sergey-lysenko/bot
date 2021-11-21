package works.lysenko.scenarios.google.quote;

import static works.lysenko.bot.Constants.GOOGLE_LUCKY_BUTTON;
import static works.lysenko.bot.Constants.quoteShortEnough;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
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