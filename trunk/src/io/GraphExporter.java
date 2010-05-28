package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import algorithm.Graph;
import algorithm.TreeState;

public class GraphExporter {

	public static <T> void exportGraph(String fileName, Graph<T> graph)
			throws IOException {
		GraphExporter.exportGraph(fileName, graph, false);
	}

	public static <T> void exportGraphTree(String filename, Graph<T> graph)
			throws IOException {
		GraphExporter.exportGraph(filename, graph, true);
	}

	@SuppressWarnings("unchecked")
	private static <T> void exportGraph(String fileName, Graph<T> graph,
			boolean tree) throws IOException {
		BufferedWriter output = null;
		try {
			File file = new File(fileName + ".dot");
			output = new BufferedWriter(new FileWriter(file));

			output.write("graph G { ");
			output.newLine();
			output.write("graph [splines = true] " + "node [height=0.4 shape="
					+ (tree ? "rectangle" : "circle") + " style=filled]");
			output.newLine();

			List<T> nodes = graph.DFS();
			HashMap<T, List<T>> outGraph = new HashMap<T, List<T>>();

			// Remover redundancia de aristas
			for (T node : nodes) {
				List<T> neighbors = graph.neighbors(node);
				for (T neighbor : neighbors) {
					if (outGraph.containsKey(neighbor)) {
						List<T> other = outGraph.get(neighbor);
						other.remove(node);
					}
				}
				outGraph.put(node, neighbors);
			}

			// Almacenar datos para exportar
			Set<Entry<T, List<T>>> entries = outGraph.entrySet();

			// Primero, declarar nodos y colores
			for (Entry<T, List<T>> entry : entries) {
				String line = "";
				if (tree) {
					TreeState<T> state = (TreeState<T>) entry.getKey();
					line = state.toString() + " [ label= \"node = "
							+ state.getInfo() + " color = " + state.getColor()
							+ "\"] ;";
				} else
					line = entry.getKey() + " [ label= \"node = "
							+ entry.getKey().toString() + " color = "
							+ graph.getColor(entry.getKey()) + "\"] ;";
				output.write(line);
				output.newLine();
			}

			for (Entry<T, List<T>> entry : entries) {
				for (T neighbor : entry.getValue()) {
					String line = "";
					line += entry.getKey() + " -- " + neighbor
							+ ";";
					output.write(line);
					output.newLine();
				}
			}

			output.write("}");

		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	public static <T> void exportColors(String fileName, Graph<T> graph)
			throws IOException {
		BufferedWriter output = null;
		try {
			File file = new File(fileName + ".color");
			output = new BufferedWriter(new FileWriter(file));

			List<T> nodes = graph.DFS();
			for (T node : nodes) {
				int color = graph.getColor(node);
				output.write(node + "=" + color);
				output.newLine();
			}
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}
}
