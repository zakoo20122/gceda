import java.io.IOException;

public class GraphColoring {

	public static void main(String args[]) {
		try {
			if (args.length < 3) {
				System.out
						.println("Ingrese el archivo a cargar y el metodo elegido");
				return;
			}
			Graph<String> graph = GraphLoader.loadGraph(args[1]);
			String method = args[2];
			if (method.equals("exact")) {
				boolean tree = false;
				if (args.length == 4) {
					if (args[3].equals(tree))
						tree = true;
					else
						System.out
								.println("Error. El tercer argumento debe ser \"tree\" o null");
				}
				graph.exactColoring(tree);
			} else if (method.equals("greedy")) {
				graph.greedyColoring();
			} else if (method.equals("ts")) {
				graph.tsColoring();
			} else {
				System.out
						.println("Error. Debe especificarse algun de los siguientes metodos : exact, greedy o ts.");
				return;
			}
		} catch (IOException e) {
			System.out.println("Error de archivo.");
		}
	}
}
