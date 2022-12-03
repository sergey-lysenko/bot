package works.lysenko.utils;

/**
 * @author Sergii Lysenko
 */
@SuppressWarnings({"PublicMethodNotExposedInInterface", "ClassHasNoToStringMethod", "ClassWithoutLogger", "PublicMethodWithoutLogging", "ClassWithTooManyTransitiveDependents"})
public class Stopwatch {
    private final long start;

    /**
     * Create and start the Stopwatch instance
     */
    @SuppressWarnings("PublicConstructor")
    public Stopwatch() {
        start = System.currentTimeMillis();
    }

    /**
     * @return milliseconds since Stopwatch instantiation
     */
    public long millis() {
        return System.currentTimeMillis() - start;
    }

    /**
     * @return readable string of Stopwatch uptime
     */
    @SuppressWarnings("unused")
    public String read() {
        return millis() + " ms";
    }

    /**
     * @return moment of Stopwatch instantiation
     */
    public long startedAt() {
        return start;
    }
}
