import java.util.List;

public class Kn<V> extends Graph<V> {

	public Kn(int vertices, List<V> values) {
		super();

		// Add vertices
		for (int i = 0; i < vertices; i++) {
			this.addVertex(values.get(i));
		}

		// Add edges
		for (int i = 0; i < vertices; i++) {
			for (int j = 0; j < vertices; j++)
				if (j != i)
					this.addEdge(values.get(i), values.get(j));
		}
	}

}
