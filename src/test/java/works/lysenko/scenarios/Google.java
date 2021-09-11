package works.lysenko.scenarios;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.scenarios.google.Quote;
import works.lysenko.utils.GoogleDomains;
import works.lysenko.scenarios.google.Character;

public class Google extends AbstractNodeScenario {

	public Google(Execution x) {
		super(Set.of(new Quote(x), new Character(x)), x);
	}

	public void action() {
		section("Opening Google");
		openDomain(GoogleDomains.getGoogleDomain());
	}

	public boolean sufficed() {
		return true;
	}
}
