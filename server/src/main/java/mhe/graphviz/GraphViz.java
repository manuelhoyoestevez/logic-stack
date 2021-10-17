package mhe.graphviz;

import java.util.*;

/** Clase estática para la escritura de estructuras GraphViz
 * @author Manuel Hoyo Estévez
 */
public class GraphViz{
	public static String drawTree(GraphVizNode node,String name){
		String r = "digraph " + name + " {\n";
		return r + drawGraph(node,new TreeSet<>()) + "}\n";
	}
	
	private static String entityArgs(GraphVizEntity entity){
		String r = " [";
		r += entity.getLabel() == null ? "" : " label=\"" + entity.getLabel() + "\" ";
		r += entity.getColor() == null ? "" : " color=\"" + entity.getColor() + "\" ";
		r += entity.getShape() == null ? "" : " shape=\"" + entity.getShape() + "\" ";
		return r + "];\n";
	}
	
	private static String drawGraph(GraphVizNode node,Collection<GraphVizNode> visit){
		StringBuilder r = new StringBuilder();
		if (!visit.contains(node)) {
			
			r.append("\tnode").append(node.hashCode()).append(entityArgs(node));
			visit.add(node);

			for(GraphVizLink aux: node.getLinks()){
				r.append(drawGraph(aux.getTargetNode(), visit)).append("\t node")
						.append(aux.getOriginNode().hashCode()).append(" -> node")
						.append(aux.getTargetNode().hashCode()).append(entityArgs(aux));
			}
		}
		return r.toString();
	}
}
