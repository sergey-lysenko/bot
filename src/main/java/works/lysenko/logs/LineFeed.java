package works.lysenko.logs;

@SuppressWarnings("javadoc")
public class LineFeed extends AbstractLogData {

	public LineFeed() {
		super();
		this.text = "";
	}

	public String text() {
		return text;
	}

	public String render() {
		return text;
	}

	public String toString() {
		return "LineFeed";
	}

}
