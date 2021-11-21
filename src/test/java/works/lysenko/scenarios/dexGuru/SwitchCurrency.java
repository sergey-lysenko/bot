package works.lysenko.scenarios.dexGuru;

import static works.lysenko.bot.Constants.ETH;
import static works.lysenko.bot.Constants.ETH_SIGN;
import static works.lysenko.bot.Constants.LIQUIDITY;
import static works.lysenko.bot.Constants.USD;
import static works.lysenko.bot.Constants.USD_SIGN;
import static works.lysenko.bot.Constants.VOLUME;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;

import works.lysenko.Execution;
import works.lysenko.scenarios.AbstractLeafScenario;

@SuppressWarnings("javadoc")
public class SwitchCurrency extends AbstractLeafScenario {

	public SwitchCurrency(Execution x) {
		super(x);
	}

	@Override
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

	@Override
	public boolean sufficed() {
		return true;
	}
}
