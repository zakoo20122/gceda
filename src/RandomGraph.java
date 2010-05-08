/**
 * Random Graph, for time complexity testing.
 * 
 * @author Mariano
 * 
 * @param <T>
 */

public class RandomGraph extends Graph<String> {

	public RandomGraph(int vertexCount, int edgeCount) {
		super();

		// Add vertices
		for (int i = 0; i < vertexCount; i++) {
			this.addVertex(String.valueOf(i));
		}

		// Add edges
		for (int i = 0; i < edgeCount; i++) {
			int v1 = (int) (Math.random() * vertexCount);
			int v2 = (int) (Math.random() * vertexCount);
			this.addEdge(String.valueOf(v1), String.valueOf(v2));
		}

	}
}
