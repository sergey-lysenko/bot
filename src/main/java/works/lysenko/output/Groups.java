package works.lysenko.output;

import static works.lysenko.Constants.EMPTY;
import static works.lysenko.Constants.MASKED_DOT;
import static works.lysenko.Constants.u002E;

import java.util.Map;
import java.util.TreeMap;

import works.lysenko.Result;

/**
 * @author Sergii Lysenko
 */
public class Groups extends TreeMap<String, TreeMap<String, Result>> {

	private static final long serialVersionUID = -8268293097575167966L;

	/**
	 * @param source
	 * @return Groups
	 */
	public static Groups flence(TreeMap<String, Result> source) {
		Groups g = new Groups();
		for (Map.Entry<String, Result> e : source.entrySet()) {
			String groupId = e.getKey().split(MASKED_DOT)[0];
			String newKey = e.getKey().replace(groupId + u002E, EMPTY);
			Result newValue = e.getValue();
			TreeMap<String, Result> newMap = null == g.get(groupId) ? new TreeMap<>() : g.get(groupId);
			newMap.put(newKey, newValue);
			g.put(groupId, newMap);
		}
		return g;
	}

	/**
	 * 
	 */
	public Groups() {
		super();
	}

	/**
	 * @param key
	 * @param value
	 */
	public Groups(String key, TreeMap<String, Result> value) {
		super();
		put(key, value);
	}

	/**
	 * @param groups
	 */
	public Groups(TreeMap<String, TreeMap<String, Result>> groups) {
		super(groups);
	}

}
