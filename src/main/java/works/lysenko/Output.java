package works.lysenko;

import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import works.lysenko.enums.Ansi;
import works.lysenko.output.Groups;
import works.lysenko.output.Parts;
import works.lysenko.scenarios.Scenario;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static works.lysenko.Common.c;
import static works.lysenko.Constants.GENERATED_CONFIG_FILE;
import static works.lysenko.Constants.RUNS;
import static works.lysenko.Constants.RUN_JSON_FILENAME;
import static works.lysenko.Constants.RUN_SVG_FILENAME;
import static works.lysenko.Constants.SVG_HEIGHT;
import static works.lysenko.Constants.SVG_WIDTH;
import static works.lysenko.enums.Ansi.BLACK;
import static works.lysenko.enums.Ansi.CYAN;
import static works.lysenko.enums.Ansi.GREEN;
import static works.lysenko.enums.Ansi.GREEN_BACKGROUND;
import static works.lysenko.enums.Ansi.MAGENTA;
import static works.lysenko.enums.Ansi.RED;
import static works.lysenko.enums.Ansi.RED_BACKGROUND;
import static works.lysenko.enums.Ansi.WHITE_BOLD_BRIGHT;
import static works.lysenko.enums.Ansi.YELLOW;
import static works.lysenko.enums.Ansi.colorize;
import static works.lysenko.enums.Ansi.y;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"StaticMethodOnlyUsedInOneClass", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "BoundedWildcard", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyDependencies", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "MethodWithMultipleLoops"})
public class Output {

    /**
     *
     */
    @SuppressWarnings("UseOfConcreteClass")
    private final Execution x;

    /**
     * @param x reference to {@link Execution} instance
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor"})
    public Output(Execution x) {
        this.x = x;
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConfusingArgumentToVarargsMethod", "UseOfConcreteClass", "ChainedMethodCall", "UseOfPropertiesAsHashtable", "HardcodedFileSeparator", "LawOfDemeter", "FeatureEnvy"})
    static String name(Execution x, String t, String... v) {
        String path = c(RUNS, x.parameters.get("TEST"), "/");
        List<String> list = (new ArrayList<>(Arrays.asList(v)));
        list.add(0, String.valueOf(x.t.startedAt()));
        String name = c(path, Common.fill(t, list.toArray(v)));
        Paths.get(name).getParent().toFile().mkdirs();
        return name;
    }

    @SuppressWarnings({"SameParameterValue", "TypeMayBeWeakened", "DuplicateStringLiteralInspection"})
    private static void drawGroup(SVGGraphics2D g, int col, int[] dy, TreeMap<String, Result> sorted) {
        SortedMap<String, TreeMap<String, Result>> t = new Groups("cycles", sorted);
        for (Map.Entry<String, TreeMap<String, Result>> group : t.entrySet())
            drawGroup(g, null, col, dy, group);
    }

    @SuppressWarnings({"MagicNumber", "UseOfConcreteClass", "ValueOfIncrementOrDecrementUsed", "ChainedMethodCall", "ObjectAllocationInLoop", "CollectionWithoutInitialCapacity", "AutoBoxing", "AutoUnboxing", "ImplicitNumericConversion", "ParameterNameDiffersFromOverriddenParameter", "StringToUpperCaseOrToLowerCaseWithoutLocale"})
    private static void drawGroup(SVGGraphics2D g, Map<String, Integer> parentDys, int col, int[] dy, Map.Entry<String, TreeMap<String, Result>> group) {
        Map<String, Integer> myDys = new HashMap<>();
        Integer pDy = null == parentDys ? null : parentDys.get(group.getKey().toLowerCase());
        // Title
        dy[col]++;
        int tDy = dy[col]++;
        g.setColor(java.awt.Color.GRAY);

        int cW = 300; // Column Width
        int rH = 15; // Row Height

        if (null != pDy) {
            g.drawLine(col * cW - 90, pDy * rH - 4, col * cW - 70, pDy * rH - 4);
            g.drawLine(col * cW - 70, pDy * rH - 4, col * cW - 10, tDy * rH - 4);
            g.drawLine(col * cW - 10, tDy * rH - 4, col * cW + 10, tDy * rH - 4);
        }

        g.setColor(java.awt.Color.BLACK);
        g.drawString(group.getKey(), col * 300 + 15, tDy * 15);

        // Head
        Parts parts = Parts.process(group.getValue());
        for (Map.Entry<String, Result> sgr : parts.head.entrySet()) {
            if (0 == sgr.getValue().executions) {
                if (0 < sgr.getValue().cWeight + sgr.getValue().dWeight + sgr.getValue().uWeight)
                    g.setColor(new Color(164, 164, 255));
                else g.setColor(java.awt.Color.GRAY);
            } else if (sgr.getValue().problems.isEmpty()) g.setColor(new Color(0, 128, 0));
            else g.setColor(new Color(255, 128, 0));
            int myDy = dy[col]++;
            myDys.put(sgr.getKey().toLowerCase().split(" ")[0], myDy);
            g.drawString(c(sgr.getKey(), " : ", sgr.getValue().toString()), col * cW + 15, myDy * rH);
        }

        // Body
        Groups groups = Groups.process(parts.body);
        for (Map.Entry<String, TreeMap<String, Result>> subgroup : groups.entrySet())
            drawGroup(g, myDys, col + 1, dy, subgroup);
    }

    @SuppressWarnings("AutoUnboxing")
    private static double jsonify(Double d) {
        return Double.POSITIVE_INFINITY == d ? Double.MAX_VALUE : d;
    }

    /**
     * Write default configuration to a file
     *
     * @param defConf set of strings to be written as default configuration
     */
    @SuppressWarnings("TypeMayBeWeakened")
    public static void writeDefConf(Set<String> defConf) {
        Common.writeToFile(null, defConf, GENERATED_CONFIG_FILE);
    }

    /**
     * This routine writes test execution in JSON format. Potentially will be
     * replaced by some JSON libraries, if the amount of data to write will be more
     * than manageable. File location defined by DEFAULT_RUNS_LOCATION, file name
     * template defined by RUN_JSON_FILENAME
     */
    @SuppressWarnings({"UseOfConcreteClass", "ValueOfIncrementOrDecrementUsed", "HardcodedLineSeparator", "HardcodedFileSeparator", "ObjectAllocationInLoop", "DynamicRegexReplaceableByCompiledPattern", "AutoBoxing", "OverlyLongMethod", "OverlyComplexMethod", "MagicCharacter", "ImplicitDefaultCharsetUsage", "ProhibitedExceptionThrown", "ThrowInsideCatchBlockWhichIgnoresCaughtException", "LawOfDemeter", "FeatureEnvy"})
    void jsonStats() {
        SortedMap<Scenario, Result> sorted = x.r.getSorted();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(name(x, RUN_JSON_FILENAME)))) {
            int i;
            w.write(c("{\"startAt\":", x.t.startedAt(), ",\"issues\":{"));
            w.write("\"newIssues\":[");
            i = x.newIssues.size();
            for (String str : x.newIssues) {
                w.write(c('"', str.replaceAll("\n", " ").replace("\"", "\\\""), '"'));
                if (1 < i--) w.write(",");
            }
            i = x.knownIssues.size();
            w.write("],\"knownIssues\":[");
            for (String str : x.knownIssues) {
                w.write(c("\"", str.replaceAll("\n", " ").replace("\"", "\\\""), "\""));
                if (1 < i--) w.write(",");
            }
            i = x.notReproduced.size();
            w.write("],\"notReproduced\":[");
            for (String str : x.notReproduced) {
                w.write(c("\"", str.replaceAll("\n", " ").replace("\"", "\\\""), "\""));
                if (1 < i--) w.write(",");
            }
            w.write("]},\"run\":[");
            i = sorted.size();
            for (Map.Entry<Scenario, Result> entry : sorted.entrySet()) {
                w.write(c("{\"scenario\":\"", entry.getKey(), "\"", ",\"cWeight\": ", jsonify(entry.getValue().cWeight), ",\"dWeight\": ", jsonify(entry.getValue().dWeight), ",\"uWeight\": ", jsonify(entry.getValue().uWeight), ",\"executions\":", entry.getValue().executions));
                if (!entry.getValue().problems.isEmpty()) {
                    w.write(",\"problems\":[");
                    int j = entry.getValue().problems.size();
                    for (Problem p : entry.getValue().problems) {
                        String shift;
                        String type;
                        String text;
                        String p0 = p.text.split(" ")[0];
                        String p1 = p.text.split(" ")[1];
                        if (p0.matches("\\[[\\d]+\\]")) {
                            shift = p0.substring(1, p0.length() - 1);
                            type = p1;
                        } else {
                            shift = "";
                            type = p0;
                        }
                        w.write(c("{\"time\":", p.time, ","));
                        if (shift.isEmpty()) text = p.text.substring(type.length() + 1);
                        else {
                            text = p.text.substring(shift.length() + type.length() + 4);
                            w.write(c("\"shift\":", shift, ","));
                        }
                        w.write(c("\"type\":\"", type, "\","));
                        w.write("\"text\":\"");
                        w.write(text.replaceAll("\n", " ").replace("\"", "\\\""));
                        w.write("\"}");
                        if (1 < j--) w.write(",");
                    }
                    w.write("]");
                }
                w.write("}");
                if (1 < i--) w.write(",");
            }
            w.write("]}");
        } catch (IOException e) {
            throw new RuntimeException("JSON stats writing issue");
        }
    }

    @SuppressWarnings({"FeatureEnvy", "LawOfDemeter"})
    private void plaque(String message, Ansi foreground, Ansi background) {
        x.l.log(0, colorize(colorize(" ".repeat(message.length()), background), foreground));
        x.l.log(0, colorize(colorize(message, background), foreground));
        x.l.log(0, colorize(colorize(" ".repeat(message.length()), background), foreground));
    }

    @SuppressWarnings({"FeatureEnvy", "ChainedMethodCall", "ConstantConditions", "ObjectAllocationInLoop", "AutoBoxing", "ImplicitNumericConversion", "OverlyLongMethod", "OverlyComplexMethod", "DuplicateStringLiteralInspection", "LawOfDemeter"})
    void stats() {
        // If scenarios stack is not empty at that point, it means that normal execution
        // was halted due to an error
        boolean status = x.current.isEmpty();
        String failed = null;

        int total = x.cycles.scenarios.combinations(false);
        int active = x.cycles.scenarios.combinations(true);
        String percentage = c(new DecimalFormat("####0.0").format((double) active / total * 100), "%");

        // It works, but this is kinda wrong. It is very non-elegant cross-reference.
        // TODO: [framework] rework this to eliminate cross-reference
        if (!status) {
            failed = x.current.peek().shortName();
            x.current.clear();
        }

        // This is there to keep message un-indented
        x.l.log(0, c(y(x.currentCycle()), " cycle", (1 == x.currentCycle() ? "" : "s"), " of ", x.testDescription(), " done"));

        // New issues output
        x.l.logln();
        if (x.newIssues.isEmpty()) x.l.log(0, colorize("No new Issues", GREEN));
        else {
            x.l.log(0, c((x._debug() ? colorize("Notices and ", CYAN) : ""), colorize("New Issues:", RED)));
            for (String s : x.newIssues)
                x.l.log(0, colorize(s));
        }

        // Known issues
        x.l.logln();
        if (x.knownIssues.isEmpty()) x.l.log(0, colorize("No Known Issues were reproduced", RED));
        else {
            x.l.log(0, colorize("Confirmed Known Issues:", MAGENTA));
            x.l.log(0, colorize(String.join(", ", x.knownIssues), MAGENTA));
        }

        // Not reproduced issues
        if (!x.knownIssues.isEmpty()) {
            x.l.logln();
            if (x.notReproduced.isEmpty()) x.l.log(0, colorize("All Known Issues were reproduced", GREEN));
            else {
                x.l.log(0, colorize("Not reproduced Known Issues:", YELLOW));
                x.l.log(0, colorize(String.join(", ", x.notReproduced), YELLOW));
            }
        }

        // Scenarios statistics
        x.l.logln();
        x.l.log(0, "Scenarios statistics:");
        SortedMap<Scenario, Result> sorted = x.r.getSorted();
        x.l.log(0, c(total, " paths were possible with current set of Scenarios"));
        x.l.log(0, c(active, " (", percentage, ") among these had a chance to be executed"));
        if (sorted.entrySet().isEmpty()) x.l.log(0, colorize("[WARNING] No Test Execution Data available"));
        else for (Map.Entry<Scenario, Result> e : sorted.entrySet())
            x.l.log(0, c(e.getKey().type().tag(), " ", e.getKey().shortName(), " : ", e.getValue().toString()));
        x.l.logln();

        // Result marker
        if (status) plaque(" = Execution passed successfully = ", BLACK, GREEN_BACKGROUND);
        else {
            plaque(c(" = Execution of '", failed, "' failed = "), WHITE_BOLD_BRIGHT, RED_BACKGROUND);
            x.makeSnapshot("_[EXIT]_");
        }
    }

    @SuppressWarnings({"CheckForOutOfMemoryOnLargeArrayAllocation", "ImplicitNumericConversion", "ProhibitedExceptionThrown", "ThrowInsideCatchBlockWhichIgnoresCaughtException", "LawOfDemeter"})
    void svgStats() {
        TreeMap<String, Result> sorted = x.r.getSortedStrings();
        SVGGraphics2D g = new SVGGraphics2D(SVG_WIDTH, SVG_HEIGHT);

        int[] dy = new int[100];
        Arrays.fill(dy, 1);

        drawGroup(g, 0, dy, sorted);
        try {
            SVGUtils.writeToSVG(new File(Common.fill(name(x, RUN_SVG_FILENAME))), g.getSVGElement());
        } catch (IOException e) {
            throw new RuntimeException("Unable to write png file");
        }
    }
}
