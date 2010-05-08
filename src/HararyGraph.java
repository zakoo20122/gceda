import java.util.ArrayList;
import java.util.List;

/**
 * Harary graph
 * 
 * @author Mariano
 * 
 */
public class HararyGraph extends Graph<Integer> {

	// OJO, ANDA MAL EN ALGUNOS CASOS - REVISAR
	public HararyGraph(int k, int n) {
		List<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < n; i++)
			values.add(i);

		// Build a Cn
		this.addVertex(0);
		for (int i = 1; i < n; i++) {
			this.addVertex(i);
			this.addEdge(i, i - 1);
		}
		this.addEdge(0, n - 1);

		// Build first pass
		int r = k / 2;
		for (int i = 0; i < n - 1; i++)
			for (int j = i + 1; j < n; j++)
				if (j - i <= r || n + i - j <= r)
					this.addEdge(i, j);

		// Second pass
		if (k % 2 != 0) {
			if (n % 2 == 0) {
				for (int i = 0; i < (n / 2); i++)
					this.addEdge(i, i + (n / 2));
			} else {
				this.addEdge(0, (n - 1) / 2);
				this.addEdge(0, (n + 1) / 2);
				for (int i = 0; i < (n - 3) / 2; i++)
					this.addEdge(i, i + (n + 1) / 2);
			}
		}
	}
}
