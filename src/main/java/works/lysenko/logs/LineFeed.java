package works.lysenko.logs;

import static works.lysenko.Constants.EMPTY;

/**
 * @author Sergii Lysenko
 */
public class LineFeed extends AbstractLogData {

	/**
	 * Line Feed
	 */
	public LineFeed() {
		super();
		this.text = EMPTY;
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
		return "LineFeed"; //$NON-NLS-1$
	}

}
