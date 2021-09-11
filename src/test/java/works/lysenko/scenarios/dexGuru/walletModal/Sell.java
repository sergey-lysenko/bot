package works.lysenko.scenarios.dexGuru.walletModal;

import static works.lysenko.C.SELL;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Sell extends AbstractLeafScenario {
	public Sell(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Pressing 'Sell' icon");
		click(SELL);
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}