package mhe.graphviz;


/** Define un elemento de GrpahViz y sus atributos
 * @author Manuel Hoyo Estévez
 */
public interface GraphVizEntity{
	//public Map<String, String> getAttributes();
	
	/** Figura */
	public String getShape();
	
	/** Etiqueta */
	public String getLabel();
	
	/** Color */
	public String getColor();
}
