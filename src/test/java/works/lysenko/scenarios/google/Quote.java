package works.lysenko.scenarios.google;

import static works.lysenko.bot.Constants.GOOGLE_INPUT;

import org.openqa.selenium.Keys;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractNodeScenario;
import works.lysenko.scenarios.google.quote.Enter;
import works.lysenko.scenarios.google.quote.Lucky;
import works.lysenko.scenarios.google.quote.Search;
import works.lysenko.utils.Fakers;

@SuppressWarnings("javadoc")
public class Quote extends AbstractNodeScenario {
	public Quote(Execution x) {
		super(x, new Search(x), new Lucky(x), new Enter(x));
	}

	@Override
	public void action() {
		section("Typing random Quote into search query input");
		String q = Fakers.quote();
		put("quote", q.length());
		log(2, "Length of selected quote:" + q.length());
		typeInto(GOOGLE_INPUT, q);
		typeInto(GOOGLE_INPUT, Keys.TAB);
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}