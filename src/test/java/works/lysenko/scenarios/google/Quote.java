package works.lysenko.scenarios.google;

import org.openqa.selenium.Keys;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractNodeScenario;
import works.lysenko.utils.Fakers;

@SuppressWarnings("javadoc")
public class Quote extends AbstractNodeScenario {
	public Quote(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		section("Typing random Quote into search query input");
		String q = Fakers.quote();
		put("quote", q.length());
		log(2, "Length of selected quote:" + q.length());
		typeInto("Query", q);
		typeInto("Query", Keys.TAB);
	}

	@Override
	public boolean isSufficed() {
		return true;
	}
}