package works.lysenko.scenarios.google;

import java.util.Set;

import works.lysenko.AbstractNodeScenario;
import works.lysenko.C;
import works.lysenko.Fakers;
import works.lysenko.Run;
import works.lysenko.scenarios.google.buttons.Enter;
import works.lysenko.scenarios.google.buttons.Lucky;
import works.lysenko.scenarios.google.buttons.Search;

public class Character extends AbstractNodeScenario {
	public Character(Run r) {
		super(Set.of(new Search(r), new Lucky(r), new Enter(r)), r);
	}

	public void action() {
		section("Typing random Character into search query input");
		typeIn(C.GOOGLE_INPUT, Fakers.character());
		/*
		 * Absence of "quote" in a data storage is sign that Quote scenario was not
		 * recently executed and it is ok to press Search or Lucky without additional
		 * verifications
		 */
		remove("quote");

	}

	public boolean sufficed() {
		return true;
	}
}