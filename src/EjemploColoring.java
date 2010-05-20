import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EjemploColoring {

	public static void main(String args[]) throws IOException{
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < 4; i++)
			values.add(String.valueOf(Character.toChars(65+i)));

		
		Graph<String> g = new RandomGraph(9,10);
		GraphExporter.exportGraph("descoloreado.dot", g);
		long time = System.currentTimeMillis();
		g.perfectColoring();
		System.out.println(System.currentTimeMillis()-time);
		g.printColors();
		GraphExporter.exportGraph("asdasdasd.dot", g);
		System.out.println("");
		//GraphExporter.exportGraph("tree.dot", tree);
	}
}
