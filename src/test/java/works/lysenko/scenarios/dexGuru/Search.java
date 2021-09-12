package works.lysenko.scenarios.dexGuru;

import static works.lysenko.C.ROW;
import static works.lysenko.C.SEARCH;

import org.apache.commons.lang3.RandomStringUtils;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

public class Search extends AbstractLeafScenario {

	public Search(Execution x) {
		super(x);
	}

	@Override
	public void action() {
		typeInto(SEARCH, RandomStringUtils.randomAlphabetic(2));
		click(selectOne(findS(ROW)));
	}

	@Override
	public boolean sufficed() {
		return true;
	}
}
