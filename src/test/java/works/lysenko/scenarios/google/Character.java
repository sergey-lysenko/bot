package works.lysenko.scenarios.google;

import static works.lysenko.C.GOOGLE_INPUT;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractNodeScenario;
import works.lysenko.scenarios.google.character.Enter;
import works.lysenko.scenarios.google.character.Lucky;
import works.lysenko.scenarios.google.character.Search;
import works.lysenko.utils.Fakers;

public class Character extends AbstractNodeScenario {
	public Character(Execution x) {
		super(Set.of(new Search(x), new Lucky(x), new Enter(x)), x);
	}

	@Override
	public void action() {
		section("Typing random Character into search query input");
		typeInto(GOOGLE_INPUT, Fakers.character());
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