package works.lysenko.scenarios;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.scenarios.dexGuru.SwitchCurrency;
import works.lysenko.scenarios.dexGuru.WalletModal;

import static works.lysenko.C.*;

public class DexGuru extends AbstractNodeScenario {

	public DexGuru(Execution x) {
		super(Set.of(new WalletModal(x), new SwitchCurrency(x)), x);
	}

	public void action() {
		section("Opening Dex.Guru");
		openDomain(DEX_DOMAIN);
		find(LOADER_RAINBOW);
		find(LOADER_AVATAR);
		waitInvisibility(LOADER_AVATAR);
		wait(GRAPH);
	}

	public boolean sufficed() {
		return true;
	}
}
