package works.lysenko.scenarios.dexGuru.walletModal;

import static works.lysenko.bot.Constants.SELL;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
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