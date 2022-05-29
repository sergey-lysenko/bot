package works.lysenko.bot;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import works.lysenko.Cycles;
import works.lysenko.Execution;

@SuppressWarnings("javadoc")
public class BotTest {

	private static Execution x;
	private static Cycles cycles;

	@BeforeAll
	public static void setupTest() {
		x = new Execution();
		cycles = new Cycles(x);
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