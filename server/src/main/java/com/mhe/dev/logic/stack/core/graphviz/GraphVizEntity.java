package com.mhe.dev.logic.stack.core.graphviz;

/**
 * Define un elemento de GraphViz y sus atributos.
 *
 * @author Manuel Hoyo Estévez
 */
public interface GraphVizEntity {
    /**
     * Figura.
     */
    String getShape();

    /**
     * Etiqueta.
     */
    String getLabel();

    /**
     * Color.
     */
    String getColor();
}
