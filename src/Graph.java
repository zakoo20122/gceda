import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Graph<V> {

	private class Node {
		public V info;
		public boolean visited;
		public int color;
		public List<Node> adj;

		public Node(V info) {
			this.info = info;
			this.visited = false;
			this.adj = new ArrayList<Node>();
			this.color = -1;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((info == null) ? 0 : info.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Node other = (Node) obj;
			if (info == null) {
				if (other.info != null)
					return false;
			} else if (!info.equals(other.info))
				return false;
			return true;
		}
	}

	private HashMap<V, Node> nodes;
	private List<State<V>> colored;
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

	public Graph() {
		this.nodes = new HashMap<V, Node>();
		this.colored = new ArrayList<State<V>>();
		this.usedColors = 0;
		this.quantColor = new ArrayList<Integer>();
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public void addVertex(V vertex) {
		if (!nodes.containsKey(vertex)) {
			nodes.put(vertex, new Node(vertex));
		}
	}

	public void addEdge(V v, V w) {
		Node origin = nodes.get(v);
		Node dest = nodes.get(w);
		if (origin != null && dest != null && !origin.equals(dest)) {
			if (!origin.adj.contains(dest)) {
				origin.adj.add(dest);
				dest.adj.add(origin);
			}
		}
	}

	public int edgeCount() {
		int count = 0;
		for (Node n : getNodes())
			count += n.adj.size();
		count /= 2;
		return count;
	}

	public void RemoveEdge(V v, V w) {
		Node origin = nodes.get(v);
		if (origin == null)
			return;
		Node dest = nodes.get(w);
		if (dest == null)
			return;
		origin.adj.remove(dest);
		dest.adj.remove(origin);
	}

	public boolean isEdge(V v, V w) {
		Node origin = nodes.get(v);
		if (origin == null)
			return false;

		for (Node neighbor : origin.adj) {
			if (neighbor.info.equals(w)) {
				return true;
			}
		}
		return false;
	}

	public List<V> neighbors(V v) {
		Node node = nodes.get(v);
		if (node != null) {
			List<V> l = new ArrayList<V>(node.adj.size());
			for (Node neighbor : node.adj) {
				l.add(neighbor.info);
			}
			return l;
		}
		return null;
	}

	public void removeVertex(V v) {
		Node node = nodes.get(v);
		if (node == null)
			return;

		// Primero removerlo de la lista de adyacencia de sus vecinos
		for (Node n : getNodes()) {
			if (!n.equals(node))
				n.adj.remove(node);
		}

		// Eliminar el nodo
		nodes.remove(v);
	}

	public int vertexCount() {
		return nodes.size();
	}

	private List<Node> getNodes() {
		List<Node> l = new ArrayList<Node>(vertexCount());
		Iterator<V> it = nodes.keySet().iterator();
		while (it.hasNext()) {
			l.add(nodes.get(it.next()));
		}
		return l;
	}

	public List<V> DFS() {
		return DFS(getNodes().get(0).info);
	}

	public List<V> DFS(V origin) {
		Node node = nodes.get(origin);
		if (node == null)
			return null;
		clearMarks();
		List<V> l = new ArrayList<V>();
		this.DFS(node, l);
		return l;
	}

	private void clearMarks() {
		for (Node n : getNodes()) {
			n.visited = false;
		}
	}

	private void DFS(Node origin, List<V> l) {
		if (origin.visited)
			return;
		l.add(origin.info);
		origin.visited = true;
		for (Node neighbor : origin.adj)
			DFS(neighbor, l);
	}

	public List<V> BFS(V origin) {
		Node node = nodes.get(origin);
		if (node == null)
			return null;
		clearMarks();
		List<V> l = new ArrayList<V>();

		Queue<Node> q = new LinkedList<Node>();
		q.add(node);
		while (!q.isEmpty()) {
			node = q.poll();
			l.add(node.info);
			node.visited = true;
			for (Node neighbor : node.adj) {
				if (!neighbor.visited) {
					q.add(neighbor);
				}
			}
		}
		return l;
	}

	public boolean isConnected() {
		if (isEmpty()) {
			return true;
		}
		clearMarks();
		List<Node> l = getNodes();
		List<V> laux = new ArrayList<V>();
		DFS(l.get(0), laux);
		for (Node node : l) {
			if (!node.visited) {
				return false;
			}
		}
		return true;
	}

	public int getColor(V info) {
		if (nodes.containsKey(info))
			return nodes.get(info).color;
		return -1;
	}

	public void setColor(V info, int color) {
		nodes.get(info).color = color;
	}

	public boolean hasNeighborColor(V info) {
		Node node = nodes.get(info);
		for (Node neighbor : node.adj) {
			if (node.color == neighbor.color)
				return true;
		}
		return false;
	}

	public Graph<State<V>> perfectColoring() {
		// Agrego un nodo ficticio unido a todos los demas
		addVertex(null);
		for (Node node : getNodes()) {
			addEdge(null, node.info);
		}
		// Lista de colores disponibles
		List<Integer> available = new ArrayList<Integer>();
		Graph<State<V>> tree = new Graph<State<V>>();
		available.add(0);
		perfectColoring(nodes.get(null), available, tree);
		removeVertex(null);
		for (State<V> state : colored) {
			nodes.get(state.getInfo()).color = state.getColor();
		}
		return tree;
	}

	private State<V> perfectColoring(Node node, List<Integer> available,
			Graph<State<V>> tree) {
		// Colorea si no es el nodo ficticio
		if (node.info != null)
			color(node, available);

		State<V> state = new State<V>(node.info, node.color);
		// tree.addVertex(state);

		// System.out.println(node.info + ": " + node.color);
		for (Node other : getNodes()) {
			// Si la cantidad de colores disponibles iguala o supera
			// la cantidad de colores usados en la mejor solucion
			// hasta ahora, no sigue verificando ese nodo
			if (usedColors != 0 && available.size() == usedColors) {
				discolor(node, available);
				return state;
			}
			// Si no es el ficticio y no se coloreo aun
			else if (other.info != null && other.color == -1) {
				State<V> neighborState = perfectColoring(other, available, tree);
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
		if (node.info != null)
			discolor(node, available);
		return state;
	}

	/*
	 * Verifica si coloreo a todos los nodos
	 */
	private boolean hasAllColored() {
		// Para omitir al ficticio
		int vertexCount = vertexCount() - 1;
		int acum = 0;
		for (int color : quantColor) {
			acum += color;
		}
		return acum == vertexCount;
	}

	private void color(Node node, List<Integer> available) {
		Set<Integer> disabled = new HashSet<Integer>();
		for (Node neighbor : node.adj) {
			if (neighbor.color != -1) {
				disabled.add(neighbor.color);
			}
		}
		int newColor = 0;
		if (disabled.size() == available.size()) {
			newColor = available.get(available.size() - 1) + 1;
			available.add(newColor);
			node.color = newColor;
		} else {
			int i = 0;
			while (node.color == -1) {
				newColor = available.get(i);
				if (!disabled.contains(newColor)) {
					node.color = newColor;
				}
				i++;
			}
		}
		if (newColor >= quantColor.size())
			quantColor.add(0);
		quantColor.set(newColor, quantColor.get(newColor) + 1);
	}

	private void discolor(Node node, List<Integer> available) {
		int color = node.color;
		node.color = -1;
		quantColor.set(color, quantColor.get(color) - 1);
		updateAvailable(color, available);
	}

	/*
	 * Contiene por cada nodo, su informacion y su color
	 */
	private void backUp() {
		colored.clear();
		Set<Integer> aux = new HashSet<Integer>();
		for (Node node : getNodes()) {
			if (node.info != null) {
				colored.add(new State<V>(node.info, node.color));
				aux.add(node.color);
			}
		}
		usedColors = aux.size();
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
