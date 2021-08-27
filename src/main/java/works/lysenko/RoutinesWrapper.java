package works.lysenko;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * This is a set of delegates to be used for usage in Scenarios
 * 
 * @author Sergii Lysenko
 *
 */
public class RoutinesWrapper {

	public Run r;

	protected RoutinesWrapper() {
		r = null;
	}

	protected RoutinesWrapper(Run r) {
		this.r = r;
	}

	public By by(String l) {
		return Routines.by(l);
	}

	public void click(String l) {
		Routines.click(l, r);
	}

	public void click(WebElement e) {
		Routines.click(e, r);
	}

	public String colorize(String s) {
		return Routines.colorize(s);
	}

	public String colorize(String s, Color c) {
		return Routines.colorize(s, c);
	}

	public String fill(String template, Object... values) {
		return Routines.fill(template, values);
	}

	public WebElement find(String l) {
		return Routines.find(l, r);
	}

	public WebElement find(String parent, String child) {
		return Routines.find(parent, child, r);
	}

	public List<WebElement> findS(String l) {
		return Routines.findS(l, r);
	}

	public void log(String l) {
		Routines.log(l, r);
	}

	public void log(int i, String l) {
		Routines.log(i, l, r);
	}

	public void log(int i, String l, long t) {
		Routines.log(i, l, r, t);
	}

	public void logWrite(String l) {
		Routines.log(l, r);
	}

	public void makeScreenshot(String name) {
		Routines.makeScreenshot(name, r);
	}

	public void makeScreenshot(WebElement element, String name) {
		Routines.makeScreenshot(element, name, r);
	}

	public void open(String p, String u) {
		Routines.open(p, u, r);
	}

	public void openDomain(String u) {
		Routines.openDomain(u, r);
	}

	public String read(String l) {
		return Routines.read(l, r);
	}

	public void sendKeys(String l, CharSequence s) {
		Routines.sendKeys(l, s, r);
	}

	public void sleep(int i) {
		Routines.sleep(i);
	}

	public void sleep(int i, boolean s) {
		Routines.sleep(i, s);
	}

	public void typeIn(String l, String c) {
		Routines.typeIn(l, c, r);
	}

	public void typeIn(String l, String c, boolean secret) {
		Routines.typeIn(l, c, secret, r);
	}

	public void wait(String l) {
		Routines.wait(l, r);
	}

	public void waitValue(String l, String s) {
		Routines.waitValue(l, s, r);
	}

	public String waitValueNot(String l, String s) {
		return Routines.waitValueNot(l, s, r);
	}

	public String waitValueNotEmpty(String l) {
		return Routines.waitValueNotEmpty(l, r);
	}

}