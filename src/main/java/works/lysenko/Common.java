package works.lysenko;

public class Common extends RoutinesWrapper {

	public Common(Run r) {
		super(r);
	}

	public Common() {
		super();
	}
	
	public void logln() {
		log(0, " ");
	}

	public void section(String s) {
		log(0, colorize("= " + s + " =", Color.BLUE_BOLD_BRIGHT));
	}

	public void waitClick(String l) {
		wait(l);
		click(l);
	}

}
