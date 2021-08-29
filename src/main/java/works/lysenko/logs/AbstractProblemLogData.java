package works.lysenko.logs;

import works.lysenko.utils.Color;
import works.lysenko.utils.Severity;

public abstract class AbstractProblemLogData extends AbstractLogData {

	int depth;
	String tag;

	public AbstractProblemLogData(int depth, String text) {
		super();
		this.depth = depth;
		this.text = text;
	}

	public String render() {
		return LogData.renderLog(depth, 0, Color.colorize(text, Severity.byCode(tag).color()));
	}

}
