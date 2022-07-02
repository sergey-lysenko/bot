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
		this.text = "";
	}

	@Override
	public String render() {
		return this.text;
	}

	@Override
	public String text() {
		return this.text;
	}

	@Override
	public String toString() {
		return "LineFeed";
	}

}
