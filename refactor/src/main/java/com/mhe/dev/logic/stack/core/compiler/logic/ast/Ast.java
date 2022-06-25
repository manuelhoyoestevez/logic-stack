package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.AbstractSyntaxTree;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizDefaultLink;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizLink;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;

/**
 * Abstract Syntax Tree generic node for Regular Expressions Parser.
 *
 * @author Manuel Hoyo Estévez
 */
public abstract class Ast implements AbstractSyntaxTree<LogicSemanticCategory>, GraphVizNode {
    /**
     * Type.
     */
    private final LogicSemanticCategory logicSemanticCategory;
    /**
     * Children.
     */
    private final LinkedList<AbstractSyntaxTree<LogicSemanticCategory>> children;

    public Ast(LogicSemanticCategory logicSemanticCategory) {
        this.logicSemanticCategory = logicSemanticCategory;
        this.children = new LinkedList<>();
    }

    protected static String quote(String str) {
        return "\"" + str + "\"";
    }

    protected static String constJson(boolean value) {
        return "{" + quote("operator") + ":" + quote(value ? "and" : "or") + "}";
    }

    protected static String literalJson(String literal) {
        return "{"
                + quote("operator") + ":" + quote("literal") + ","
                + quote("literal") + ":" + quote(literal) + "}";
    }

    protected static String orderJson(List<String> order) {
        if (order == null) {
            return "";
        }

        StringBuilder json = new StringBuilder(quote("order") + ":[");

        boolean f = true;

        for (String literal : order) {
            if (f) {
                f = false;
            } else {
                json.append(",");
            }
            json.append(quote(literal));
        }

        return json + "],";
    }

    protected static String opJson(String operator, List<String> nodes, List<String> order) {
        StringBuilder json = new StringBuilder("{"
                + quote("operator") + ":" + quote(operator) + ","
                + orderJson(order)
                + quote("children") + ":[");

        boolean f = true;

        for (String node : nodes) {
            if (f) {
                f = false;
            } else {
                json.append(",");
            }

            json.append(node);
        }

        return json + "]}";
    }

    protected static String notJson(String node, List<String> order) {
        return opJson("not", Collections.singletonList(node), order);
    }

    protected static String orJson(String nodeA, String nodeB, List<String> order) {
        return opJson("or", Arrays.asList(nodeA, nodeB), order);
    }

    protected static String andJson(String nodeA, String nodeB, List<String> order) {
        return opJson("and", Arrays.asList(nodeA, nodeB), order);
    }

    public LinkedList<AbstractSyntaxTree<LogicSemanticCategory>> getChildren() {
        return this.children;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> getFirstChild() {
        if (this.getChildren().isEmpty()) {
            return null;
        }
        return this.getChildren().getFirst();
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> getSecondChild() {
        if (this.getChildren().size() <= 1) {
            return null;
        }

        return this.getChildren().get(1);
    }

    @Override
    public LogicSemanticCategory getType() {
        return this.logicSemanticCategory;
    }

    @Override
    public Collection<GraphVizLink> getLinks() {
        Collection<GraphVizLink> ret = new ArrayList<>();

        for (AbstractSyntaxTree<LogicSemanticCategory> e : this.getChildren()) {
            ret.add(new GraphVizDefaultLink(this, e));
        }

        return ret;
    }

    @Override
    public int compareTo(GraphVizNode node) {
        Integer x = this.hashCode();
        Integer y = node.hashCode();
        return x.compareTo(y);
    }

    @Override
    public String toString() {
        return this.toString(0);
    }

    private String toString(int p) {
        StringBuilder r = new StringBuilder();

        for (int i = 0; i < p; i++) {
            r.append(" ");
        }

        r.append(this.getLabel()).append('\n');

        for (AbstractSyntaxTree<LogicSemanticCategory> e : this.getChildren()) {
            r.append(((Ast) e).toString(p + 1));
        }

        return r.toString();
    }
}
