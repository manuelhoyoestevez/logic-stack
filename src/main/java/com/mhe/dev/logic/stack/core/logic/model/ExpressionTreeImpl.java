package com.mhe.dev.logic.stack.core.logic.model;

import com.mhe.dev.logic.stack.core.graphviz.GraphVizDefaultLink;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizLink;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * ExpressionTreeImpl.
 */
public class ExpressionTreeImpl implements ExpressionTree
{
    private final List<String> literals = new ArrayList<>();
    private final boolean mode;
    private final String literal;
    private final ExpressionTreeType type;
    private final SortedSet<ExpressionTree> children;
    private final List<String> weights;

    /**
     * Constructor.
     *
     * @param type     Type
     * @param mode     Mode
     * @param literal  Literal
     * @param children Children
     * @param weights  Weights
     */
    public ExpressionTreeImpl(
        ExpressionTreeType type,
        boolean mode,
        String literal,
        SortedSet<ExpressionTree> children,
        List<String> weights
    )
    {
        this.type = type;
        this.mode = mode;
        this.literal = literal;
        this.children = children;
        this.weights = weights;

        if (literal != null)
        {
            literals.add(literal);
        }

        for (ExpressionTree child : getChildren())
        {
            for (String lit : child.getLiterals())
            {
                if (!literals.contains(lit))
                {
                    literals.add(lit);
                }
            }
        }

        literals.sort((o1, o2) ->
        {
            int w1 = weights.indexOf(o1);
            int w2 = weights.indexOf(o2);

            if (w1 < 0)
            {
                w1 = weights.size();
            }
            if (w2 < 0)
            {
                w2 = weights.size();
            }

            return w1 - w2;
        });


    }

    public static String quotify(String str)
    {
        return "\"" + str + "\"";
    }

    protected static ExpressionTree addToChildren(Boolean mode, Set<ExpressionTree> children, ExpressionTree newChild)
    {
        for (ExpressionTree child : children)
        {

            if (child.equivalent(newChild))
            {
                return null;
            }
            if (child.complementary(newChild))
            {
                return new ExpressionTreeImpl(
                    ExpressionTreeType.OPERATOR,
                    !mode,
                    null,
                    new TreeSet<>(),
                    new ArrayList<>()
                );
            }
        }

        children.add(newChild);

        return null;
    }

    @Override
    public List<String> getWeights()
    {
        return weights;
    }

    @Override
    public List<String> getLiterals()
    {
        return literals;
    }

    @Override
    public boolean getMode()
    {
        return mode;
    }

    @Override
    public String getLiteral()
    {
        return literal;
    }

    @Override
    public ExpressionTreeType getType()
    {
        return type;
    }

    @Override
    public SortedSet<ExpressionTree> getChildren()
    {
        return children;
    }

    @Override
    public boolean isFinal()
    {
        return getChildren().isEmpty();
    }

    @Override
    public ExpressionTree reduce()
    {
        Map<String, Boolean> map = new HashMap<>();
        return reduceBy(map);
    }

    @Override
    public int compareTo(GraphVizNode gnode)
    {
        ExpressionTree node = (ExpressionTree) gnode;
        int ret = getExpression().compareTo(node.getExpression());

        if (ret == 0)
        {
            ret = hashCode() - gnode.hashCode();
        }

        return ret;
    }

    @Override
    public Collection<GraphVizLink> getLinks()
    {
        Collection<GraphVizLink> ret = new ArrayList<>();

        for (ExpressionTree child : getChildren())
        {
            ret.add(new GraphVizDefaultLink(this, child));
        }

        return ret;
    }

    @Override
    public boolean equivalent(ExpressionTree expressionTree)
    {
        return reduce().getExpression().compareTo(expressionTree.reduce().getExpression()) == 0;
    }

    @Override
    public boolean complementary(ExpressionTree expressionTree)
    {
        return generateNot().reduce().equivalent(expressionTree.reduce());
    }

    @Override
    public String getShape()
    {
        if (type == ExpressionTreeType.OPERATOR)
        {
            return quotify(getChildren().isEmpty() ? "rectangle" : "ellipse");
        }
        if (type == ExpressionTreeType.LITERAL)
        {
            return quotify("rectangle");
        }
        if (type == ExpressionTreeType.NOT)
        {
            return quotify("ellipse");
        }
        return null;
    }

    @Override
    public String getLabel()
    {
        if (type == ExpressionTreeType.OPERATOR)
        {
            if (getChildren().isEmpty())
            {
                return quotify(getMode() ? "1" : "0" + " " + getLiterals().toString());
            }
            return quotify(getMode() ? "&" : "|" + " " + getLiterals().toString());
        }
        if (type == ExpressionTreeType.LITERAL)
        {
            if (getMode())
            {
                return quotify(getLiteral());
            }
            return quotify("!" + getLiteral());
        }
        if (type == ExpressionTreeType.NOT)
        {
            return quotify("!" + " " + getLiterals().toString());
        }
        return null;
    }

    @Override
    public String getColor()
    {
        if (type == ExpressionTreeType.NOT)
        {
            return quotify("orange");
        }
        if (type == ExpressionTreeType.LITERAL)
        {
            return quotify(getMode() ? "blue" : "red");
        }
        if (type == ExpressionTreeType.OPERATOR)
        {
            return quotify(getChildren().isEmpty() ? getMode() ? "blue" : "red" : getMode() ? "green" : "purple");
        }
        throw new IllegalStateException("Unexpected value: " + type);
    }

    @Override
    public ExpressionTree generateNot()
    {
        if (getType() == ExpressionTreeType.NOT)
        {
            return getChildren().first();
        }
        if (getType() == ExpressionTreeType.LITERAL)
        {
            return new ExpressionTreeImpl(
                ExpressionTreeType.LITERAL,
                !getMode(),
                getLiteral(),
                new TreeSet<>(),
                getWeights()
            );
        }
        if (getType() == ExpressionTreeType.OPERATOR)
        {
            SortedSet<ExpressionTree> newChildren = new TreeSet<>();

            for (ExpressionTree child : getChildren())
            {
                newChildren.add(child.generateNot());
            }

            return new ExpressionTreeImpl(
                ExpressionTreeType.OPERATOR,
                !getMode(),
                null,
                newChildren,
                getWeights()
            );
        }
        return this;
    }

    @Override
    public ExpressionTree reduceBy(Map<String, Boolean> values)
    {
        ExpressionTree child;
        if (getType() == ExpressionTreeType.LITERAL)
        {
            Boolean value = values.get(getLiteral());
            return value == null ? this : new ExpressionTreeImpl(
                ExpressionTreeType.OPERATOR,
                value == getMode(),
                null,
                new TreeSet<>(),
                getWeights()
            );
        }
        if (getType() == ExpressionTreeType.NOT)
        {
            child = getChildren().first();
            return child.generateNot().reduceBy(values);
        }
        if (getType() == ExpressionTreeType.OPERATOR)
        {
            ExpressionTree finalChild;
            SortedSet<ExpressionTree> newChildren = new TreeSet<>();
            for (ExpressionTree c : getChildren())
            {
                child = c.reduceBy(values);

                ExpressionTreeType childType = child.getType(); // Error de programacion
                if (childType == ExpressionTreeType.NOT || childType == ExpressionTreeType.LITERAL)
                {
                    finalChild = addToChildren(getMode(), newChildren, child);

                    if (finalChild != null)
                    {
                        return finalChild;
                    }
                } else if (childType == ExpressionTreeType.OPERATOR)
                {
                    if (getMode() == child.getMode())
                    {
                        for (ExpressionTree d : child.getChildren())
                        {
                            finalChild = addToChildren(getMode(), newChildren, d);

                            if (finalChild != null)
                            {
                                return finalChild;
                            }
                        }
                    } else if (child.isFinal())
                    {
                        return child;
                    } else
                    {
                        finalChild = addToChildren(getMode(), newChildren, child);

                        if (finalChild != null)
                        {
                            return finalChild;
                        }
                    }
                }
            }

            return newChildren.size() == 1
                ? newChildren.first()
                : new ExpressionTreeImpl(
                ExpressionTreeType.OPERATOR,
                getMode(),
                null,
                newChildren,
                getWeights()
            );
        }

        return null;
    }

    @Override
    public String getExpression()
    {
        StringBuilder ret = new StringBuilder();
        if (getType() == ExpressionTreeType.LITERAL)
        {
            ret.append(getMode() ? "" : "!").append(getLiteral());
        }
        if (getType() == ExpressionTreeType.NOT)
        {
            ret.append("!").append(getChildren().first().getExpression());
        }
        if (getType() == ExpressionTreeType.OPERATOR)
        {
            switch (getChildren().size())
            {
                case 0:
                    ret.append(getMode() ? "1" : "0");
                    break;
                case 1:
                    ret.append(getChildren().first().getExpression());
                    break;
                default:
                    boolean f = true;

                    ret.append(getMode() ? "[" : "{");

                    for (ExpressionTree child : getChildren())
                    {
                        if (f)
                        {
                            f = false;
                        } else
                        {
                            ret.append(",");
                        }
                        ret.append(child.getExpression());
                    }
                    ret.append(getMode() ? "]" : "}");
            }
        }
        return ret.toString();
    }

    @Override
    public String toJsonString()
    {
        if (getType() == ExpressionTreeType.LITERAL)
        {
            return "{"
                + quotify("operator") + ":" + quotify("literal") + ","
                + quotify("literal") + ":" + quotify(literal) + "}";
        }
        if (getType() == ExpressionTreeType.NOT)
        {
            return "{"
                + quotify("operator") + ":" + quotify("not") + ","
                + quotify("children") + ":[" + getChildren().first().toJsonString() + "]}";
        }
        if (getType() == ExpressionTreeType.OPERATOR)
        {
            String jsonString = "{"
                + quotify("operator") + ":" + quotify(getMode() ? "and" : "or");

            String jsonChildren = "";

            if (!getChildren().isEmpty())
            {
                boolean f = true;
                jsonChildren += "," + quotify("children") + ":[";

                for (ExpressionTree child : getChildren())
                {
                    if (f)
                    {
                        f = false;
                    } else
                    {
                        jsonChildren += ",";
                    }

                    jsonChildren += child.toJsonString();
                }

                jsonChildren += "]";
            }

            return jsonString + jsonChildren + "}";
        }

        return null;
    }

    @Override
    public String toString()
    {
        return getExpression();
    }

}
