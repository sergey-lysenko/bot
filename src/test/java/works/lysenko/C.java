package works.lysenko;

public class C { // C for Constants & Common

	// Parameters
	public static final String DEFAULT_TEST = "bot";
	public static final String TEST = (null == System.getenv("TEST")) ? DEFAULT_TEST : System.getenv("TEST");
	// try to increase this limit and see the failures
	public static final Integer MAX_QUOTE_LENGHT_FOR_BUTTONS = 30;

	// Selectors
	public static final String GOOGLE_INPUT = "//input[@class='gLFyf gsfi']";
	public static final String GOOGLE_SEARCH_BUTTON = "//input[@name='btnK']";
	public static final String GOOGLE_LUCKY_BUTTON = "//input[@name='btnI']";

	public static final boolean quoteShortEnough(Run r) {
		return (null == r.data.get("quote")) ? true : (Integer) r.data.get("quote") < C.MAX_QUOTE_LENGHT_FOR_BUTTONS;
	}

}