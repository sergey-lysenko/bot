package works.lysenko.scenarios.google;

import static works.lysenko.bot.Constants.GOOGLE_INPUT;

import org.openqa.selenium.Keys;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractNodeScenario;
import works.lysenko.scenarios.google.character.Enter;
import works.lysenko.scenarios.google.character.Lucky;
import works.lysenko.scenarios.google.character.Search;
import works.lysenko.utils.Fakers;

@SuppressWarnings("javadoc")
public class Character extends AbstractNodeScenario {
	public Character(Execution x) {
		super(x, new Search(x), new Lucky(x), new Enter(x));
	}

	@Override
	public void action() {
		section("Typing random Character into search query input");
		typeInto(GOOGLE_INPUT, Fakers.character());
		typeInto(GOOGLE_INPUT, Keys.TAB);
		/*
		 * Absence of "quote" in a data storage is sign that Quote scenario was not
		 * recently executed and it is ok to press Search or Lucky without additional
		 * verifications
		 */
		remove("quote");

	}

	@Override
	public boolean sufficed() {
		return true;
	}
}