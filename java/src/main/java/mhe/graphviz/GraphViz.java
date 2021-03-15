package mhe.graphviz;

import java.util.*;

/** Clase estática para la escritura de estructuras GraphViz
 * @author Manuel Hoyo Estévez
 */
public class GraphViz{
	
	/** Generar fichero con Diagrama GraphViz en forma de árbol
	 * @param node Nodo Raíz del árbol
	 * @param name Nombre del fichero
	 * @return true si no hay problemas
	 */
	public static boolean drawTreeFile(GraphVizNode node,String name){
		return stringToFile(drawTree(node,name),name);
	}
	
	/** Generar fichero con Diagrama GraphViz en forma de autómata
	 * @param node Primer Nodo del Autómata
	 * @param name Nombre del fichero
	 * @return true si no hay problemas
	 */
	public static boolean drawAutomatonFile(GraphVizNode node,String name){
		return stringToFile(drawAutomaton(node,name),name);
	}
	
	public static String drawTree(GraphVizNode node,String name){
		String r = "digraph " + name + " {\n";
		return r + drawGraph(node,new TreeSet<GraphVizNode>()) + "}\n";
	}
	
	public static String drawAutomaton(GraphVizNode node,String name){
		String r = "digraph " + name + " {\n";
		r += "\trankdir=LR;\n";
		return r + drawGraph(node,new TreeSet<GraphVizNode>()) + "}\n";
	}
	
	private static String entityArgs(GraphVizEntity entity){
		
		String r = " [";
		/*
		for(Entry<String, String> entry : entity.getAttributes().entrySet()) {
			r += entry.getKey() + "=" + entry.getValue()+ " ";
		}
		*/
		r += entity.getLabel() == null ? "" : " label=" + entity.getLabel() + " ";
		r += entity.getColor() == null ? "" : " color=" + entity.getColor() + " ";
		r += entity.getShape() == null ? "" : " shape=" + entity.getShape() + " ";
		return r + "];\n";
	}
	
	private static String drawGraph(GraphVizNode node,Collection<GraphVizNode> visit){
		String r = "";
		if(visit.contains(node) == false){
			
			r+= "\tnode" + node.getSerial() + entityArgs(node);
			visit.add(node);

			for(GraphVizLink aux: node.getLinks()){
				r += drawGraph(aux.getDestinNode(),visit)
				  +  "\t node" 
				  +  aux.getOriginNode().getSerial() + " -> node" 
				  +  aux.getDestinNode().getSerial() + entityArgs(aux);
			}
		}
		return r;
	}
	
	private static boolean stringToFile (String str,String name){
		boolean r = true;
		try{
			java.io.FileWriter fichero = new java.io.FileWriter(name);
	        fichero.write(str);
	        fichero.close();
		}
        catch (Exception e) {
            r = false;
        }
        return r;
	}
}
