import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestIO {

	public static void main(String args[]) {

		try {
			Graph<String> graph = GraphLoader.loadGraph("grafo1.graph");

			List<String> values = new ArrayList<String>();
			int size = 20;
			for (int i = 0; i < size; i++)
				values.add(String.valueOf(i));

			Kn<String> kn = new Kn<String>(values);
			Cn<String> cn = new Cn<String>(values);
			WheelGraph<String> wn = new WheelGraph<String>(values, "Centro");
			RandomGraph rn = new RandomGraph(100, 300);
			HararyGraph hn = new HararyGraph(35, 50);

			GraphExporter.exportGraph("outRandom", rn);
			GraphExporter.exportGraph("outKn", kn);
			GraphExporter.exportGraph("outCicle", cn);
			GraphExporter.exportGraph("outWheel", wn);
			GraphExporter.exportGraph("out", graph);
			GraphExporter.exportGraph("outHarary", hn);

		} catch (IOException e) {
			System.out.println(e);
		}

	}
}
