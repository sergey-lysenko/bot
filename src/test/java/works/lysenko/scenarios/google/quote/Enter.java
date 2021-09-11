package works.lysenko.scenarios.google.quote;

import org.openqa.selenium.Keys;

import works.lysenko.C;
import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Enter extends AbstractLeafScenario {
	public Enter(Execution x) {
		super(x);
	}

	public void action() {
		section("Pressing 'Enter' button");
		sendKeys(C.GOOGLE_INPUT, Keys.ENTER);
		makeScreenshot("cycle" + x.currentCycle() + "-" + name());
	}

	public boolean sufficed() {
		return true;
	}
}