package works.lysenko;

import static works.lysenko.Constants.DEFAULT_RUNS_LOCATION;
import static works.lysenko.Constants.GENERATED_CONFIG_FILE;
import static works.lysenko.Constants.RUN_JSON_FILENAME;
import static works.lysenko.Constants.RUN_SVG_FILENAME;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;

import works.lysenko.output.Groups;
import works.lysenko.output.Parts;
import works.lysenko.utils.Ansi;

/**
 * @author Sergii Lysenko
 */
public class Output {

	/**
	 * 
	 */
	public Execution x;

	/**
	 * @param x
	 */
	public Output(Execution x) {
		super();
		this.x = x;
	}

	private void drawGroup(SVGGraphics2D g, int col, int[] dy, TreeMap<String, Result> sorted) {
		// TODO: get rid of this wtf method
		Groups t = new Groups("cycles", sorted);
		for (Entry<String, TreeMap<String, Result>> group : t.entrySet()) {
			drawGroup(g, null, col, dy, group);
		}
	}

	private void drawGroup(SVGGraphics2D g, Map<String, Integer> parentDys, int col, int[] dy,
			Entry<String, TreeMap<String, Result>> group) {
		Map<String, Integer> myDys = new HashMap<String, Integer>();
		Integer pDy = (null == parentDys) ? null : parentDys.get(group.getKey().toLowerCase());
		// Title
		dy[col]++;
		int tDy = dy[col]++;
		g.setColor(java.awt.Color.GRAY);

		int cW = 300; // Column Width
		int rH = 15; // Row Height

		if (null != pDy) {
			g.drawLine((col * cW) - 90, (pDy * rH) - 4, (col * cW) - 70, (pDy * rH) - 4);
			g.drawLine((col * cW) - 70, (pDy * rH) - 4, (col * cW) - 10, (tDy * rH) - 4);
			g.drawLine((col * cW) - 10, (tDy * rH) - 4, (col * cW) + 10, (tDy * rH) - 4);
		}

		g.setColor(java.awt.Color.BLACK);
		g.drawString(group.getKey(), (col * 300) + 15, tDy * 15);

		// Head
		Parts parts = Parts.flence(group.getValue());
		for (Entry<String, Result> sgr : parts.head.entrySet()) {
			if (sgr.getValue().executions == 0)
				g.setColor(java.awt.Color.GRAY);
			else if (sgr.getValue().problems.isEmpty())
				g.setColor(new Color(0, 128, 0));
			else
				g.setColor(new Color(255, 128, 0));
			int myDy = dy[col]++;
			myDys.put(sgr.getKey().toLowerCase().split(" ")[0], myDy);
			g.drawString(sgr.getKey() + " : " + sgr.getValue().toString(), (col * cW) + 15, myDy * rH);
		}

		// Body
		Groups groups = Groups.flence(parts.body);
		for (Entry<String, TreeMap<String, Result>> subgroup : groups.entrySet()) {
			drawGroup(g, myDys, (col + 1), dy, subgroup);
		}
	}

	protected double jsonify(Double i) {
		return (i == Double.POSITIVE_INFINITY) ? Double.MAX_VALUE : i;
	}

	/**
	 * This routine writes test execution in JSON format. Potentially will be
	 * replaced by some JSON libraries, if the amount of data to write will be more
	 * then manageable. File location defined by DEFAULT_RUNS_LOCATION, file name
	 * template defined by RUN_JSON_FILENAME
	 */
	protected void jsonStats() {
		// TODO: migrate to Gson (?)
		TreeMap<String, Result> sorted = x.r.getSorted();
		BufferedWriter w;
		try {
			w = new BufferedWriter(new FileWriter(name(RUN_JSON_FILENAME)));
			w.write("{\"startAt\":" + x.t.startedAt() + ",\"run\":[");
			int i = sorted.size();
			for (Entry<String, Result> s : sorted.entrySet()) {
				w.write("{\"scenario\":\"" + s.getKey() + "\"" + ",\"cWeight\": " + jsonify(s.getValue().confWeight)
						+ ",\"dWeight\": " + jsonify(s.getValue().permWeight) + ",\"uWeight\": "
						+ jsonify(s.getValue().pervWeight) + ",\"executions\":" + s.getValue().executions);
				if ((s.getValue().problems != null) && (s.getValue().problems.size() > 0)) {
					w.write(",\"problems\":[");
					int j = s.getValue().problems.size();
					for (Problem p : s.getValue().problems) {
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
						w.write("{\"time\":" + p.time + ",");
						if (shift.isEmpty()) {
							text = p.text.substring(type.length() + 1);
						} else {
							text = p.text.substring(shift.length() + type.length() + 4);
							w.write("\"shift\":" + shift + ",");
						}
						w.write("\"type\":\"" + type + "\",");
						w.write("\"text\":\"");
						w.write(text.replaceAll("\n", " ").replace("\"", "\\\""));
						w.write("\"}");
						if (j-- > 1)
							w.write(",");
					}
					w.write("]");
				}
				w.write("}");
				if (i-- > 1)
					w.write(",");
			}
			w.write("]}");
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String name(String t) {
		return DEFAULT_RUNS_LOCATION + Common.fill(t, String.valueOf(x.t.startedAt()));
	}

	/**
	 * Write test execution statistics only to console
	 */
	public void stats() {
		int total = x.cycles.scenarios.combinations(false);
		int active = x.cycles.scenarios.combinations(true);
		String persentage = new DecimalFormat("####0.0").format((Double.valueOf(active) / Double.valueOf(total)) * 100)
				+ "%";
		x.l.log(0, "= Execution statistics =");
		TreeMap<String, Result> sorted = x.r.getSorted();
		x.l.log(0, total + " paths were possible with current set of Scenarios");
		x.l.log(0, active + " (" + persentage + ") among these were allowed by current configuration");
		// TODO: add statistics of actually executed Node Scenarios
		if (sorted.entrySet().isEmpty())
			x.l.log(0, Ansi.colorize("[WARNING] No Test Execution Data available"));
		else
			for (Map.Entry<String, Result> e : sorted.entrySet()) {
				x.l.log(0, e.getKey() + " : " + e.getValue().toString());
			}
		x.l.logln();
	}

	protected void svgStats() {
		TreeMap<String, Result> sorted = x.r.getSorted(false, true);
		SVGGraphics2D g = new SVGGraphics2D(1920, 1080);

		int[] dy = new int[100];
		Arrays.fill(dy, 1);

		drawGroup(g, 0, dy, sorted);
		try {
			SVGUtils.writeToSVG(new File(Common.fill(name(RUN_SVG_FILENAME))), g.getSVGElement());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write default configuration to a file
	 * 
	 * @param defConf
	 * @param fileName
	 */
	public void writeDefConf(Set<String> defConf) {
		Common.writeToFile(null, defConf, GENERATED_CONFIG_FILE);
	}
}
