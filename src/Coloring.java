import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Coloring<T> {

	protected Graph<T> graph;

	/*
	 * Contiene en cada elemento la cantidad de nodos coloreados con el color
	 * i-esimo
	 */
	protected List<Integer> nodesPerColor;

	/* Lista de colores disponibles */
	protected List<Integer> available;

	public Coloring(Graph<T> graph) {
		this.graph = graph;
		this.nodesPerColor = new ArrayList<Integer>();
		this.available = new ArrayList<Integer>();
	}

	/**
	 * Colorea un nodo intentando usar solamente los colores de available.
	 * 
	 * @param info
	 *            Nodo a colorear
	 * @param available
	 *            Colores disponibles
	 * @return true si pudo colorear usando solo available, false si tuvo que
	 *         ampliar available para colorear
	 */
	protected void color(T info) {
		Set<Integer> disabled = new HashSet<Integer>();
		for (T neighbor : graph.neighbors(info)) {
			if (graph.getColor(neighbor) != -1) {
				disabled.add(graph.getColor(neighbor));
			}
		}
		int newColor = 0;
		if (disabled.size() == available.size()) {
			newColor = available.get(available.size() - 1) + 1;
			available.add(newColor);
			graph.setColor(info, newColor);
		} else {
			int i = 0;
			while (graph.getColor(info) == -1) {
				newColor = available.get(i);
				if (!disabled.contains(newColor)) {
					graph.setColor(info, newColor);
				}
				i++;
			}
		}
		
		if (newColor == nodesPerColor.size())
			nodesPerColor.add(0);
		nodesPerColor.set(newColor, nodesPerColor.get(newColor) + 1);
	}

	protected void discolor(T info) {
		int oldColor = graph.getColor(info);
		graph.setColor(info, -1);
		nodesPerColor.set(oldColor, nodesPerColor.get(oldColor) - 1);
	}
}
