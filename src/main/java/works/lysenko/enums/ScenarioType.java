package works.lysenko.enums;

@SuppressWarnings("javadoc")
public enum ScenarioType {

	LEAF("◆"), NODE("▷"), MONO("◼");

	private String tag;

	ScenarioType(String tag) {
		this.tag = tag;
	}

	public String tag() {
		return tag;
	}
}
