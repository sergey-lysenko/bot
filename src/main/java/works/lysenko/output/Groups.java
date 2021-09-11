package works.lysenko.output;

import java.util.Map;
import java.util.TreeMap;

import works.lysenko.Result;

public class Groups extends TreeMap<String, TreeMap<String, Result>> {

	private static final long serialVersionUID = -8268293097575167966L;

	public Groups() {
		super();
	}

	public Groups(TreeMap<String, TreeMap<String, Result>> groups) {
		super(groups);
	}

	public Groups(String key, TreeMap<String, Result> value) {
		super();
		put(key, value);
	}

	public static Groups flence(TreeMap<String, Result> source) {
		Groups g = new Groups();
		for (Map.Entry<String, Result> e : source.entrySet()) {
			String groupId = e.getKey().split("\\.")[0];
			String newKey = e.getKey().replace(groupId + ".", "");
			Result newValue = e.getValue();
			TreeMap<String, Result> newMap = (null == g.get(groupId)) ? new TreeMap<String, Result>() : g.get(groupId);
			newMap.put(newKey, newValue);
			g.put(groupId, newMap);
		}
		return g;
	}

}
