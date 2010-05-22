import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Coloring<T> {
	
	protected Graph<T> graph;
	
	protected void color(T info, List<Integer> available) {
		Set<Integer> disabled = new HashSet<Integer>();
		for (T neighbor : graph.neighbors(info)) {
			if (graph.getColor(neighbor) != -1) {
				disabled.add(graph.getColor(neighbor));
			}
		}
		int newColor = 0;
		if (disabled.size() == available.size()) {
			newColor = available.get(available.size() - 1) + 1;
			available.add(newColor);
			graph.setColor(info, newColor);
		} else {
			int i = 0;
			while (graph.getColor(info) == -1) {
				newColor = available.get(i);
				if (!disabled.contains(newColor)) {
					graph.setColor(info, newColor);
				}
				i++;
			}
		}
	}
}
