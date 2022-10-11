package works.lysenko.utils;

/**
 * @author Sergii Lysenko
 */
public class KnownIssueException extends RuntimeException {

	private static final long serialVersionUID = 4203658749164177761L;

	/**
	 * @param string message to identify the known issue
	 */
	public KnownIssueException(String string) {
		super(string);
	}

}
