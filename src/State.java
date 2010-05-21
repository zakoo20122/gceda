public class State<V> {
	private V info;
	private int color;
	private static int cant = 0;
	//Usado para diferenciar un estado 
	//de otro con los mismos valores
	private int num;
	
	public State(V info, int color) {
		this.info = info;
		this.color = color;
		this.num = cant;
		cant++;		
	}
	
	public int getColor(){
		return color;
	}
	
	public V getInfo(){
		return info;
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
		State other = (State) obj;
		if (num != other.num)
			return false;
		return true;
	}

	@Override
	public String toString() {
		if(info == null )
			return "Start";
		return info.toString() + color + num;
	}
}
