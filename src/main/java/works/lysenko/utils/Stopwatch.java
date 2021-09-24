package works.lysenko.utils;

@SuppressWarnings("javadoc")
public class Stopwatch {
    private long start;

    public Stopwatch() {
        start = System.currentTimeMillis();
    }

    public long startedAt() {
        return start;
    }

    public String read() {
        return String.valueOf(millis()) + " ms";
    }

    public long millis() {
        return (System.currentTimeMillis() - start);
    }
}
