package mhe.graphviz;

import java.util.Collection;

/** Define un nodo de GraphViz
 * @author Manuel Hoyo Estévez
 */
public interface GraphVizNode extends GraphVizEntity, Comparable<GraphVizNode>{
	/** Numero de serie identificador del nodo */
	int getSerial();
	
	/** Colección de enlaces a nodos adyacentes */
	Collection<GraphVizLink> getLinks();
}
