package works.lysenko.scenarios.dexGuru.walletModal;

import static works.lysenko.C.SELL;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Sell extends AbstractLeafScenario {
	public Sell(Execution x) {
		super(x);
	}

	public void action() {
		section("Pressing 'Sell' icon");
		click(SELL);
	}

	public boolean sufficed() {
		return true;
	}
}