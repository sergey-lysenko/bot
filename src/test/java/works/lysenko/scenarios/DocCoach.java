package works.lysenko.scenarios;

import static works.lysenko.bot.Constants.AVATAR;
import static works.lysenko.bot.Constants.DEX_DOMAIN;
import static works.lysenko.bot.Constants.GRAPH;
import static works.lysenko.bot.Constants.RAINBOW;
import static works.lysenko.bot.Constants.SKIP;
import static works.lysenko.bot.Constants.TOOLTIP;

import works.lysenko.Execution;
import works.lysenko.scenarios.dexGuru.Quote;
import works.lysenko.scenarios.dexGuru.Search;
import works.lysenko.scenarios.dexGuru.SwitchCurrency;
import works.lysenko.scenarios.dexGuru.WalletModal;

@SuppressWarnings("javadoc")
public class DocCoach extends AbstractNodeScenario {

	public DocCoach(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Opening Doc.Coach");
		openDomain("doc.coach");
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}
