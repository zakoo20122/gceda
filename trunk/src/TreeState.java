/**
 * Clase usada para los nodos del arbol de estados
 */

public class TreeState<V> extends State<V> {
	private static int cant = 0;
	// Usado para diferenciar un estado
	// de otro con los mismos valores
	private int num;

	public TreeState(V info, int color) {
		super(info, color);
		this.num = cant;
		cant++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + num;
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeState other = (TreeState) obj;
		if (num != other.num)
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (getInfo() == null)
			return "Start";
		return getInfo().toString() + getColor() + num;
	}
}
