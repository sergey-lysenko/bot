package works.lysenko.scenarios.google;

import org.openqa.selenium.Keys;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractNodeScenario;
import works.lysenko.utils.Fakers;

@SuppressWarnings("javadoc")
public class Character extends AbstractNodeScenario {
	public Character(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Typing random Character into search query input");
		typeInto("Query", Fakers.character());
		typeInto("Query", Keys.TAB);
		/*
		 * Absence of "quote" in a data storage is sign that Quote scenario was not
		 * recently executed and it is ok to press Search or Lucky without additional
		 * verifications
		 */
		remove("quote");

	}

	@Override
	public boolean isSufficed() {
		return true;
	}
}