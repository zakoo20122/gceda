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

		if (edgeCount > ((vertexCount - 1) * vertexCount / 2))
			edgeCount = (vertexCount - 1) * vertexCount / 2;

		// Add vertices
		for (int i = 0; i < vertexCount; i++) {
			this.addVertex(String.valueOf(i));
		}

		// Add edges
		for (int i = 0; i < edgeCount; i++) {
			int v1;
			int v2;
			do {
				v1 = (int) (Math.random() * vertexCount);
				v2 = (int) (Math.random() * vertexCount);
			} while (v1 == v2);
			this.addEdge(String.valueOf(v1), String.valueOf(v2));
			if (i == edgeCount - 1 && !isConnected())
				i--;
		}

	}
}
