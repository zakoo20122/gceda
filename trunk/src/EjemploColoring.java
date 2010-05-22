import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EjemploColoring {

	public static void main(String args[]) throws IOException {
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < 4; i++)
			values.add(String.valueOf(Character.toChars(65 + i)));

		Graph<String> g = new RandomGraph(9, 10);
		long time = System.currentTimeMillis();
		ExactColoring<String> ec = new ExactColoring<String>(g);
		Graph<TreeState<String>> tree = ec.perfectColoring();
		System.out.println(System.currentTimeMillis() - time);
		GraphExporter.exportGraph("asdasdasd", g);
		//GraphExporter.exportGraph("tree", tree);
	}
}
