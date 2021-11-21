package works.lysenko.scenarios;

import works.lysenko.Execution;
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
