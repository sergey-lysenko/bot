package works.lysenko.bot;

import works.lysenko.Cycles;
import works.lysenko.Execution;
import works.lysenko.scenarios.Google;
import works.lysenko.C;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.logging.LogType;

import java.util.Set;

@Disabled
public class GoogleTest {

	private static Execution x;
	private static Cycles cycles;
	private static Set<String> logs = Set.of(LogType.BROWSER, LogType.CLIENT, LogType.DRIVER, LogType.PERFORMANCE,
			LogType.PROFILER, LogType.SERVER);

	@BeforeAll
	public static void setupTest() {
		x = new Execution(1, 30, logs, C.TEST);
		cycles = new Cycles(Set.of(new Google(x)), x);
	}

	@AfterAll
	public static void quit() {
		x.complete();
	}

	public static void init(Execution xun) {
	}

	@Test
	public void test() throws InterruptedException {
		init(x);
		cycles.execute();
	}
}