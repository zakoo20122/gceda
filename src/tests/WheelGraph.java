package tests;

import java.util.List;

public class WheelGraph<T> extends Cn<T> {

	public WheelGraph(List<T> values, T center) {
		super(values);
		this.addVertex(center);

		for (T t : values)
			this.addEdge(t, center);
	}

}
