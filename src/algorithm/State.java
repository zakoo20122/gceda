package algorithm;
public class State<V> {
	private V info;
	private int color;

	public State(V info, int color) {
		this.info = info;
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public V getInfo() {
		return info;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((info == null) ? 0 : info.hashCode());
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
		State other = (State) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}

	//PARA TESTEAR
	@Override
	public String toString() {
		return "[info=" + info + ", color=" + color + "]";
	}
}
