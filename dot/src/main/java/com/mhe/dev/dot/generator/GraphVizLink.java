package com.mhe.dev.dot.generator;

/**
 * Define un enlace entre dos nodos.
 *
 * @author Manuel Hoyo Estévez
 */
public interface GraphVizLink extends GraphVizEntity, Comparable<GraphVizLink>
{
    /**
     * Obtener nodo origen.
     */
    GraphVizNode getOriginNode();

    /**
     * Obtener nodo destino.
     */
    GraphVizNode getTargetNode();
}
