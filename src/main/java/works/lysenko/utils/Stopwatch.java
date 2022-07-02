package works.lysenko.utils;

/**
 * @author Sergii Lysenko
 */
public class Stopwatch {
	private long start;

	/**
	 * Create and start the Stopwatch instance
	 */
	public Stopwatch() {
		this.start = System.currentTimeMillis();
	}

	/**
	 * @return milliseconds since Stopwatch instantiation
	 */
	public long millis() {
		return System.currentTimeMillis() - this.start;
	}

	/**
	 * @return readable string of Stopwatch uptime
	 */
	public String read() {
		return String.valueOf(millis()) + " ms";
	}

	/**
	 * @return moment of Stopwatch instantiation
	 */
	public long startedAt() {
		return this.start;
	}
}
