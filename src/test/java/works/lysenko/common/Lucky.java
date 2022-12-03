package works.lysenko.common;

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
		section("Pressing 'Lucky' button");
		findThenClick("L ucky button");
		makeScreenshot("cycle" + x.currentCycle() + "-" + name());
	}

	@Override
	public boolean isSufficed() {
		return quoteShortEnough(x);
	}
}