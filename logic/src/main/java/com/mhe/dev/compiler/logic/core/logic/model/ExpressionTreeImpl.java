package com.mhe.dev.compiler.logic.core.logic.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ExpressionTreeImpl.
 */
public class ExpressionTreeImpl implements ExpressionTree
{
    private final List<String> literals = new ArrayList<>();
    private final boolean mode;
    private final int size;
    private final String literal;
    private final ExpressionTreeType type;
    private final Collection<ExpressionTree> children;
    private final List<String> weights;

    /**
     * Create constant ExpressionTree.
     *
     * @param mode false for 0, true for 1
     * @return ExpressionTree
     */
    public static ExpressionTreeImpl createConstantExpressionTree(boolean mode)
    {
        return new ExpressionTreeImpl(
                ExpressionTreeType.OPERATOR,
                mode,
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    /**
     * Create literal ExpressionTree.
     *
     * @param mode negates if false
     * @param literal literal
     * @return ExpressionTree
     */
    public static ExpressionTreeImpl createLiteralExpressionTree(boolean mode, String literal)
    {
        return new ExpressionTreeImpl(
                ExpressionTreeType.LITERAL,
                mode,
                literal,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    /**
     * Create negated ExpressionTree.
     *
     * @param expressionTree ExpressionTree to negate
     * @return ExpressionTree
     */
    public static ExpressionTreeImpl createNotExpressionTree(ExpressionTree expressionTree)
    {
        return new ExpressionTreeImpl(
                ExpressionTreeType.NOT,
                expressionTree.getMode(),
                expressionTree.getLiteral(),
                Collections.singletonList(expressionTree),
                expressionTree.getWeights()
        );
    }

    /**
     * Create operator ExpressionTree.
     *
     * @param mode false for OR, true for AND
     * @param children Children
     * @param weights Weights
     * @return ExpressionTree
     */
    public static ExpressionTreeImpl createOperatorExpressionTree(
            boolean mode,
            Collection<ExpressionTree> children,
            List<String> weights
    )
    {
        return new ExpressionTreeImpl(
                ExpressionTreeType.OPERATOR,
                mode,
                null,
                children,
                weights
        );
    }

    /**
     * Constructor.
     *
     * @param type     Type
     * @param mode     Mode
     * @param literal  Literal
     * @param children Children
     * @param weights  Weights
     */
    private ExpressionTreeImpl(
        ExpressionTreeType type,
        boolean mode,
        String literal,
        Collection<ExpressionTree> children,
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

        int size = 1;

        // Añadimos los literales de los hijos
        for (ExpressionTree child : getChildren())
        {
            size += child.getSize();
            for (String lit : child.getLiterals())
            {
                if (!literals.contains(lit))
                {
                    literals.add(lit);
                }
            }
        }

        this.size = size;

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
    public int getSize()
    {
        return size;
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
    public Collection<ExpressionTree> getChildren()
    {
        return children;
    }

    @Override
    public String toString()
    {
        return getExpression();
    }

    @Override
    public ExpressionTree reduce()
    {
        if (getType() == ExpressionTreeType.LITERAL)
        {
            return this;
        }

        if (getType() == ExpressionTreeType.NOT)
        {
            return getFirst().generateNot().reduce();
        }

        List<ExpressionTree> newChildren = new ArrayList<>();

        for (ExpressionTree child : getChildren())
        {
            if (child.getType() != ExpressionTreeType.OPERATOR || !child.getChildren().isEmpty())
            {
                newChildren.add(child.reduce());
                continue;
            }

            if (getMode() != child.getMode())
            {
                return child;
            }
        }

        List<ExpressionTree> finalChildren = new ArrayList<>();

        for (int i = 0; i < newChildren.size(); i++)
        {
            boolean add = true;
            ExpressionTree childI = newChildren.get(i);

            for (int j = i + 1; j < newChildren.size(); j++)
            {
                ExpressionTree childJ = newChildren.get(j);

                if (childI.equivalent(childJ))
                {
                    add = false;
                    continue;
                }

                if (childI.complementary(childJ))
                {
                    return ExpressionTreeImpl.createConstantExpressionTree(!getMode());
                }
            }

            if (add)
            {
                finalChildren.add(childI);
            }
        }

        if (finalChildren.size() == 1)
        {
            return finalChildren.get(0);
        }

        return ExpressionTreeImpl.createOperatorExpressionTree(getMode(), finalChildren, getWeights());
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
                + quotify("children") + ":[" + getFirst().toJsonString() + "]}";
        }
        if (getType() == ExpressionTreeType.OPERATOR)
        {
            String jsonString = "{"
                + quotify("operator") + ":" + quotify(getMode() ? "and" : "or");

            StringBuilder jsonChildren = new StringBuilder();

            if (!getChildren().isEmpty())
            {
                boolean f = true;
                jsonChildren.append(",").append(quotify("children")).append(":[");

                for (ExpressionTree child : getChildren())
                {
                    if (f)
                    {
                        f = false;
                    } else
                    {
                        jsonChildren.append(",");
                    }

                    jsonChildren.append(child.toJsonString());
                }

                jsonChildren.append("]");
            }

            return jsonString + jsonChildren + "}";
        }

        return null;
    }

    @Override
    public ExpressionTree generateNot()
    {
        if (getType() == ExpressionTreeType.NOT)
        {
            return getFirst();
        }

        return new ExpressionTreeImpl(
                getType(),
                !getMode(),
                getLiteral(),
                getChildren().stream().map(ExpressionTree::generateNot).collect(Collectors.toList()),
                getWeights()
        );
    }

    @Override
    public ExpressionTree replaceBy(Map<String, Boolean> values)
    {
        if (Collections.disjoint(getLiterals(), values.keySet()))
        {
            return this;
        }

        if (getType() == ExpressionTreeType.LITERAL)
        {
            if (!values.containsKey(getLiteral()))
            {
                return this;
            }

            return ExpressionTreeImpl.createConstantExpressionTree(values.get(getLiteral()) == getMode());
        }

        return new ExpressionTreeImpl(
                getType(),
                getMode(),
                getLiteral(),
                getChildren().stream().map(c -> c.replaceBy(values)).collect(Collectors.toList()),
                getWeights()
        );
    }

    private static String quotify(String str)
    {
        return "\"" + str + "\"";
    }
}
