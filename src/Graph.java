import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
	private int chromaticNumber;

	public Graph() {
		this.nodes = new HashMap<V, Node>();
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

	/*
	 * Retorna el valor de los nodos vecinos si tiene el mismo color o null en
	 * caso que no exista ninguno
	 */
	public List<V> neighborsColor(V info) {
		List<V> ans = new ArrayList<V>();
		Node node = nodes.get(info);
		for (Node neighbor : node.adj) {
			if (node.color == neighbor.color)
				ans.add(neighbor.info);
		}
		return ans;
	}

	public void exactColoring() {
		clearColors();
		ExactColoring<V> ec = new ExactColoring<V>(this);
		ec.perfectColoring();
		this.chromaticNumber = ec.getKNumber();
	}

	public int getChromaticNumber() {
		return chromaticNumber;
	}

	public void clearColors() {
		for (V v : DFS())
			this.setColor(v, -1);
	}

	public void tabuSearch() {
		clearColors();
		TabuSearch<V> ts = new TabuSearch<V>(this);
		ts.coloring();
	}

}
