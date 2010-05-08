import java.util.List;

/**
 * A complete graph.
 * @author Mariano
 *
 * @param <V>
 */
public class Kn<V> extends Graph<V> {

	public Kn(List<V> values) {
		super();

		// Add vertices
		for (int i = 0; i < values.size(); i++) {
			this.addVertex(values.get(i));
		}

		// Add edges
		for (int i = 0; i < values.size(); i++) {
			for (int j = 0; j < values.size(); j++)
				if (j != i)
					this.addEdge(values.get(i), values.get(j));
		}
	}

}
