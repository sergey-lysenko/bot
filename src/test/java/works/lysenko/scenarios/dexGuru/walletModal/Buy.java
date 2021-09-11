package works.lysenko.scenarios.dexGuru.walletModal;

import static works.lysenko.C.BUY;
import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Buy extends AbstractLeafScenario {
	public Buy(Execution x) {
		super(x);
	}

	public void action() {
		section("Pressing 'Buy' icon");
		click(BUY);
	}

	public boolean sufficed() {
		return true;
	}
}