import java.util.ArrayList;
import java.util.List;


public class EjemploColoring {

	public static void main(String args[]){
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < 555; i++)
			values.add(String.valueOf(Character.toChars(65+i)));

		
		Graph<String> g = new Cn<String>(values);
		long time = System.currentTimeMillis();
		Graph<String> aux = g.perfectColoring();
		System.out.println(System.currentTimeMillis()-time);
		aux.printColors();
	}
}
