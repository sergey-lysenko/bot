package works.lysenko.scenarios.dexGuru;

import static works.lysenko.C.ROW;
import static works.lysenko.C.SEARCH;

import org.apache.commons.lang3.RandomStringUtils;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class Search extends AbstractLeafScenario {

	public Search(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		String query = RandomStringUtils.randomAlphabetic(2);
		typeInto(SEARCH, query);
		click(selectOne(findAll(ROW)));
		put("last-query", query);
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}
