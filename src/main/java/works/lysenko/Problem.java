package works.lysenko;

public class Problem {
	public long time;
	public String text;

	public Problem(long tm, String tx) {
		super();
		this.time = tm;
		this.text = tx;
	}

	public String toString() {
		return "(" + time + ") " + text;
	}
}
