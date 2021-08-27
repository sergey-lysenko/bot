package works.lysenko;

public class Problem {
	public long time;
	public String text;

	public Problem(long time, String text) {
		super();
		this.time = time;
		this.text = text;
	}

	public String toString() {
		return "(" + time + ") " + text;
	}
}
