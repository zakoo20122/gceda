import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TabuSearch<T> extends Coloring<T> {

	private static int MAX_TRIES = 1;
	private static int[] memory;
	private static int j = 4;

	public static void main(String[] args) throws IOException {
		Graph<String> g = new RandomGraph(4, 5);
		TabuSearch<String> ts = new TabuSearch<String>(g);
		long time = System.currentTimeMillis();
		ts.coloring();
		System.out.println(System.currentTimeMillis() - time);
		GraphExporter.exportGraph("ts", g);
	}

	private class Solution {

		Set<State<T>> states;
		/* Indica cual es el nodo que cambio de color */
		int index;

		public Solution(Set<State<T>> states, int index) {
			this.states = states;
			this.index = index;
		}

		public int evaluate() {
			Set<Integer> distinctColors = new HashSet<Integer>();
			for (State<T> state : states)
				distinctColors.add(state.getColor());
			return distinctColors.size();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + index;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Solution other = (Solution) obj;
			if (index != other.index)
				return false;
			return true;
		}

		public Set<Solution> neighbors() {
			System.out.println(quantColor);
			System.out.println(available);
			Set<Solution> set = new HashSet<Solution>();
			int i = 0;
			for (T info : graph.DFS()) {
				if (memory[i] == 0) {
					Set<State<T>> aux = new HashSet<State<T>>();
					if (changeColor(info, aux)) {
						// Agrega los estados de los nodos que no fueron
						// modificados ya que hashea por el info
						aux.addAll(states);
						set.add(new Solution(aux, i));
						restore();
					}
				}
				i++;
			}
			return set;
		}

		/*
		 * Devuelve false si la cantidad de colores que requiere es mayor a la
		 * que ya se estaba utilizando.
		 */
		private boolean changeColor(T info, Set<State<T>> ans) {
			int oldColor = graph.getColor(info);
			discolor(info);
			if (oldColor == 0)
				graph.setColor(info, oldColor + 1);
			else
				graph.setColor(info, oldColor - 1);

			ans.add(new State<T>(info, graph.getColor(info)));

			for (T next : graph.neighborsColor(info)) {
				if (ans.contains(new State<T>(next, -1))) {
					int previousSize = available.size();
					color(info);
					/*
					 * Si tuvo que usar mas colores para colorearlo, remueve el
					 * que acaba de agregar y retorna false
					 */
					if (previousSize < available.size()) {
						available.remove((Object) graph.getColor(info));
						return false;
					} else {
						ans.remove(new State<T>(info, -1));
						ans.add(new State<T>(info, graph.getColor(info)));
						return true;
					}
				} else if (!changeColor(next, ans))
					return false;
			}
			return true;
		}

		private void restore() {
			for (State<T> state : states) {
				graph.setColor(state.getInfo(), state.getColor());
			}
			System.out.println(quantColor);
			System.out.println(available);
			System.out.println("Hola");

			updateLists(states);
			System.out.println(quantColor);
			System.out.println(available);
		}

		// PARA TESTEAR
		public String toString() {
			return states.toString();
		}
	}

	public TabuSearch(Graph<T> graph) {
		this.graph = graph;
		this.available = new ArrayList<Integer>();
		this.quantColor = new ArrayList<Integer>();
	}

	public void coloring() {
		memory = new int[graph.vertexCount()];
		Solution localSolution = initialSolution();
		Solution bestSolution = localSolution;
		int localSolEval = localSolution.evaluate(), bestSolEval = localSolEval;
		// System.out.println("Initial: "+localSolution);
		for (int i = 0; i < MAX_TRIES; i++) {
			for (Solution neighbor : localSolution.neighbors()) {
				int neighborSolEval = neighbor.evaluate();
				if (localSolEval > neighborSolEval) {
					//System.out.println(neighbor);
					localSolution = neighbor;
					localSolEval = neighborSolEval;
					updateLists(localSolution.states);
					memory[localSolution.index] = j;
				}
			}
			for (int k = 0; k < memory.length; k++)
				if (memory[k] != 0)
					memory[k]--;
			if (bestSolEval > localSolEval) {
				System.out.println("Mejoro");
				bestSolution = localSolution;
				bestSolEval = localSolEval;
			}
		}

		for (State<T> state : bestSolution.states) {
			graph.setColor(state.getInfo(), state.getColor());
		}
	}

	private Solution initialSolution() {
		Set<State<T>> states = new HashSet<State<T>>();
		available.add(0);
		for (T info : graph.DFS()) {
			color(info);
			states.add(new State<T>(info, graph.getColor(info)));
		}
		return new Solution(states, -1);
	}

	private void updateLists(Set<State<T>> states) {
		Set<Integer> aux = new HashSet<Integer>();
		for(int i = 0; i < quantColor.size(); i++)
			quantColor.set(i, 0);
		available.clear();
		for (State<T> state : states) {
			aux.add(state.getColor());
			quantColor.set(state.getColor(),
					quantColor.get(state.getColor()) + 1);
		}
		available.addAll(aux);	
	}
}
