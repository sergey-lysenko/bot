package works.lysenko.scenarios;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.scenarios.google.Character;
import works.lysenko.scenarios.google.Quote;
import works.lysenko.utils.GoogleDomains;

@SuppressWarnings("javadoc")
public class Google extends AbstractNodeScenario {

	public Google(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Opening Google");
		openDomain(GoogleDomains.getGoogleDomain());
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}
