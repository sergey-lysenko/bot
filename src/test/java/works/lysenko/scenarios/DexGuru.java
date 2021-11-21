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
public class DexGuru extends AbstractNodeScenario {

	public DexGuru(Execution x) {
		super(x, new WalletModal(x), new SwitchCurrency(x), new Search(x), new Quote(x));
	}

	@Override
	public void action() {
		section("Opening Dex.Guru");
		openDomain(DEX_DOMAIN);
		find(RAINBOW);
		find(AVATAR);
		waitInvisibility(AVATAR);
		wait(GRAPH);
		if (isPresent(TOOLTIP))
			click(SKIP);
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}
