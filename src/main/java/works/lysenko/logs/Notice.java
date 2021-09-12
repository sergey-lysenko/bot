package works.lysenko.logs;

public class Notice extends AbstractProblemLogData {


	public Notice(int depth, String text) {
		super(depth, text);
		this.tag = "[NOTICE]";
	}

	public String toString() {
		return "Notice [depth=" + depth + ", tag=" + tag + ", text=" + text + "]";
	}

}
