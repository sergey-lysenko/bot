package works.lysenko.enums;

/**
 * @author Sergii Lysenko
 */
public enum ScenarioType {

	/**
	 * Leaf
	 */
	LEAF("◆"),
	
	/**
	 * Node
	 */
	NODE("▷"),
	
	/**
	 * Mono
	 */
	MONO("◼");

	private String tag;

	ScenarioType(String tag) {
		this.tag = tag;
	}

	/**
	 * @return tag of this Type
	 */
	public String tag() {
		return tag;
	}
}
