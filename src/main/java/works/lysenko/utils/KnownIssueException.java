package works.lysenko.utils;

/**
 * @author Sergii Lysenko
 */
public class KnownIssueException extends RuntimeException {

	/**
	 * @param string
	 */
	public KnownIssueException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 4203658749164177761L;

}
