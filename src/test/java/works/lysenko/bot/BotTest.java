package works.lysenko.bot;

import static org.openqa.selenium.logging.LogType.BROWSER;
import static org.openqa.selenium.logging.LogType.CLIENT;
import static org.openqa.selenium.logging.LogType.DRIVER;
import static org.openqa.selenium.logging.LogType.PERFORMANCE;
import static org.openqa.selenium.logging.LogType.PROFILER;
import static org.openqa.selenium.logging.LogType.SERVER;
import static works.lysenko.bot.Constants.TEST;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import works.lysenko.Cycles;
import works.lysenko.Execution;

@SuppressWarnings("javadoc")
public class BotTest {

	private static Execution x;
	private static Cycles cycles;
	private static Set<String> logs = Set.of(BROWSER, CLIENT, DRIVER, PERFORMANCE, PROFILER, SERVER);

	@BeforeAll
	public static void setupTest() {
		x = new Execution(1, 30, logs, TEST);
		cycles = new Cycles("works.lysenko.scenarios", x);
	}

	@AfterAll
	public static void quit() {
		x.complete();
	}

	public static void init(Execution x) {
	
	}

	@Test
	public void test() throws InterruptedException {
		init(x);
		cycles.execute();
	}
}