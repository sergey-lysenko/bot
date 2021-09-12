package works.lysenko.scenarios.dexGuru;

import static works.lysenko.C.FROM;
import static works.lysenko.C.INPUT;
import static works.lysenko.C._BUY_;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Quote extends AbstractLeafScenario {

	public Quote(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		typeInto(find(_BUY_, FROM, INPUT), integer(1, 27));
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}
