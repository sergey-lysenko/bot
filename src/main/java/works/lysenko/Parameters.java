package works.lysenko;

import static works.lysenko.Constants.DOMAIN;
import static works.lysenko.Constants.EMPTY;
import static works.lysenko.Constants.PLATFORM;
import static works.lysenko.Constants.RESET;
import static works.lysenko.Constants.STORED_PARAMETERS_FILE;
import static works.lysenko.Constants.TEST;
import static works.lysenko.Constants.TESTS;
import static works.lysenko.Constants.u002E;
import static works.lysenko.Constants.u003B;
import static works.lysenko.Constants.u0070;
import static works.lysenko.Constants.u0074;

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
import org.eclipse.jdt.annotation.Nullable;

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
		read(DOMAIN, EMPTY);
		read(PLATFORM, Platform.CHROME.title());
		read(TEST.toUpperCase(), EMPTY);

		if (null != list) {
			String[] data = list.split(u003B);
			this.params = data[0].split(u003B);
			this.types = data[1].split(u003B);
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
				case u0074:
					this.aparams.add(new JTextField((String) get(param), WIDTH));
					break;
				case u0070:
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
		add(DOMAIN, this.domain, p);
		add(PLATFORM, this.platform, p);
		add(TEST.toUpperCase(), this.test, p);

		// Additional parameters
		if (null != this.aparams) {
			int i = 0;
			for (JTextField aparam : this.aparams) {
				p.add(new JLabel(this.params[i++]));
				p.add(aparam);
			}
		}

		add("available browsers", this.reset, p); //$NON-NLS-1$
		return p;
	}

	private void displayGUI() {

		standardParameters();
		additionalParameters();

		int answer = JOptionPane.showConfirmDialog(null, dialogueBox(), "Test Parameters Verification", //$NON-NLS-1$
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (answer == 2 || answer == -1)
			System.exit(1);

		propagate();
	}

	@SuppressWarnings("resource")
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
		this.platform.setSelectedItem(get(PLATFORM));

		this.reset = new JButton(RESET);
		this.reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(@Nullable ActionEvent e) {
				Platforms.reset();
				System.exit(2);
			}
		});
	}

	private void propagate() {

		// Propagation of the user input
		put(DOMAIN, this.domain.getText());
		put(PLATFORM, this.platform.getSelectedItem() == null ? EMPTY : this.platform.getSelectedItem().toString());
		put(TEST.toUpperCase(), this.test.getSelectedItem() == null ? EMPTY : this.test.getSelectedItem().toString());

		// Additional parameters
		if (null != this.aparams) {
			int i = 0;
			for (JTextField aparam : this.aparams)
				put(this.params[i++], aparam.getText());
		}
	}

	private void read(String name) {
		read(name, EMPTY);
	}

	private void read(String name, Object def) {
		if (!(null == System.getenv(name)))
			put(name, System.getenv(name)); // prio0: Environment variable
		else if (!containsKey(name))
			put(name, def); // prio2: Default value
		// NOP: prio1: Value from cache already loaded
	}

	@SuppressWarnings("resource")
	private void save() {

		new File(STORED_PARAMETERS_FILE).getParentFile().mkdirs(); // Create parent directory
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(STORED_PARAMETERS_FILE));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			store(writer, EMPTY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void standardParameters() {

		// Standard parameters
		this.domain = new JTextField((String) get(DOMAIN), WIDTH);

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
		tests.add((String) get(TEST.toUpperCase()));

		// Retrieving possible test names
		File directory = new File(TESTS);
		File[] files = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(@Nullable File dir, @Nullable String name) {
				return name.endsWith(u002E + TEST);
			}
		});

		// Filling up the list of available tests
		if (null != files) {
			for (File f : files) {
				String aTest = StringUtils.removeEnd(f.getName(), u002E + TEST);
				if (!tests.contains(aTest))
					tests.add(aTest);
			}
			tests.sort(null);
		}

		// Creating Tests ComboBox
		this.test = new JComboBox<>(tests.toArray());
		this.test.setSelectedItem(get(TEST.toUpperCase()));
	}
}
