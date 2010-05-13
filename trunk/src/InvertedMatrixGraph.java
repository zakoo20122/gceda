import java.util.ArrayList;
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
			for (int i = index; i < bits.length; i++)
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
	private List<List<Integer>> equivalenceClasses;

	public static void main(String args[]) {

		List<String> values = new ArrayList<String>();
		int size = 5;
		for (int i = 0; i < size; i++)
			values.add(String.valueOf(i));

		Cn<String> cn = new Cn<String>(values);
		InvertedMatrixGraph<String> matrix = new InvertedMatrixGraph<String>(cn);
		matrix.print();

		System.out.println("");
		matrix.getEquivalenceClasses();
	}

	public InvertedMatrixGraph(Graph<T> graph) {
		this.graph = graph;
		this.rows = new ArrayList<BitRow>();
		this.vertexCount = graph.vertexCount();
		this.equivalenceClasses = new ArrayList<List<Integer>>();

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
					rows.get(j).addEdge(i);
				}
	}

	public void print() {
		for (BitRow br : rows)
			System.out.println(br);
	}

	public boolean getRamification(BitRow actual, int index) {

		boolean last = true;

		for (int i = index + 1; i < vertexCount; i++) {
			BitRow next = actual.and(rows.get(i));
			// System.out.println(next);

			if (next.isConsistent()) {
				// If next ramification is the last one, then add this eq. class
				if (getRamification(next, i)) {
					// System.out.println("LASSST" + next);
					equivalenceClasses.add(next.getInfo());
				}
				// But this ramification isn't the last, so return false
				last = false;
			}
		}

		return last;
	}

	public void getEquivalenceClasses() {
		for (int i = 0; i < vertexCount; i++) {
			getRamification(rows.get(i), i);
		}

		System.out
				.println("Posible equivalence classes: " + equivalenceClasses);
	}

}
