package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract Syntax Tree generic node for Regular Expressions Parser.
 *
 * @author Manuel Hoyo Estévez
 */
public abstract class Ast implements AbstractSyntaxTree<LogicSemanticCategory>
{
    /**
     * Type.
     */
    private final LogicSemanticCategory logicSemanticCategory;
    /**
     * Children.
     */
    private final LinkedList<AbstractSyntaxTree<LogicSemanticCategory>> children;

    public Ast(LogicSemanticCategory logicSemanticCategory)
    {
        this.logicSemanticCategory = logicSemanticCategory;
        this.children = new LinkedList<>();
    }

    protected static String quote(String str)
    {
        return "\"" + str + "\"";
    }

    protected static String constJson(boolean value)
    {
        return "{" + quote("operator") + ":" + quote(value ? "and" : "or") + "}";
    }

    protected static String literalJson(String literal)
    {
        return "{"
            + quote("operator") + ":" + quote("literal") + ","
            + quote("literal") + ":" + quote(literal) + "}";
    }

    protected static String orderJson(List<String> order)
    {
        if (order == null)
        {
            return "";
        }

        StringBuilder json = new StringBuilder(quote("order") + ":[");

        boolean f = true;

        for (String literal : order)
        {
            if (f)
            {
                f = false;
            } else
            {
                json.append(",");
            }
            json.append(quote(literal));
        }

        return json + "],";
    }

    protected static String opJson(String operator, List<String> nodes, List<String> order)
    {
        StringBuilder json = new StringBuilder("{"
            + quote("operator") + ":" + quote(operator) + ","
            + orderJson(order)
            + quote("children") + ":[");

        boolean f = true;

        for (String node : nodes)
        {
            if (f)
            {
                f = false;
            } else
            {
                json.append(",");
            }

            json.append(node);
        }

        return json + "]}";
    }

    protected static String notJson(String node, List<String> order)
    {
        return opJson("not", Collections.singletonList(node), order);
    }

    protected static String orJson(String nodeA, String nodeB, List<String> order)
    {
        return opJson("or", Arrays.asList(nodeA, nodeB), order);
    }

    protected static String andJson(String nodeA, String nodeB, List<String> order)
    {
        return opJson("and", Arrays.asList(nodeA, nodeB), order);
    }

    public LinkedList<AbstractSyntaxTree<LogicSemanticCategory>> getChildren()
    {
        return this.children;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> getFirstChild()
    {
        if (this.getChildren().isEmpty())
        {
            return null;
        }
        return this.getChildren().getFirst();
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> getSecondChild()
    {
        if (this.getChildren().size() <= 1)
        {
            return null;
        }

        return this.getChildren().get(1);
    }

    @Override
    public LogicSemanticCategory getType()
    {
        return this.logicSemanticCategory;
    }
}
