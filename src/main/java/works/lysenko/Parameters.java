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
			this.params = data[0].split(":");
			this.types = data[1].split(":");
			for (String param : this.params)
				read(param);
		}

		if (!(Execution.insideDocker() || Execution.insideCI())) {
			displayGUI();
			save();
		}
	}

	private static void add(String l, Component m, Container n) {
		n.add(new JLabel(l));
		n.add(m);
	}

	private void additionalParameters() {

		// Additional parameters
		if (null != this.params) {
			this.aparams = new LinkedList<>();
			int i = 0;
			for (String param : this.params)
				switch (this.types[i++]) {
				case "t":
					this.aparams.add(new JTextField((String) get(param), WIDTH));
					break;
				case "p":
					this.aparams.add(new JPasswordField((String) get(param), WIDTH));
					break;
				default:
				}
		}
	}

	private JPanel dialogueBox() {

		// Building dialogue box
		JPanel p = new JPanel();

		p.setLayout(new GridLayout(size() + 1, 2, 5, 5));
		add("DOMAIN", this.domain, p);
		add("PLATFORM", this.platform, p);
		add("TEST", this.test, p);

		// Additional parameters
		if (null != this.aparams) {
			int i = 0;
			for (JTextField aparam : this.aparams) {
				p.add(new JLabel(this.params[i++]));
				p.add(aparam);
			}
		}

		add("available browsers", this.reset, p);
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
		} catch (@SuppressWarnings("unused") IOException e) {
			// NOP
		}
	}

	private static List<String> platformNames() {
		List<String> platformNames = new LinkedList<>();
		for (Platform b : Platforms.available())
			platformNames.add(b.title());
		platformNames.sort(null);
		return platformNames;
	}

	private void platforms() {

		List<String> platformNames = platformNames();

		// Creating Browsers ComboBox
		this.platform = new JComboBox<>(platformNames.toArray());
		this.platform.setSelectedItem(get("PLATFORM"));

		this.reset = new JButton("reset");
		this.reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Platforms.reset();
				System.exit(2);
			}
		});
	}

	private void propagate() {

		// Propagation of the user input
		put("DOMAIN", this.domain.getText());
		put("PLATFORM", this.platform.getSelectedItem() == null ? "" : this.platform.getSelectedItem().toString());
		put("TEST", this.test.getSelectedItem() == null ? "" : this.test.getSelectedItem().toString());

		// Additional parameters
		if (null != this.aparams) {
			int i = 0;
			for (JTextField aparam : this.aparams)
				put(this.params[i++], aparam.getText());
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
		this.domain = new JTextField((String) get("DOMAIN"), WIDTH);

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
		List<String> tests = new LinkedList<>();
		tests.add((String) get("TEST"));

		// Retrieving possible test names
		File directory = new File(TESTS);
		File[] files = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(TEST);
			}
		});

		// Filling up the list of available tests
		if (null != files) {
			for (File f : files) {
				String aTest = StringUtils.removeEnd(f.getName(), TEST);
				if (!tests.contains(aTest))
					tests.add(aTest);
			}
			tests.sort(null);
		}

		// Creating Tests ComboBox
		this.test = new JComboBox<>(tests.toArray());
		this.test.setSelectedItem(get("TEST"));
	}
}
