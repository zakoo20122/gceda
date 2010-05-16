import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EjemploColoring {

	public static void main(String args[]) throws IOException{
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < 400; i++)
			values.add(String.valueOf(Character.toChars(65+i)));

		
		Graph<String> g = new Cn<String>(values);
		long time = System.currentTimeMillis();
		Graph<String> aux = g.perfectColoring();
		System.out.println(System.currentTimeMillis()-time);
		aux.printColors();
		GraphExporter.exportGraph("asdasdasd.dot", aux);
	}
}
