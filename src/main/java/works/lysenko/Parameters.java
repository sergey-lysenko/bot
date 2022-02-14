package works.lysenko;

import static works.lysenko.Constants.STORED_PARAMETERS_FILE;
import static works.lysenko.Constants.TEST;
import static works.lysenko.Constants.TESTS;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import works.lysenko.enums.Platform;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings("serial")
public class Parameters extends Properties {

	private static final int WIDTH = 18;

	JTextField domain = null;
	JComboBox<Object> platform = null;
	JButton reset = null;
	JComboBox<Object> test = null;
	List<JTextField> aparams = null;
	String[] params;
	String[] types;

	/**
	 * @param list
	 */
	public Parameters(String list) {

		super();
		load();
		read("DOMAIN", "");
		read("PLATFORM", Platform.CHROME.title());
		read("TEST", "");

		if (null != list) {
			String[] data = list.split(";");
			params = data[0].split(":");
			types = data[1].split(":");
			for (String param : params) {
				read(param);
			}
		}

		if (!(Execution.insideDocker() || Execution.insideCI())) {
			displayGUI();
			save();
		}
	}

	private void add(String l, Component m, Container n) {

		n.add(new JLabel(l));
		n.add(m);
	}

	private void additionalParameters() {

		// Additional parameters
		if (null != params) {
			aparams = new LinkedList<JTextField>();
			int i = 0;
			for (String param : params) {
				switch (types[i++]) {
				case "t":
					aparams.add(new JTextField((String) get(param), WIDTH));
					break;
				case "p":
					aparams.add(new JPasswordField((String) get(param), WIDTH));
					break;
				default:
				}
			}
		}
	}

	private void platforms() {

		List<String> platformNames = platformNames();

		// Creating Browsers ComboBox
		platform = new JComboBox<Object>(platformNames.toArray());
		platform.setSelectedItem((String) get("PLATFORM"));

		reset = new JButton("reset");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Platforms.reset();
				System.exit(2);
			}
		});
	}

	private List<String> platformNames() {
		List<String> platformNames = new LinkedList<String>();
		for (Platform b : Platforms.available())
			platformNames.add(b.title());
		platformNames.sort(null);
		return platformNames;
	}

	private JPanel dialogueBox() {

		// Building dialogue box
		JPanel p = new JPanel();

		p.setLayout(new GridLayout(size() + 1, 2, 5, 5));
		add("DOMAIN", domain, p);
		add("PLATFORM", platform, p);
		add("TEST", test, p);

		// Additional parameters
		if (null != aparams) {
			int i = 0;
			for (JTextField aparam : aparams) {
				p.add(new JLabel(params[i++]));
				p.add(aparam);
			}
		}

		add("available browsers", reset, p);
		return p;
	}

	private void displayGUI() {

		standardParameters();
		additionalParameters();

		int answer = JOptionPane.showConfirmDialog(null, dialogueBox(), "Test Parameters Verification",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (answer == 2 || answer == -1)
			System.exit(1);

		propagate();
	}

	private void load() {
		try {
			load(new FileInputStream(STORED_PARAMETERS_FILE));
		} catch (IOException e) {
			// NOP
		}
	}

	private void propagate() {

		// Propagation of the user input
		put("DOMAIN", domain.getText());
		put("PLATFORM", (platform.getSelectedItem() == null) ? "" : platform.getSelectedItem().toString());
		put("TEST", (test.getSelectedItem() == null) ? "" : test.getSelectedItem().toString());

		// Additional parameters
		if (null != aparams) {
			int i = 0;
			for (JTextField aparam : aparams)
				put(params[i++], aparam.getText());
		}
	}

	private void read(String name) {
		read(name, "");
	}

	private void read(String name, Object def) {
		if (!(null == System.getenv(name)))
			put(name, System.getenv(name)); // prio0: Environment variable
		else if (!containsKey(name))
			put(name, def); // prio2: Default value
		// NOP: prio1: Value from cache already loaded
	}

	private void save() {

		new File(STORED_PARAMETERS_FILE).getParentFile().mkdirs(); // Create parent directory
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(STORED_PARAMETERS_FILE));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			store(writer, "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void standardParameters() {

		// Standard parameters
		domain = new JTextField((String) get("DOMAIN"), WIDTH);

		platforms();
		tests();
	}

	/**
	 * @param name
	 * @return String
	 */
	public String string(String name) {
		return (String) get(name);
	}

	private void tests() {

		// Tests
		List<String> tests = new LinkedList<String>();
		tests.add((String) get("TEST"));

		// Retrieving possible test names
		File dir = new File(TESTS);
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(TEST);
			}
		});

		// Filling up the list of available tests
		if (null != files) {
			for (File f : files) {
				String test = StringUtils.removeEnd(f.getName(), TEST);
				if (!tests.contains(test))
					tests.add(test);
			}
			tests.sort(null);
		}

		// Creating Tests ComboBox
		test = new JComboBox<Object>(tests.toArray());
		test.setSelectedItem((String) get("TEST"));
	}
}
