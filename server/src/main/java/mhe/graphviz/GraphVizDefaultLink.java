package mhe.graphviz;

/** Clase que implementa el enlace entre nodos GraphViz
 * @author Manuel Hoyo Estévez
 */
public class GraphVizDefaultLink implements GraphVizLink {
	protected GraphVizNode origin;
	protected GraphVizNode target;

	protected String shape;
	protected String label;
	protected String color;

	/** Constructor simple
	 * @param o Nodo Origen
	 * @param d Nodo Destino
	 */
	public GraphVizDefaultLink(GraphVizNode o, GraphVizNode d){
		this(o,d,null,null,null);
	}
	
	/** Constructor
	 * @param o Nodo Origen
	 * @param d Nodo Destino
	 * @param s Figura
	 * @param l Etiqueta
	 * @param c Color
	 */
	public GraphVizDefaultLink(
		GraphVizNode o, GraphVizNode d, 
		String s, String l, String c
	){
		this.origin = o;
		this.target = d;
		this.shape  = s;
		this.label  = l;
		this.color  = c;
	}
	
	@Override
	public String getShape() { 
		return this.shape; 
		}

	@Override
	public String getLabel() { 
		return this.label; 
	}

	@Override
	public String getColor() { 
		return this.color; 
	}
	
	@Override
	public GraphVizNode getOriginNode() { 
		return this.origin; 
	}

	@Override
	public GraphVizNode getTargetNode() {
		return this.target;
	}

	@Override
	public int compareTo(GraphVizLink link) {
		int a = this.origin.compareTo(link.getOriginNode());

		if (a != 0) {
			return a;
		}

		int b = this.target.compareTo(link.getTargetNode());

		if (b != 0) {
			return b;
		}

		if (this.getLabel() != null) {
			return this.getLabel().compareTo(link.getLabel());
		}

		if (link.getLabel() != null) {
			return 1;
		}

		return 0;
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj == null) {
			return false;
		}

		if (obj instanceof GraphVizLink) {
			return this.compareTo((GraphVizLink) obj) == 0;
		}

		return false;
	}
}
