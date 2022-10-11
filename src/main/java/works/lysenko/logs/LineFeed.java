package works.lysenko.logs;

/**
 * @author Sergii Lysenko
 */
public class LineFeed extends AbstractLogData {

	/**
	 * Line Feed
	 */
	public LineFeed() {
		super();
		text = "";
	}

	@Override
	public String render() {
		return text;
	}

	@Override
	public String text() {
		return text;
	}

	@Override
	public String toString() {
		return "LineFeed";
	}

}
