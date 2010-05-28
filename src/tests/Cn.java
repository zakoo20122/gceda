package tests;

import java.util.List;

import algorithm.Graph;

/**
 * A ciclyc graph
 * @author Mariano
 *
 * @param <T>
 */
public class Cn<T> extends Graph<T> {

	public Cn(List<T> values) {
		super();

		this.addVertex(values.get(0));
		for (int i = 1; i < values.size(); i++) {
			this.addVertex(values.get(i));
			this.addEdge(values.get(i), values.get(i - 1));
		}
		this.addEdge(values.get(0), values.get(values.size() - 1));

	}
}
