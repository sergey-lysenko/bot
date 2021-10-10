package works.lysenko.scenarios;

import static works.lysenko.C.DEX_DOMAIN;
import static works.lysenko.C.GRAPH;
import static works.lysenko.C.LOADER_AVATAR;
import static works.lysenko.C.LOADER_RAINBOW;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.scenarios.dexGuru.Quote;
import works.lysenko.scenarios.dexGuru.Search;
import works.lysenko.scenarios.dexGuru.SwitchCurrency;
import works.lysenko.scenarios.dexGuru.WalletModal;

@SuppressWarnings("javadoc")
public class DexGuru extends AbstractNodeScenario {

	public DexGuru(Execution x) {
		super(Set.of(new WalletModal(x), new SwitchCurrency(x), new Search(x), new Quote(x)), x);
	}

	@Override
	public void action() {
		section("Opening Dex.Guru");
		openDomain(DEX_DOMAIN);
		find(LOADER_RAINBOW);
		find(LOADER_AVATAR);
		waitInvisibility(LOADER_AVATAR);
		wait(GRAPH);
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}
