package works.lysenko.enums;

/**
 * @author Sergii Lysenko
 */
public enum ScenarioType {

	/**
	 * Leaf
	 */
	LEAF("◆"), //$NON-NLS-1$
	
	/**
	 * Node
	 */
	NODE("▷"), //$NON-NLS-1$
	
	/**
	 * Mono
	 */
	MONO("◼"); //$NON-NLS-1$

	private String tag;

	ScenarioType(String tag) {
		this.tag = tag;
	}

	/**
	 * @return tag of this Type
	 */
	public String tag() {
		return this.tag;
	}
}
