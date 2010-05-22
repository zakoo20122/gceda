import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvertedMatrixGraph<T> {

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

		public BitRow and(BitRow other) {
			List<Integer> elements = new ArrayList<Integer>();
			elements.addAll(this.getInfo());
			elements.addAll(other.getInfo());
			BitRow out = new BitRow(bits.length, elements);
			for (int i = 0; i < bits.length; i++)
				out.set(i, bits[i] && other.get(i));
			return out;
		}

		public boolean existsRightOnes(int index) {
			if (index + 1 > bits.length)
				return false;
			for (int i = index + 1; i < bits.length; i++)
				if (bits[i])
					return true;
			return false;
		}

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

	private List<BitRow> rows;
	private Graph<T> graph;
	private int vertexCount;
	// A list of groups which contain possible equivalence classes
	private List<List<List<Integer>>> possible;

	// The real list of equivalence classes. This is the minimal coloring
	private List<List<T>> minimal;

	public static void main(String args[]) {

		try {
			long init;

			List<String> values = new ArrayList<String>();
			int size = 100;
			for (int i = 0; i < size; i++)
				values.add(String.valueOf(i));

			init = (new Date()).getTime();
			RandomGraph rn;
			Kn<String> kn = new Kn<String>(values);
			Cn<String> cn = new Cn<String>(values);
			InvertedMatrixGraph<String> matrix;

			for (int i = 0; i < 100; i++) {
				rn = new RandomGraph(8, 15);
				System.out.println("Conexo? " + rn.isConnected());
				matrix = new InvertedMatrixGraph<String>(rn);
				init = (new Date()).getTime();
				int chromaticBit = matrix.getMinimalColoring().size();
				System.out.println("Numero cromático BITS: " + chromaticBit);
				System.out.println("Tiempo BITS: "
						+ ((new Date()).getTime() - init));

				GraphExporter.exportGraph("bits", rn);

				init = (new Date()).getTime();
				rn.greedy();
				int chromaticGREEDY = rn.getChromaticNumber();
				System.out.println("Numero cromático GREEDY: "
						+ chromaticGREEDY);
				System.out.println("Tiempo GREEDY: "
						+ ((new Date()).getTime() - init));

				GraphExporter.exportGraph("greedy", rn);

				init = (new Date()).getTime();
				rn.exactColoring();
				int chromaticEC = rn.getChromaticNumber();
				System.out.println("Numero cromático EC: " + chromaticEC);
				System.out.println("Tiempo EC: "
						+ ((new Date()).getTime() - init));

				GraphExporter.exportGraph("ec", rn);
				System.out.println("");

				if (chromaticBit != chromaticEC) {
					i = 1001;
					System.out
							.println("WTFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public InvertedMatrixGraph(Graph<T> graph) {
		this.graph = graph;
		this.rows = new ArrayList<BitRow>();
		this.vertexCount = graph.vertexCount();
		this.possible = new ArrayList<List<List<Integer>>>();

		addVertices();
		addEdges();

	}

	/**
	 * Adds vertices to the inverted matrix. Note: the label(s) of each BitRow
	 * are the indices of a DFS. The order is not important, but it needs to be
	 * consistent. It is implicit on the actual processing code, but when
	 * exporting the matrix, it will be needed.
	 */
	private void addVertices() {
		for (int i = 0; i < vertexCount; i++)
			rows.add(new BitRow(vertexCount, i));
	}

	/**
	 * Adds edges by negating entries in the matrix. Because its an inverted
	 * adjacency matrix, its symmetrical.
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

	public void print() {
		for (BitRow br : rows)
			System.out.println(br);
	}

	public boolean getRamification(BitRow actual, int index,
			List<List<Integer>> group) {

		boolean last = true;
		for (int i = index + 1; i < vertexCount; i++) {
			BitRow next = actual.and(rows.get(i));

			if (next.isConsistent()) {
				// Go to next ramification, ONLY if there is any future possible
				// combination
				if (next.existsRightOnes(i)) {
					getRamification(next, i, group);
					// But this ramification isn't the last, so return false
					last = false;
				}
				// Add this possible class
				group.add(next.getInfo());
			}
		}
		return last;
	}

	public void getEquivalenceClasses() {

		// Create a group for the i-th vertex, then search for equivalence
		// classes. Add each vertex as its own equivalence class.
		for (int i = 0; i < vertexCount; i++) {
			possible.add(new ArrayList<List<Integer>>());
			possible.get(i).add(new ArrayList<Integer>());
			possible.get(i).get(0).add(i);
			getRamification(rows.get(i), i, possible.get(i));
		}
	}

	public List<List<T>> getMinimalColoring() {
		graph.clearColors();
		this.getEquivalenceClasses();
		this.searchMinimalColoring();

		for (T t : graph.DFS())
			if (graph.neighborsColor(t).size() != 0)
				System.out.println("ANDA MAL");
		return minimal;
	}

	/**
	 * The concept of this method is: start by combining the possible
	 * equivalence classes grouped by vertex. Then, find the list of equivalence
	 * classes that has minimum classes but maximum quantity of vertices.
	 */
	private void searchMinimalColoring() {

		List<List<List<Integer>>> combinedClasses = new ArrayList<List<List<Integer>>>();
		combineClasses(0, new ArrayList<List<Integer>>(), combinedClasses);

		List<List<Integer>> min = null;

		// Now it is a matter of finding the minimal set with all vertices
		for (List<List<Integer>> combination : combinedClasses) {

			int sum = 0;
			for (List<Integer> eqClass : combination)
				sum += eqClass.size();

			if (sum == vertexCount
					&& (min == null || combination.size() < min.size()))
				min = combination;
		}

		// Get the nodes, and now map the indices of DFS to the vertices
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
	 * Note that as the algorithm iterates over each group, it is guaranteed
	 * that any further group won't contain the characteristic vertex of that
	 * group. That is, the last group which may contain the I-eth vertex is the
	 * I-eth group. This is due to the recursive algorithm that finds all
	 * possible equivalence classes
	 */
	private void combineClasses(int i, List<List<Integer>> ramification,
			List<List<List<Integer>>> combinedClasses) {

		// If there are no more eq. classes, add the resulting equivalence class
		if (i >= possible.size() || possible.get(i).isEmpty()) {
			combinedClasses.add(ramification);
			return;
		} else {
			for (List<Integer> eqClass : possible.get(i)) {

				/*
				 * Check if this vertex has already been used. If that's the
				 * case, ignore this branch. This is useful to get to the
				 * isolated vertices, exploring further combinations.
				 */
				boolean exists = false;
				for (Integer v : eqClass)
					for (List<Integer> list : ramification)
						for (Integer w : list)
							if (v == w) {
								exists = true;
							}

				// Add this eq Class
				List<List<Integer>> next = new ArrayList<List<Integer>>();
				next.addAll(ramification);
				if (!exists)
					next.add(eqClass);

				// Go deeper in the tree.
				combineClasses(i + 1, next, combinedClasses);
			}
		}
	}
}
