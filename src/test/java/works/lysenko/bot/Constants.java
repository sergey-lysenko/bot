package works.lysenko.bot;

import works.lysenko.Execution;

@SuppressWarnings("javadoc")
public class Constants { // C for Constants & Common

	// Parameters
	public static final String DEFAULT_TEST = "bot"; //$NON-NLS-1$
	public static final String TEST = (null == System.getenv("TEST")) ? DEFAULT_TEST : System.getenv("TEST");  //$NON-NLS-1$ //$NON-NLS-2$

	// try to increase this limit and see the failures
	@SuppressWarnings("boxing")
	public static final Integer MAX_QUOTE_LENGHT_FOR_BUTTONS = 30;
	
	// Selectors
	public static final String GOOGLE_INPUT = "//input[@class='gLFyf gsfi']"; //$NON-NLS-1$
	public static final String GOOGLE_SEARCH_BUTTON = "//form/div/div/div/center/input[1]"; //$NON-NLS-1$
	public static final String GOOGLE_LUCKY_BUTTON = "//form/div/div/div/center/input[2]"; //$NON-NLS-1$

	@SuppressWarnings("boxing")
	public static final boolean quoteShortEnough(Execution x) {
		return (null == x.data.get("quote")) ? true //$NON-NLS-1$
				: (Integer) x.data.get("quote") < Constants.MAX_QUOTE_LENGHT_FOR_BUTTONS; //$NON-NLS-1$
	}
}