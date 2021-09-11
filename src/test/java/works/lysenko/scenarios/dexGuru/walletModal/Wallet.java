package works.lysenko.scenarios.dexGuru.walletModal;

import static works.lysenko.C.WALLET;
import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Wallet extends AbstractLeafScenario {
	public Wallet(Execution x) {
		super(x);
	}

	public void action() {
		section("Pressing 'Wallet' icon");
		click(WALLET);
	}

	public boolean sufficed() {
		return true;
	}
}