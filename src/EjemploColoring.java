import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EjemploColoring {

	public static void main(String args[]) throws IOException{
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < 4; i++)
			values.add(String.valueOf(Character.toChars(65+i)));

		
		Graph<String> g = new Cn<String>(values);
		long time = System.currentTimeMillis();
		Graph<State<String>> tree = g.perfectColoring();
		System.out.println(System.currentTimeMillis()-time);
		GraphExporter.exportGraph("asdasdasd.dot", g);
		//GraphExporter.exportGraph("tree.dot", tree);
	}
}
