package works.lysenko.scenarios;

import java.util.Set;

import works.lysenko.AbstractNodeScenario;
import works.lysenko.GoogleDomains;
import works.lysenko.Run;
import works.lysenko.scenarios.google.Quote;
import works.lysenko.scenarios.google.Character;

public class Google extends AbstractNodeScenario {

	public Google(Run r) {
		super(Set.of(new Quote(r), new Character(r)), r);
	}

	public void action() {
		section("Opening Google");
		openDomain(GoogleDomains.getGoogleDomain());
	}
}
