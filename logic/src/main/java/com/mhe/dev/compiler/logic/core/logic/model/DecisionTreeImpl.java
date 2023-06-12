package com.mhe.dev.compiler.logic.core.logic.model;

import com.mhe.dev.logic.stack.core.graphviz.GraphVizDefaultLink;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizLink;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;
import java.util.ArrayList;
import java.util.Collection;

/**
 * DecisionTreeImpl.
 */
public class DecisionTreeImpl implements DecisionTree
{
    private final DecisionTreeType type;
    private final Boolean mode;
    private final String literal;
    private final TruthTable truthTable;
    private final DecisionTree zero;
    private final DecisionTree one;
    private String expression;

    /**
     * Constructor.
     *
     * @param truthTable Truth table
     * @param literal    Current literal
     * @param zero       Zero
     * @param one        One
     */
    public DecisionTreeImpl(
        TruthTable truthTable,
        String literal,
        DecisionTree zero,
        DecisionTree one
    )
    {
        super();
        this.literal = literal;
        this.truthTable = truthTable;
        this.zero = zero;
        this.one = one;

        if (this.isLeaf())
        {
            this.type = DecisionTreeType.LEAF;
            this.mode = this.getLeafValue();
            return;
        }

        if (this.zero.isLeaf())
        {
            this.mode = !this.zero.getLeafValue();
            if (this.one.isLeaf())
            {
                this.type = DecisionTreeType.LITERAL;
            } else if (this.zero.getLeafValue())
            {
                this.type = DecisionTreeType.LATERAL_1;
            } else
            {
                this.type = DecisionTreeType.LATERAL_0;
            }
            return;
        }
        if (this.one.isLeaf())
        {
            this.mode = this.one.getLeafValue();
            if (this.zero.isLeaf())
            {
                this.type = DecisionTreeType.LITERAL;
            } else if (this.one.getLeafValue())
            {
                this.type = DecisionTreeType.LATERAL_1;
            } else
            {
                this.type = DecisionTreeType.LATERAL_0;
            }
            return;
        }

        this.mode = null;
        this.type = DecisionTreeType.COMPLETE;
    }

    @Override
    public DecisionTreeType getType()
    {
        return this.type;
    }

    @Override
    public TruthTable getTruthTable()
    {
        return truthTable;
    }

    @Override
    public Boolean getMode()
    {
        return this.mode;
    }

    @Override
    public String getLiteral()
    {
        return this.literal;
    }

    @Override
    public boolean isLeaf()
    {
        return truthTable.getEntropy() == 0.0;
    }

    @Override
    public boolean getLeafValue()
    {
        return truthTable.getLeafValue();
    }

    @Override
    public DecisionTree getSubDecisionTree(boolean value)
    {
        return value ? this.one : this.zero;
    }

    @Override
    public String getExpression()
    {
        return expression;
    }

    @Override
    public DecisionTree setExpression(String expression)
    {
        this.expression = expression;
        return this;
    }

    @Override
    public String getColor()
    {
        if (this.getType() == DecisionTreeType.COMPLETE)
        {
            return "\"black\"";
        }

        return "\"" + (getMode() ? "blue" : "red") + "\"";
    }

    @Override
    public String getShape()
    {
        switch (this.getType())
        {
            case COMPLETE:
                return "\"octagon\"";
            case LATERAL_0:
                return "\"invtrapezium\"";
            case LATERAL_1:
                return "\"trapezium\"";
            case LITERAL:
                return "\"ellipse\"";
            default:
                return "\"rectangle\"";
        }
    }

    @Override
    public String getLabel()
    {
        switch (this.getType())
        {
            case LEAF:
                return "\"" + Math.round(truthTable.getAverage()) + "\"";
            case COMPLETE:
            case LATERAL_0:
            case LATERAL_1:
                return "\"" + (this.getLiteral() + " (" + (Math.round(truthTable.getEntropy() * 100.0) / 100.0) + "): ")
                    + truthTable.getAverage() + "\"";
            case LITERAL:
                return "\"" + this.getLiteral() + "\"";
            default:
                return "\"-\"";
        }
    }

    @Override
    public int compareTo(GraphVizNode arg0)
    {
        return this.hashCode() - arg0.hashCode();
    }

    @Override
    public Collection<GraphVizLink> getLinks()
    {
        ArrayList<GraphVizLink> ret = new ArrayList<>();

        if (this.getType() != DecisionTreeType.LITERAL)
        {
            if (this.zero != null)
            {
                ret.add(new GraphVizDefaultLink(this, this.zero, null, "\"" + this.getLiteral() + " = 0\"", null));
            }
            if (this.one != null)
            {
                ret.add(new GraphVizDefaultLink(this, this.one, null, "\"" + this.getLiteral() + " = 1\"", null));
            }
        }

        return ret;
    }
}
