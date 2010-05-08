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
			HararyGraph hn = new HararyGraph(5, 8);

			GraphExporter.exportGraph("outRandom.dot", rn);
			GraphExporter.exportGraph("outKn.dot", kn);
			GraphExporter.exportGraph("outCicle.dot", cn);
			GraphExporter.exportGraph("outWheel.dot", wn);
			GraphExporter.exportGraph("out.dot", graph);
			GraphExporter.exportGraph("outHarary.dot", hn);

		} catch (IOException e) {
			System.out.println(e);
		}

	}
}
