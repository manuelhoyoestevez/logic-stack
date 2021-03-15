package mhe.graphviz;

/** Clase que implmenta el enlace entre nodos GraphViz
 * @author Manuel Hoyo Estévez
 */
public class GraphVizDefaultLink implements GraphVizLink {
	protected GraphVizNode origin;
	protected GraphVizNode destin;

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
		this.destin = d;
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
	public GraphVizNode getDestinNode() { 
		return this.destin; 
	}
	
	@Override
	public void setOriginNode(GraphVizNode node) { 
		this.origin = node; 
	}

	@Override
	public void setDestinNode(GraphVizNode node) { 
		this.destin = node; 
	}

	@Override
	public int compareTo(GraphVizLink link) {
		if(link == null)
			return 0;
		else{
			int a = this.origin.compareTo(link.getOriginNode());
			if(a != 0)
				return a;
			else{
				int b = this.destin.compareTo(link.getDestinNode());
				if(b != 0)
					return b;
				else{
					if(this.getLabel() == null){
						if(link.getLabel() == null)
							return 0;
						else
							return 1;
					}
					else
						return this.getLabel().compareTo(link.getLabel());
				}
			}
		}
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null)
			return false;
		else if(obj instanceof GraphVizLink)
			return this.compareTo((GraphVizLink)obj) == 0;
		else
			return false;
	}
}
