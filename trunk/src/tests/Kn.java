package tests;


import java.util.List;

import algorithm.Graph;

/**
 * A complete graph.
 * @author Mariano
 *
 * @param <V>
 */
public class Kn<V> extends Graph<V> {

	public Kn(List<V> values) {
		super();

		// Agregar vertices
		for (int i = 0; i < values.size(); i++) {
			this.addVertex(values.get(i));
		}

		// Agregar aristas
		for (int i = 0; i < values.size(); i++) {
			for (int j = 0; j < values.size(); j++)
				if (j != i)
					this.addEdge(values.get(i), values.get(j));
		}
	}

}
