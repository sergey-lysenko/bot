package works.lysenko.logs;

import java.text.DecimalFormat;

import static works.lysenko.Common.c;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ParameterHidesMemberVariable", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithoutNoArgConstructor", "ClassWithTooManyTransitiveDependencies", "ClassWithTooManyTransitiveDependents", "CyclicClassDependency"})
public class LogRecord {

    private final long time;
    @SuppressWarnings("UseOfConcreteClass")
    private final AbstractLogData data;

    /**
     * @param time time
     * @param data {@link AbstractLogData}
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor"})
    public LogRecord(long time, AbstractLogData data) {
        this.data = data;
        this.time = time;
    }

    /**
     * @param time time
     * @param data {@link KnownIssue}
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor"})
    public LogRecord(long time, KnownIssue data) {
        this.data = data;
        this.time = time;
    }

    /**
     * @param time time
     * @param data {@link Notice}
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor"})
    public LogRecord(long time, Notice data) {
        this.data = data;
        this.time = time;
    }

    /**
     * @param time time
     * @param data {@link Severe}
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor"})
    public LogRecord(long time, Severe data) {
        this.data = data;
        this.time = time;
    }

    /**
     * @param time time
     * @param data {@link Warning}
     */
    @SuppressWarnings({"UseOfConcreteClass", "PublicConstructor"})
    public LogRecord(long time, Warning data) {
        this.data = data;
        this.time = time;
    }

    /**
     * @param t time
     * @return formatted time
     */
    @SuppressWarnings({"ChainedMethodCall", "ImplicitNumericConversion", "WeakerAccess"})
    public static String renderTime(Long t) {
        return c("[", new DecimalFormat("0000000.000").format(t.doubleValue() / 1000), "]");
    }

    /**
     * @return data
     */
    @SuppressWarnings("unused")
    public LogData data() {
        return data;
    }

    /**
     * @return rendered LogRecord
     */
    @SuppressWarnings("AutoBoxing")
    public String render() {
        return c(renderTime(time), " ", data.render());
    }

    /**
     * @return text of the LogRecord
     */
    public String text() {
        return data.text();
    }

    /**
     * @return time of the LogRecord
     */
    @SuppressWarnings("AutoBoxing")
    public Long time() {
        return time;
    }

    @SuppressWarnings("AutoBoxing")
    @Override
    public String toString() {
        return c("LogRecord [data=", data, "time=", time, "]"); //NON-NLS
    }
}
