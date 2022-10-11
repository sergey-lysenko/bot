package works.lysenko.output;

import java.util.Map;
import java.util.TreeMap;

import works.lysenko.Result;

/**
 * @author Sergii Lysenko
 */
public class Parts {
	/**
	 * @param source
	 * @return Parts
	 */
	public static Parts flence(TreeMap<String, Result> source) {
		TreeMap<String, Result> head = new TreeMap<>();
		TreeMap<String, Result> body = new TreeMap<>();
		for (Map.Entry<String, Result> e : source.entrySet())
			if (Character.isUpperCase(e.getKey().charAt(0)))
				head.put(e.getKey(), e.getValue());
			else
				body.put(e.getKey(), e.getValue());
		return new Parts(head, body);
	}

	/**
	 * 
	 */
	public TreeMap<String, Result> head;

	/**
	 * 
	 */
	public TreeMap<String, Result> body;

	/**
	 * 
	 */
	public Parts() {
		super();
		head = new TreeMap<>();
		body = new TreeMap<>();
	}

	/**
	 * @param head
	 * @param body
	 */
	public Parts(TreeMap<String, Result> head, TreeMap<String, Result> body) {
		super();
		this.head = head;
		this.body = body;
	}

}
