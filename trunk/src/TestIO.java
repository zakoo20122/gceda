import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestIO {

	public static void main(String args[]) {

		try {
			Graph<String> graph = GraphLoader.loadGraph("test.graph");

			List<String> values = new ArrayList<String>();
			int size = 20;
			for (int i = 0; i < size; i++)
				values.add(String.valueOf(i));

			Kn<String> kn = new Kn<String>(size, values);

			GraphExporter.exportGraph("outKn.dot", kn);
			GraphExporter.exportGraph("out.dot", graph);

		} catch (IOException e) {
			System.out.println(e);
		}

	}
}
