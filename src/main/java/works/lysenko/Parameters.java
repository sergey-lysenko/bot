package works.lysenko;

import org.apache.commons.lang3.StringUtils;
import works.lysenko.enums.Platform;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static works.lysenko.Common.c;
import static works.lysenko.Constants.DEFAULT_DEVICE;
import static works.lysenko.Constants.DEFAULT_DOMAIN;
import static works.lysenko.Constants.DEFAULT_PLATFORM;
import static works.lysenko.Constants.DEFAULT_TEST;
import static works.lysenko.Constants.FORCED_PLATFORMS;
import static works.lysenko.Constants.STORED_PARAMETERS_FILE;
import static works.lysenko.Constants.TEST;
import static works.lysenko.Constants.TESTS;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "OverlyStrongTypeCast", "ClassHasNoToStringMethod", "LocalVariableHidesMemberVariable", "CloneableClassInSecureContext", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "ClassExtendsConcreteCollection"})
public final class Parameters extends Properties {

    private static final long serialVersionUID = -1L;
    private static final int WIDTH = 18;

    private JTextField domain = null;
    private JComboBox<Object> platform = null;
    // JComboBox<Object> device = null;
    private JButton reset = null;
    private JComboBox<Object> test = null;
    private List<JTextField> aparams = null;
    private String[] params = null;
    private String[] types = null;

    /**
     * @param list of additional parameters
     */
    @SuppressWarnings({"ConstantConditions", "FeatureEnvy", "PublicConstructor", "ChainedMethodCall", "AutoUnboxing", "DuplicateStringLiteralInspection"})
    public Parameters(String list) {

        loadFromFile();
        read("TEST", DEFAULT_TEST);
        read("PLATFORM", DEFAULT_PLATFORM);

        if (get("PLATFORM").equals(Platform.ANDROID.title())) {
            read("DEVICE", DEFAULT_DEVICE);
        } else {
            read("DOMAIN", DEFAULT_DOMAIN);
        }

        if (null != list) {
            String[] data = list.split(";");
            params = data[0].split(":");
            types = data[1].split(":");
            for (String param : params)
                read(param);
        }

        boolean inDocker = null != Execution.insideDocker() && Execution.insideDocker();
        boolean inCI = Execution.insideCI();

        if (!(inDocker || inCI)) {
            displayGUI();
            saveToFile();
        }
    }

    @SuppressWarnings("QuestionableName")
    private static void add(String string, Component comp, Container container) {
        container.add(new JLabel(string));
        container.add(comp);
    }

    private static List<String> platformNames() {

        List<String> platformNames = new LinkedList<>();
        for (Platform platform : Platforms.available())
            platformNames.add(platform.title());
        platformNames.sort(null);
        return platformNames;
    }

    @SuppressWarnings({"SwitchStatement", "ObjectAllocationInLoop"})
    private void addAdditionalParameters() {

        // Additional parameters
        if (null != params) {
            aparams = new LinkedList<>();
            int i = 0;
            for (String param : params)
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

    @SuppressWarnings({"StatementWithEmptyBody", "ValueOfIncrementOrDecrementUsed", "ChainedMethodCall", "ObjectAllocationInLoop", "DuplicateStringLiteralInspection"})
    private JPanel dialogueBox() {

        // Building dialogue box
        JPanel p = new JPanel();

        p.setLayout(new GridLayout(size() + 1, 2, 5, 5));
        add("TEST", test, p);
        add("PLATFORM", platform, p);

        if (get("PLATFORM").equals(Platform.ANDROID.title())) {
            // TODO: [framework] continue implementation of automatic emulators management
            // add("DEVICE", device.template, p);
        } else {
            add("DOMAIN", domain, p);
        }

        // Additional parameters
        if (null != aparams) {
            int i = 0;
            for (JTextField aparam : aparams) {
                p.add(new JLabel(params[i++]));
                p.add(aparam);
            }
        }

        add("Platform", reset, p);
        return p;
    }

    @SuppressWarnings("CallToSystemExit")
    private void displayGUI() {

        addStandardParameters();
        addAdditionalParameters();

        int answer = JOptionPane.showConfirmDialog(null, dialogueBox(), "Test Parameters Verification", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (2 == answer || -1 == answer) System.exit(1);

        propagateUserInput();
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void loadFromFile() {
        try (FileInputStream stream = new FileInputStream(STORED_PARAMETERS_FILE)) {
            load(stream);
        } catch (IOException e) {
            // NOP
        }
    }

    @SuppressWarnings({"null", "ConstantConditions", "VariableNotUsedInsideIf", "CallToSystemExit", "DuplicateStringLiteralInspection"})
    private void addPlatforms() {

        List<String> platformNames = platformNames();
        // Creating Browsers ComboBox
        platform = new JComboBox<>(platformNames.toArray());
        platform.setSelectedItem(get("PLATFORM"));
        reset = new JButton((null == FORCED_PLATFORMS) ? "Reset" : "Defined by constant");
        reset.setEnabled(null == FORCED_PLATFORMS);
        reset.addActionListener(e -> {
            Platforms.reset();
            System.exit(2);
        });
    }

    @SuppressWarnings({"ValueOfIncrementOrDecrementUsed", "ChainedMethodCall", "DuplicateStringLiteralInspection"})
    private void propagateUserInput() {

        // Propagation of the user input
        put("TEST", null == test.getSelectedItem() ? "" : test.getSelectedItem().toString());
        put("PLATFORM", null == platform.getSelectedItem() ? "" : platform.getSelectedItem().toString());
        //noinspection StatementWithEmptyBody
        if (get("PLATFORM").equals(Platform.ANDROID.title())) {
            /* Devices selection is commented out until implementation of automatic devices management
            put("DEVICE", device.getSelectedItem() == null ? "" : device.getSelectedItem().toString()); */
        } else if (domain != null) put("DOMAIN",domain.getText());

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

    @SuppressWarnings("CallToSystemGetenv")
    private void read(String name, Object def) {

        if (null != System.getenv(name)) put(name, System.getenv(name)); // prio0: Environment variable
        else if (!containsKey(name)) put(name, def); // prio2: Default value
        // NOP: prio1: Value from cache already loaded
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ImplicitDefaultCharsetUsage", "ProhibitedExceptionThrown", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    private void saveToFile() {
        new File(STORED_PARAMETERS_FILE).getParentFile().mkdirs(); // Create parent directory
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STORED_PARAMETERS_FILE))) { // Create file writer
            store(writer, "");
        } catch (IOException e) {
            throw new RuntimeException(c("Unable to store parameters to file '", STORED_PARAMETERS_FILE, "'"));
        }
    }

    @SuppressWarnings({"ChainedMethodCall", "DuplicateStringLiteralInspection"})
    private void addStandardParameters() {
        // Standard parameters
        addTests();
        addPlatforms();

        //noinspection StatementWithEmptyBody
        if (get("PLATFORM").equals(Platform.ANDROID.title())) {
            /* Devices selection is commented out until implementation of automatic devices management
            List<String> devices = loadLinesFromFile(Paths.get(DEVICES_FILE));
            device = new JComboBox<>(devices.toArray());
            device.setSelectedItem(get("DEVICE")); */
        } else {
            domain = new JTextField((String) get("DOMAIN"), WIDTH);
        }

    }

    /**
     * @param name of a parameter
     * @return {@link String} representation of given parameter's value
     */
    @SuppressWarnings("QuestionableName")
    public String string(String name) {
        return (String) get(name);
    }

    private void addTests() {

        // Tests
        List<String> tests = new LinkedList<>();
        if (!((String) get("TEST")).isEmpty()) {
            tests.add((String) get("TEST"));
        }

        // Retrieving possible test names
        File dir = new File(TESTS);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(TEST));

        // Filling up the list of available tests
        if (null != files) {
            for (File file : files) {
                String test = StringUtils.removeEnd(file.getName(), TEST);
                if (!tests.contains(test)) tests.add(test);
            }
            tests.sort(null);
        }

        // Creating Tests ComboBox
        test = new JComboBox<>(tests.toArray());
        test.setSelectedItem(get("TEST"));
    }

    @SuppressWarnings({"DuplicateStringLiteralInspection", "OverlyBroadThrowsClause", "unused"})
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new java.io.NotSerializableException("works.lysenko.Parameters");
    }

    @SuppressWarnings({"DuplicateStringLiteralInspection", "OverlyBroadThrowsClause", "unused"})
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        throw new java.io.NotSerializableException("works.lysenko.Parameters");
    }
}
