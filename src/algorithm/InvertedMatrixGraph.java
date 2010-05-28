package algorithm;

import java.util.ArrayList;
import java.util.List;

public class InvertedMatrixGraph<T> {

	/**
	 * La clase BitRow. Si bien podria utilizarse con verdaderos bits, esto
	 * limitaria la cantidad de vertices a 32.
	 */
	protected class BitRow {

		private boolean[] bits;
		private List<Integer> infoList;

		public BitRow(int size, int info) {
			this.bits = new boolean[size];
			this.infoList = new ArrayList<Integer>();
			if (info >= 0)
				this.infoList.add(info);
			for (int i = 0; i < size; i++)
				bits[i] = true;
		}

		public BitRow(int size, List<Integer> info) {
			this(size, -1);
			infoList.addAll(info);
		}

		public void addEdge(int other) {
			bits[other] = false;
		}

		public List<Integer> getInfo() {
			return this.infoList;
		}

		public boolean get(int index) {
			return this.bits[index];
		}

		public void set(int index, boolean b) {
			bits[index] = b;
		}

		/**
		 * Y logico. No solo hace un y logico de los bits, sino que tambien hace
		 * una union de los conjuntos.
		 */
		public BitRow and(BitRow other) {
			List<Integer> elements = new ArrayList<Integer>();
			elements.addAll(this.getInfo());
			elements.addAll(other.getInfo());
			BitRow out = new BitRow(bits.length, elements);
			for (int i = 0; i < bits.length; i++)
				out.set(i, bits[i] && other.get(i));
			return out;
		}

		/**
		 * Verifica si existen 1's a la derecha. Esto servira luego para podar
		 * el codigo. (Si no existen 1's a la derecha, entonces termina una
		 * ramificacion)
		 */
		public boolean existsRightOnes(int index) {
			if (index + 1 > bits.length)
				return false;
			for (int i = index + 1; i < bits.length; i++)
				if (bits[i])
					return true;
			return false;
		}

		/**
		 * Verifica consistencia del bitrow.
		 */
		public boolean isConsistent() {
			for (int i : infoList) {
				if (!bits[i])
					return false;
			}
			return true;
		}

		public String toString() {
			String out = "" + infoList.toString() + ": ";
			for (int i = 0; i < bits.length; i++)
				// 0 and 1 for better display
				out += (bits[i] == true ? 1 : 0) + " ";

			out += "(consistent: " + isConsistent() + ")";
			return out;
		}
	}

	private List<BitRow> rows; // la matriz
	private Graph<T> graph;
	private int vertexCount;
	// Una lista de grupos que contienen posibles clases de equivalencia.
	private List<List<List<Integer>>> possible;

	// La verdadera lista de clases de equivalencia. Este es el coloreo minimo.
	private List<List<T>> minimal;

	public InvertedMatrixGraph(Graph<T> graph) {
		this.graph = graph;
		this.rows = new ArrayList<BitRow>();
		this.vertexCount = graph.vertexCount();
		this.possible = new ArrayList<List<List<Integer>>>();

		addVertices();
		addEdges();

	}

	/**
	 * Agrega los vertices del grafo a la matriz de adyacencia invertida. Nota:
	 * los conjuntos de cada BitRow son los indices de un DFS. El orden no es
	 * importante, pero necesita ser consistente. Esto esta implicito en el
	 * codigo del algoritmo, pero cuando mapea vertices con colores, lo va a
	 * necesitar.
	 */
	private void addVertices() {
		for (int i = 0; i < vertexCount; i++)
			rows.add(new BitRow(vertexCount, i));
	}

	/**
	 * Agrega aristas mediante la negacion de entradas en la matriz. Si bien es
	 * simetrica para grafos no dirigidos, se decidio no podarlo, por si se
	 * llegara a utilizar con grafos dirigidos.
	 */
	private void addEdges() {
		List<T> nodes = graph.DFS();

		for (int i = 0; i < vertexCount; i++)
			for (int j = 0; j < vertexCount; j++)
				// If there is an edge between vertex I and J
				if (graph.neighbors(nodes.get(i)).contains(nodes.get(j))) {
					rows.get(i).addEdge(j);
				}
	}

	/**
	 * Imprime en pantalla la matriz de adyacencia invertida.
	 */
	public void print() {
		for (BitRow br : rows)
			System.out.println(br);
	}

	/**
	 * Halla todas las clases de equivalencia posibles, partiendo desde cada
	 * vertice. Si halla una combinacion consistente, verifica nuevamente con
	 * todos los vertices (pero ignora los que ya visito hasta llegar a esa
	 * combinacion, ya que se sabe que o no son consistentes, o ya se llegaron a
	 * resultados)
	 */
	private boolean getRamification(BitRow actual, int index,
			List<List<Integer>> group) {

		boolean last = true;
		for (int i = index + 1; i < vertexCount; i++) {
			BitRow next = actual.and(rows.get(i));

			if (next.isConsistent()) {
				// Ir a la proxima ramificacion, SOLO si hay alguna posible
				// combinacion
				if (next.existsRightOnes(i)) {
					getRamification(next, i, group);
					// Pero esta ramifiacacion no es la ultima, asi que
					// continuar
					last = false;
				}
				// Agregar esta posible clase de equivalencia.
				group.add(next.getInfo());
			}
		}
		return last;
	}

	/**
	 * Crea un grupo para el i-esimo vertice, luego buscar clases de
	 * equivalencia posibles. Agregar una clase de equivalencia con un unico
	 * vertice, por si hay colores aislados (aunque sea conexo).
	 */
	private void getEquivalenceClasses() {
		for (int i = 0; i < vertexCount; i++) {
			possible.add(new ArrayList<List<Integer>>());
			possible.get(i).add(new ArrayList<Integer>());
			possible.get(i).get(0).add(i);
			getRamification(rows.get(i), i, possible.get(i));
		}
	}

	/**
	 * Este metodo se encarga de llamar a todos los pasos del algoritmo de
	 * coloreo.
	 */
	public List<List<T>> getMinimalColoring() {
		graph.clearColors();
		this.getEquivalenceClasses();
		this.searchMinimalColoring();

		return minimal;
	}

	/**
	 * El concepto de este metodo es: empezar combinando todas las posibles
	 * clases de equivalencia agrupadas por vertice. Luego, encontrar el
	 * conjunto de clases de equivalencia que tenga minima cantidad de clases
	 * pero todos los vertices.
	 */
	private void searchMinimalColoring() {

		List<List<List<Integer>>> combinedClasses = new ArrayList<List<List<Integer>>>();
		combineClasses(0, new ArrayList<List<Integer>>(), combinedClasses);

		List<List<Integer>> min = null;

		// Ahora se trata de encontrar el conjunto con minima cantidad de clases
		// de equivalencia, pero que contenga a TODOS los vertices.
		for (List<List<Integer>> combination : combinedClasses) {

			int sum = 0;
			for (List<Integer> eqClass : combination)
				sum += eqClass.size();

			if (sum == vertexCount
					&& (min == null || combination.size() < min.size()))
				min = combination;
		}

		// Almacenar los nodos, y mapear los indices del DFS a los vertices
		List<T> nodes = graph.DFS();
		minimal = new ArrayList<List<T>>();

		for (int i = 0; i < min.size(); i++) {
			minimal.add(new ArrayList<T>());
			for (int j = 0; j < min.get(i).size(); j++)
				minimal.get(i).add(nodes.get(min.get(i).get(j)));
		}

		int color = 0;
		for (List<T> colorClass : minimal) {
			for (T info : colorClass)
				graph.setColor(info, color);
			color++;
		}
	}

	/**
	 * Notar que mientras el algoritmo itera sobre cada grupo, esta garantizado
	 * que cualquier grupo futuro no va a contener al vertice caracteristico de
	 * este grupo. Esto significa que el ultimo grupo que puede llegar a
	 * contener el vertice i-esimo es el i-esimo grupo. Esto se debe al
	 * algoritmo recursivo que halla todas las posibles clases de equivalencia.
	 */
	private void combineClasses(int i, List<List<Integer>> ramification,
			List<List<List<Integer>>> combinedClasses) {

		// Si no hay mas clases de equivalencia, agrega las resultantes. (caso
		// base)
		if (i >= possible.size() || possible.get(i).isEmpty()) {
			combinedClasses.add(ramification);
			return;
		} else {
			for (List<Integer> eqClass : possible.get(i)) {

				/*
				 * Checkea si este vertice ya fue usado. Si ese es el caso,
				 * entonces ignora esta ramificacion.
				 */
				boolean exists = false;
				for (Integer v : eqClass)
					for (List<Integer> list : ramification)
						for (Integer w : list)
							if (v == w) {
								exists = true;
							}

				// Agregar esta clase de equivalencia
				List<List<Integer>> next = new ArrayList<List<Integer>>();
				next.addAll(ramification);
				if (!exists)
					next.add(eqClass);

				// Profundizar en el arbol
				combineClasses(i + 1, next, combinedClasses);
			}
		}
	}
}
