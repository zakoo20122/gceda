import java.io.IOException;

public class TestIO {

	public static void main(String args[]) {

		try {
			Graph<String> graph = GraphLoader.loadGraph("test.graph");
			
			GraphExporter.exportGraph("out.dot", graph);
			
		} catch (IOException e) {
			System.out.println(e);
		}

	}
}
