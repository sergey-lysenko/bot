package works.lysenko.common;

import org.openqa.selenium.Keys;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class Enter extends AbstractLeafScenario {
	public Enter(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Pressing 'Enter' button");
		sendKeys("Query", Keys.ENTER);
		makeScreenshot("cycle" + x.currentCycle() + "-" + name());
	}

	@Override
	public boolean isSufficed() {
		return true;
	}
}