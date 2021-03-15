package mhe.graphviz;

/** Define un enlace entre dos nodos
 * @author Manuel Hoyo Est�vez
 */
public interface GraphVizLink extends GraphVizEntity, Comparable<GraphVizLink>{
	/** Obtener nodo origen */
	public GraphVizNode getOriginNode();
	
	/** Obtener nodo destino */
	public GraphVizNode getDestinNode();
	
	/** Establecer nodo origen */
	public void setOriginNode(GraphVizNode node);
	
	/** Establecer nodo destino */
	public void setDestinNode(GraphVizNode node);
}
