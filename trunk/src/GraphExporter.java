import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class GraphExporter {

	public static <T> void exportGraph(String fileName, Graph<T> graph)
			throws IOException {
		BufferedWriter output = null;
		try {
			File file = new File(fileName);
			output = new BufferedWriter(new FileWriter(file));

			output.write("graph G { ");
			output.newLine();
			output.write("graph [splines = true] "
					+ "node [height=0.4 shape=circle style=filled]");
			output.newLine();

			List<T> nodes = graph.DFS();
			HashMap<T, List<T>> outGraph = new HashMap<T, List<T>>();

			/*
			 * Remove edge redundancy
			 */
			for (T node : nodes) {
				List<T> neighbors = graph.neighbors(node);
				for (T neighbor : neighbors) {
					if (outGraph.containsKey(neighbor)) {
						List<T> other = outGraph.get(neighbor);
						other.remove(node);
					}
				}
				outGraph.put(node, neighbors);
			}

			/*
			 * Get data for exporting
			 */
			Set<Entry<T, List<T>>> entries = outGraph.entrySet();

			// First declare nodes and its colors
			for (Entry<T, List<T>> entry : entries) {
				String line = entry.getKey() + " [ color= "
						+ getColorName(graph.getColor(entry.getKey())) + "] ;";
				output.write(line);
				output.newLine();
			}

			for (Entry<T, List<T>> entry : entries) {
				for (T neighbor : entry.getValue()) {
					String line = "";
					line += entry.getKey() + " -- " + neighbor + ";";
					output.write(line);
					output.newLine();
				}
			}

			output.write("}");

		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	private static String getColorName(int color) {
		String out = "";

		switch (color) {
		case 0:
			out = "azure4";
			break;
		case 1:
			out = "crimson";
			break;
		case 2:
			out = "pink";
			break;
		case 3:
			out = "navyblue";
			break;
		case 4:
			out = "limegreen";
			break;
		case 5:
			out = "sienna";
			break;
		case 6:
			out = "indigo";
			break;
		case 7:
			out = "darkorange";
			break;
		case 8:
			out = "gold";
			break;
		case 9:
			out = "darkturquoise";
			break;
		default:
			out = "white";
			break;

		}
		return out;
	}
}
