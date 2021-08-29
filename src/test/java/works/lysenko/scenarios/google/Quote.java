package works.lysenko.scenarios.google;

import java.util.Set;

import works.lysenko.C;
import works.lysenko.Run;
import works.lysenko.scenarios.AbstractNodeScenario;
import works.lysenko.scenarios.google.buttons.Enter;
import works.lysenko.scenarios.google.buttons.Lucky;
import works.lysenko.scenarios.google.buttons.Search;
import works.lysenko.utils.Fakers;

public class Quote extends AbstractNodeScenario {
	public Quote(Run r) {
		super(Set.of(new Search(r), new Lucky(r), new Enter(r)), r);
	}

	public void action() {
		section("Typing random Quote into search query input");
		String q = Fakers.quote();
		put("quote", q.length()); 
		log(2, "Length of selected quote:" + q.length()); 
		typeIn(C.GOOGLE_INPUT, q);
	}

	public boolean sufficed() {
		return true;
	}
}