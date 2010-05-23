import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GreedyColoring<T> extends Coloring<T> {

	public GreedyColoring(Graph<T> graph) {
		super(graph);
	}

	public void greedy() {
		List<T> nodeList = graph.DFS();
		int lastColor = 0;

		for (T info : nodeList) {

			// Create a set with adjacent colors
			Set<Integer> adjColors = new HashSet<Integer>();
			for (T adj : graph.neighbors(info)) {
				adjColors.add(graph.getColor(adj));
			}

			// Now create a list with possible colors
			List<Integer> possibleColors = new ArrayList<Integer>();
			for (Integer i : available)
				if (!adjColors.contains(i))
					possibleColors.add(i);

			if (possibleColors.isEmpty()) {
				available.add(lastColor);
				possibleColors.add(lastColor);
				lastColor++;
			}
			// And put a random color to this node
			int randomColor = possibleColors
					.get((int) (Math.random() * possibleColors.size()));
			graph.setColor(info, randomColor);
		}
	}
}
