package works.lysenko.scenarios.dexGuru.walletModal;

import static works.lysenko.bot.Constants.BUY;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class Buy extends AbstractLeafScenario {
	public Buy(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Pressing 'Buy' icon");
		click(BUY);
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}