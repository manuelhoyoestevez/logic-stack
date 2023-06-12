package com.mhe.dev.logic.stack.core.graphviz;

import java.util.Collection;

/**
 * Define un nodo de GraphViz.
 *
 * @author Manuel Hoyo Estévez
 */
public interface GraphVizNode extends GraphVizEntity, Comparable<GraphVizNode>
{
    /**
     * Colección de enlaces a nodos adyacentes.
     *
     * @return Collection of GraphVizLinks
     */
    Collection<GraphVizLink> getLinks();
}
