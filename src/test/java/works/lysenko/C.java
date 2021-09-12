package works.lysenko;

public class C { // C for Constants & Common

	// Parameters
	public static final String DEFAULT_TEST = "bot";
	public static final String DEFAULT_DEX_DOMAIN = "dex.guru";
	public static final String TEST = (null == System.getenv("TEST")) ? DEFAULT_TEST : System.getenv("TEST");
	public static final String DEX_DOMAIN = (null == System.getenv("DEX_DOMAIN")) ? DEFAULT_DEX_DOMAIN
			: System.getenv("DEX_DOMAIN");
	// try to increase this limit and see the failures
	public static final Integer MAX_QUOTE_LENGHT_FOR_BUTTONS = 30;

	// Selectors
	public static final String GOOGLE_INPUT = "//input[@class='gLFyf gsfi']";
	public static final String GOOGLE_SEARCH_BUTTON = "//input[@name='btnK']";
	public static final String GOOGLE_LUCKY_BUTTON = "//input[@name='btnI']";

	// Dex.guru
	public final static String LOADER_RAINBOW = "//div[@class='loader-guru__rainbow']";
	public final static String LOADER_AVATAR = "//div[@class='loader-guru__avatar']";
	public final static String GRAPH = "//div[@class='graph']";
	public final static String WALLET = "//div[@class='wallet__icon']";
	public final static String BUY = "//button[@class='button button--xl button--buy']";
	public final static String SELL = "//button[@class='button button--xl button--sell']";
	public final static String PROVIDER_MENU = "//div[@class='provider-menu']";
	public final static String PROVIDER_ITEM = "//button[@class='provider-menu-providers__button']/span[@class='name'][text()='%1$s']";

	public final static String USD = "//button[@data-currency='USD']";
	public final static String ETH = "//button[@data-currency='ETH']";

	public final static String LIQUIDITY = "//aside[@class='dashboard__sidebar dashboard__sidebar--left']/div[@class='sidebar sidebar--left  open']/div[@class='sidebar__wrapper']/div[@class='sidebar__header']/div[@class='chart-sidebar']/div[@class='sum']/span[@class='sign-sum']";
	public final static String VOLUME = "//aside[@class='dashboard__sidebar dashboard__sidebar--right']/div[@class='sidebar sidebar--right  open']/div[@class='sidebar__wrapper']/div[@class='sidebar__header']/div[@class='chart-sidebar']/div[@class='sum']/span[@class='sign-sum']";
	public final static String USD_SIGN = "./span[1]";
	public final static String ETH_SIGN = "./span[2]";

	public final static String SEARCH = "//input[@class='omnibox-search__query']";
	public final static String ROW = "//tr[@class='dropdown-search__trow token']";
	
	public final static String _BUY_ = "//div[@class='tradeform-tab tradeform-tab--buy']";
	public final static String _SELL_ = "//div[@class='tradeform-tab tradeform-tab--sell']";

	public final static String FROM = ".//div[@class='tradeform-field tradeform-field--from']";
	public final static String TO = ".//div[@class='tradeform-field tradeform-field--to']";

	public final static String INPUT = ".//input[@class='tradeform-field__input']";

	public static final boolean quoteShortEnough(Execution x) {
		return (null == x.data.get("quote")) ? true : (Integer) x.data.get("quote") < C.MAX_QUOTE_LENGHT_FOR_BUTTONS;
	}

}