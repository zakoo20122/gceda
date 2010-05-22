import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ExactColoring<T> extends Coloring<T> {

	private List<State<T>> colored;
	/*
	 * Contiene en cada elemento la cantidad de nodos coloreados con el color
	 * i-esimo
	 */
	private List<Integer> quantColor;
	/*
	 * Contiene la cantidad de colores distintos usados en el grafo, 0 en el
	 * caso de que el mismo no este completamente coloreado o si el grafo esta
	 * vacio
	 */
	private int usedColors;

	
	public ExactColoring(Graph<T> graph){
		this.graph = graph;
		this.colored = new ArrayList<State<T>>();
		this.usedColors = 0;
		this.quantColor = new ArrayList<Integer>();
	}
	
	public Graph<State<T>> perfectColoring() {
		// Lista de colores disponibles
		List<Integer> available = new ArrayList<Integer>();
		Graph<State<T>> tree = new Graph<State<T>>();
		available.add(0);
		perfectColoring(graph.DFS().get(0), available, tree);
		for (State<T> state : colored) {
			graph.setColor(state.getInfo(), state.getColor());
		}
		return tree;
	}

	private State<T> perfectColoring(T info, List<Integer> available,
			Graph<State<T>> tree) {
		color(info, available);

		State<T> state = new State<T>(info, graph.getColor(info));
		// tree.addVertex(state);

		// System.out.println(node.info + ": " + node.color);
		for (T other : graph.DFS()) {
			// Si la cantidad de colores disponibles iguala o supera
			// la cantidad de colores usados en la mejor solucion
			// hasta ahora, no sigue verificando ese nodo
			if (usedColors != 0 && available.size() == usedColors) {
				discolor(info, available);
				return state;
			}
			// Si no se coloreo aun
			else if (graph.getColor(other) == -1) {
				State<T> neighborState = perfectColoring(other, available, tree);
				// tree.addEdge(state, neighborState);
			}
		}

		// System.out.print("- used:" + usedColors);
		// System.out.println("- available:" + available.size());

		// Si coloreo todos los vertices y todavia no habia llegado a una
		// solucion o llego a una mejor que la que ya existia la reemplaza
		if (hasAllColored()
				&& (usedColors == 0 || available.size() < usedColors)) {
			backUp();
		}
		discolor(info, available);
		return state;
	}

	/*
	 * Verifica si coloreo a todos los nodos
	 */
	private boolean hasAllColored() {
		int vertexCount = graph.vertexCount();
		int acum = 0;
		for (int color : quantColor) {
			acum += color;
		}
		return acum == vertexCount;
	}

	@Override
	protected void color(T info, List<Integer> available) {
		super.color(info, available);
		int newColor = graph.getColor(info);
		if (newColor >= quantColor.size())
			quantColor.add(0);
		quantColor.set(newColor, quantColor.get(newColor) + 1);
	}

	private void discolor(T info, List<Integer> available) {
		int oldColor = graph.getColor(info);
		graph.setColor(info, -1);
		quantColor.set(oldColor, quantColor.get(oldColor) - 1);
		updateAvailable(oldColor, available);
	}

	/*
	 * Contiene por cada nodo, su informacion y su color
	 */
	private void backUp() {
		colored.clear();
		Set<Integer> distinctColors = new HashSet<Integer>();
		for (T info : graph.DFS()) {
			colored.add(new State<T>(info, graph.getColor(info)));
			distinctColors.add(graph.getColor(info));
		}
		usedColors = distinctColors.size();
	}

	/*
	 * Verifica si el color del nodo que sera descoloreado no se usa mas y en
	 * ese caso lo saca de available
	 */
	private void updateAvailable(int color, List<Integer> available) {
		if (available.size() != 1 && quantColor.get(color) == 0)
			available.remove((Object) color);
	}
}
