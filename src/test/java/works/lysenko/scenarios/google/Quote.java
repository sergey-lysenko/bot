package works.lysenko.scenarios.google;

import static works.lysenko.C.GOOGLE_INPUT;

import java.util.Set;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractNodeScenario;
import works.lysenko.scenarios.google.quote.Enter;
import works.lysenko.scenarios.google.quote.Lucky;
import works.lysenko.scenarios.google.quote.Search;
import works.lysenko.utils.Fakers;

public class Quote extends AbstractNodeScenario {
	public Quote(Execution x) {
		super(Set.of(new Search(x), new Lucky(x), new Enter(x)), x);
	}

	@Override
	public void action() {
		section("Typing random Quote into search query input");
		String q = Fakers.quote();
		put("quote", q.length());
		log(2, "Length of selected quote:" + q.length());
		typeInto(GOOGLE_INPUT, q);
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}