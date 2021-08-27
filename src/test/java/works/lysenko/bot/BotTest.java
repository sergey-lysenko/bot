package works.lysenko.bot;

import works.lysenko.Run;
import works.lysenko.scenarios.Google;
import works.lysenko.C;
import works.lysenko.Constants;
import works.lysenko.Cycles;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.logging.LogType;

import java.util.Set;

public class BotTest {

	private static Run r;
	private static Cycles cycles;
	private static Set<String> logs = Set.of(LogType.BROWSER, LogType.CLIENT, LogType.DRIVER, LogType.PERFORMANCE,
			LogType.PROFILER, LogType.SERVER);

	@BeforeAll
	public static void setupTest() {
		r = new Run(1, 30, logs, C.TEST);
		cycles = new Cycles(Set.of(new Google(r)), r);
	}

	@AfterAll
	public static void quit() {
		r.complete();
	}

	public static void init(Run run) {
	}

	@Test
	public void test() throws InterruptedException {
		init(r);
		cycles.execute();
	}
}