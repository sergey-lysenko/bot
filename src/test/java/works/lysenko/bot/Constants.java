package works.lysenko.bot;

import works.lysenko.Execution;

@SuppressWarnings("javadoc")
public class Constants { // C for Constants & Common

	// Parameters
	public static final String DEFAULT_TEST = "bot";
	public static final String TEST = (null == System.getenv("TEST")) ? DEFAULT_TEST : System.getenv("TEST");

	// try to increase this limit and see the failures
	public static final Integer MAX_QUOTE_LENGHT_FOR_BUTTONS = 30;

	public static final boolean quoteShortEnough(Execution x) {
		Object q = x.get("quote");
		return (null == q) ? true
				: Integer.valueOf((String) q) < Constants.MAX_QUOTE_LENGHT_FOR_BUTTONS;
	}
}