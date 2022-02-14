package works.lysenko.scenarios;

import works.lysenko.Execution;

@SuppressWarnings("javadoc")
public class DocCoach extends AbstractNodeScenario {

	public DocCoach(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Opening Doc.Coach");
		openDomain("doc.coach");
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}
