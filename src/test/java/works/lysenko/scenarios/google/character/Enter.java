package works.lysenko.scenarios.google.character;

import static works.lysenko.bot.Constants.GOOGLE_INPUT;

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
		sendKeys(GOOGLE_INPUT, Keys.ENTER);
		makeScreenshot("cycle" + x.currentCycle() + "-" + name());
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}