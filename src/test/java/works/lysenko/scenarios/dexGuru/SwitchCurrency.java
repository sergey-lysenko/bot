package works.lysenko.scenarios.dexGuru;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;
import static works.lysenko.C.*;

public class SwitchCurrency extends AbstractLeafScenario {

	public SwitchCurrency(Execution x) {
		super(x);
	}

	public void action() {

		String sign;
		String text;

		section("Switching base currency");
		WebElement usd = find(USD);
		WebElement eth = find(ETH);

		if (usd.getAttribute("class").equals("active")) {
			click(eth);
			sign = ETH_SIGN;
			text = "ETH";
		} else {
			click(usd);
			sign = USD_SIGN;
			text = "USD";
		}

		Assertions.assertTrue(read(find(LIQUIDITY, sign)).contains(text));
		Assertions.assertTrue(read(find(VOLUME, sign)).contains(text));
	}

	public boolean sufficed() {
		return true;
	}
}
