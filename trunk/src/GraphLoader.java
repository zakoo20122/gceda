import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GraphLoader {

	public static Graph<String> loadGraph(String fileName) throws IOException {
		BufferedReader input = null;
		try {
			File file = new File(fileName);
			input = new BufferedReader(new FileReader(file));
			String line;
			Graph<String> graph = new Graph<String>();
			while ((line = input.readLine()) != null) {
				int separator = line.indexOf(",");
				// Verifica si hay una unica ',' en la linea
				if (separator != -1 && separator == line.lastIndexOf(",")) {
					String vertex1 = line.substring(0, separator);
					String vertex2 = line.substring(separator, line.length());
					graph.addVertex(vertex1);
					graph.addVertex(vertex2);
					graph.addEdge(vertex1, vertex2);
				}
			}
			return graph;

		} catch (IOException exc) {
			return null;
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}
}
