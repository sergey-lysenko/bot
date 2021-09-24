package works.lysenko.output;

import java.util.Map;
import java.util.TreeMap;

import works.lysenko.Result;

@SuppressWarnings("javadoc")
public class Parts {
	public TreeMap<String, Result> head;
	public TreeMap<String, Result> body;

	public Parts() {
		super();
		this.head = new TreeMap<String, Result>();
		this.body = new TreeMap<String, Result>();
	}

	public Parts(TreeMap<String, Result> head, TreeMap<String, Result> body) {
		super();
		this.head = head;
		this.body = body;
	}

	public static Parts flence(TreeMap<String, Result> source) {
		TreeMap<String, Result> head = new TreeMap<String, Result>();
		TreeMap<String, Result> body = new TreeMap<String, Result>();
		for (Map.Entry<String, Result> e : source.entrySet()) {
			if (Character.isUpperCase(e.getKey().charAt(0)))
				head.put(e.getKey(), e.getValue());
			else
				body.put(e.getKey(), e.getValue());
		}
		return new Parts(head, body);
	}

}
