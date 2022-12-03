package works.lysenko;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import works.lysenko.enums.Ansi;
import works.lysenko.enums.Severity;
import works.lysenko.logs.AbstractLogData;
import works.lysenko.logs.KnownIssue;
import works.lysenko.logs.LineFeed;
import works.lysenko.logs.Log;
import works.lysenko.logs.LogRecord;
import works.lysenko.logs.Notice;
import works.lysenko.logs.Severe;
import works.lysenko.logs.Warning;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import static works.lysenko.Common.c;
import static works.lysenko.Constants.FILENAME;
import static works.lysenko.Constants.RUNS;
import static works.lysenko.Constants.RUN_LOG_FILENAME;
import static works.lysenko.enums.Platform.CHROME;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassHasNoToStringMethod", "ParameterHidesMemberVariable", "ClassWithoutLogger", "ClassWithoutNoArgConstructor", "ClassWithTooManyDependencies", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency", "MethodWithMultipleLoops"})
public class Logger {

    /**
     * Link to Execution object
     */
    @SuppressWarnings("UseOfConcreteClass")
    private final Execution x;
    @SuppressWarnings("PackageVisibleField")
    BufferedWriter logWriter;
    /**
     * Set of browser logs to read
     */
    @SuppressWarnings("FieldMayBeFinal")
    private Set<String> logsToRead;
    /**
     * Queue of log messages
     */
    @SuppressWarnings("FieldMayBeFinal")
    private PriorityQueue<LogRecord> log;
    private long prev = 0L;
    private int length = 5;

    /**
     * @param x          instance of {@link Execution}
     * @param logsToRead set of browser logs to read and include
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor", "CollectionWithoutInitialCapacity", "ImplicitDefaultCharsetUsage", "ProhibitedExceptionThrown", "ThrowInsideCatchBlockWhichIgnoresCaughtException"})
    public Logger(Execution x, Set<String> logsToRead) {
        this.x = x;
        try {
            logWriter = new BufferedWriter(new FileWriter(Output.name(x, RUN_LOG_FILENAME)));
        } catch (IOException e) {
            throw new RuntimeException("Unable to open log file for writing");
        }
        this.logsToRead = null == logsToRead ? new HashSet<>() : logsToRead;
        log = new PriorityQueue<>(Comparator.comparing(LogRecord::time));
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void logConsole(String s) {
        System.out.println(s);
    }

    /**
     * Write a string of text to test execution log with defined logical level as
     * visual queues, for example:
     * <p>
     * This is the level 0 record
     * <p>
     * • This is the level 1 record
     * <p>
     * •• This is the level 2 record
     *
     * @param level  log level, 0 is for no markers, 1-n is for number of '•' markers
     *               before a string
     * @param string string of text to be written to log
     */
    @SuppressWarnings("PublicMethodWithoutLogging")
    public void log(int level, @SuppressWarnings("QuestionableName") String string) {
        log(level, string, null);
    }

    /**
     * Write a string of text to test execution log with preceding check of browser
     * console for present errors and eventual output of found browser console logs
     *
     * @param level         of log, 0 is for no markers, 1-n is for number of '•' markers
     *                      before a string
     * @param string        of text to be written to log
     * @param redefinedTime of event, or it can be set to null for measurement by
     *                      this method
     */
    @SuppressWarnings({"FeatureEnvy", "UseOfConcreteClass", "ObjectAllocationInLoop", "AutoUnboxing", "PublicMethodWithoutLogging", "ChainedMethodCall", "OverlyComplexMethod", "WeakerAccess", "LawOfDemeter"})
    public void log(int level, @SuppressWarnings("QuestionableName") String string, Long redefinedTime) {
        LogEntries ls;
        LogRecord lr;
        int depth = x.currentDepth();

        if (x.in(CHROME)) {
            if (logsToRead.contains(LogType.BROWSER)) {
                ls = x.d.manage().logs().get(LogType.BROWSER);
                long time = null == redefinedTime ? x.timer() : redefinedTime;
                for (LogEntry e : ls) {
                    long timestamp = e.getTimestamp() - x.t.startedAt(); // since test start
                    long difference = time - timestamp; // since capturing
                    log.add(problem(timestamp, depth, c("[", difference, "]", " [BROWSER] ", e)));
                }
            }
            if (logsToRead.contains(LogType.CLIENT)) {
                ls = x.d.manage().logs().get(LogType.CLIENT);
                long time = null == redefinedTime ? x.timer() : redefinedTime;
                for (LogEntry e : ls)
                    log.add(problem(time, depth, c("[CLIENT] ", e.toString())));
            }
            if (logsToRead.contains(LogType.DRIVER)) {
                ls = x.d.manage().logs().get(LogType.DRIVER);
                long time = null == redefinedTime ? x.timer() : redefinedTime;
                for (LogEntry e : ls)
                    log.add(problem(time, depth, c("[DRIVER] ", e.toString())));
            }
        }

        AbstractLogData ld = new Log(x.currentDepth(), level, string);
        long time = null == redefinedTime ? x.timer() : redefinedTime;
        lr = new LogRecord(time, ld);
        log.add(lr);
        process();
    }

    /**
     * Write a string of text into the test execution log with logical level 1, for
     * example:
     * <p>
     * • This is the level 1 record
     *
     * @param string string of text to be written to log
     */
    @SuppressWarnings({"PublicMethodWithoutLogging", "QuestionableName"})
    public void log(String string) {
        log(1, string);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void logFile(String s) {
        try {
            if (null == logWriter) log(0, Ansi.colorize("[SEVERE] Log Writer is not initialised, unable to write log"));
            else logWriter.write(c(s, System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a string to a named log file
     *
     * @param string    of text to write
     * @param name      of log file
     * @param extension of log file
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored", "UseOfPropertiesAsHashtable", "HardcodedFileSeparator", "AutoBoxing", "PublicMethodWithoutLogging", "CallToPrintStackTrace", "ChainedMethodCall", "ImplicitDefaultCharsetUsage", "ProhibitedExceptionThrown", "ThrowInsideCatchBlockWhichIgnoresCaughtException", "LawOfDemeter", "FeatureEnvy"})
    public void logFile(@SuppressWarnings("QuestionableName") String string, String name, String extension) {
        String path = c(RUNS, x.parameters.get("TEST"), "/", x.t.startedAt(), "/");
        String timestamp = String.format("%013d", x.t.millis());
        String fullname = Common.fill(FILENAME, timestamp, name, extension);
        String filename = c(path, fullname);

        BufferedWriter writer;
        try {
            Paths.get(filename).getParent().toFile().mkdirs();
            writer = new BufferedWriter(new FileWriter(filename));
        } catch (IOException e) {
            throw new RuntimeException("Unable to open a file for writing");
        }
        try {
            writer.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Path target = Paths.get(".", name);
        Path link = Paths.get(path, Common.fill(FILENAME, "latest", name, extension));
        try {
            // if (Files.exists(link))
            Files.delete(link);
        } catch (IOException e1) {
            // NOP
        }
    }

    /**
     * Log a problem as Known Issue
     *
     * @param s description of known issue
     */
    @SuppressWarnings({"unused", "PublicMethodWithoutLogging"})
    public void logKnownIssue(String s) {
        logProblem(Severity.SK, s);
    }

    /**
     * Log an empty line
     */
    @SuppressWarnings({"UseOfConcreteClass", "AutoUnboxing", "PublicMethodWithoutLogging"})
    public void logln() {
        LogRecord lr;
        lr = new LogRecord(x.timer(), new LineFeed());
        log.add(lr);
    }

    /**
     * Log a problem
     *
     * @param se severity of the problem
     * @param st description of the problem
     */
    @SuppressWarnings({"AutoUnboxing", "PublicMethodWithoutLogging"})
    public void logProblem(Severity se, String st) {
        int depth = x.currentDepth();
        log.add(problem(x.timer(), depth, c(se.tag(), " ", st)));
    }

    @SuppressWarnings({"UseOfConcreteClass", "StandardVariableNames", "IfStatementWithTooManyBranches", "DuplicateStringLiteralInspection", "LawOfDemeter"})
    private LogRecord problem(long t, int d, String p) {
        LogRecord lr;
        Set<String> ki = x.getKnownIssue(p);
        if (!ki.isEmpty()) lr = new LogRecord(t, new KnownIssue(d, c("[KNOWN-ISSUE]:", ki, ":", p)));
        else if (p.contains("[SEVERE]")) {
            lr = new LogRecord(t, new Severe(d, p));
            x.newIssues.add(p);
        } else if (p.contains("[WARNING]")) {
            lr = new LogRecord(t, new Warning(d, p));
            x.newIssues.add(p);
        } else if (p.contains("[NOTICE]")) {
            lr = new LogRecord(t, new Notice(d, p));
            if (x._debug()) x.newIssues.add(p);
        } else if (p.contains("[KNOWN-ISSUE]")) lr = new LogRecord(t, new KnownIssue(d, p));
        else lr = new LogRecord(t, new Notice(d, p));

        x.r.problem(lr);
        return lr;
    }

    @SuppressWarnings({"UseOfConcreteClass", "ObjectAllocationInLoop", "AutoUnboxing", "AutoBoxing", "MethodCallInLoopCondition", "FeatureEnvy"})
    private void process() {
        while (!log.isEmpty()) {
            long since;
            String debug = "";
            LogRecord r = log.poll();
            Long time = r.time();
            String line = r.render();
            if (x._debug()) {
                since = time - prev;
                int thisLength = String.valueOf(since).length();
                if (thisLength > length) length = thisLength;
                debug = c("[", String.format(c("%1$", length, "s"), time - prev), "]");
            }
            logConsole(c(debug, line));
            logFile(c(debug, line));
            prev = time;
        }
    }
}