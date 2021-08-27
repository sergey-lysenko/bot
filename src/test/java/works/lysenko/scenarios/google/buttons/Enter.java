package works.lysenko.scenarios.google.buttons;

import org.openqa.selenium.Keys;

import works.lysenko.AbstractLeafScenario;
import works.lysenko.C;
import works.lysenko.Run;

public class Enter extends AbstractLeafScenario {
	public Enter(Run r) {
		super(r);
	}

	public void action() {
		section("Pressing 'Enter' button");
		sendKeys(C.GOOGLE_INPUT, Keys.ENTER);
		makeScreenshot("cycle" + r.currentCycle() + "-" + name());
	}

	public boolean sufficed() {
		return true;
	}
}