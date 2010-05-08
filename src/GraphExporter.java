import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class GraphExporter {

	public static void exportGraph(String fileName, Graph<String> graph)
			throws IOException {
		BufferedWriter output = null;
		try {
			File file = new File(fileName);
			output = new BufferedWriter(new FileWriter(file));

			output.write("graph G { ");
			output.newLine();
			output.write("graph [splines = true] "
					+ "node [height=0.4 fixedsize=true shape=circle]");
			output.newLine();

			List<String> nodes = graph.DFS();
			HashMap<String, List<String>> outGraph = new HashMap<String, List<String>>();

			/*
			 * Remove edge redundancy
			 */
			for (String node : nodes) {
				List<String> neighbors = graph.neighbors(node);
				for (String neighbor : neighbors) {
					if (outGraph.containsKey(neighbor)) {
						List<String> other = outGraph.get(neighbor);
						other.remove(node);
					}
				}
				outGraph.put(node, neighbors);
			}

			/*
			 * Get data for exporting
			 */
			Set<Entry<String, List<String>>> entries = outGraph.entrySet();

			for (Entry<String, List<String>> entry : entries) {
				for (String neighbor : entry.getValue()) {
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
}
