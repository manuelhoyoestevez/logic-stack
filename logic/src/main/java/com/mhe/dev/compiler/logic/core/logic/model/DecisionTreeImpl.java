package com.mhe.dev.compiler.logic.core.logic.model;

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
     * @param zero       Zero branch
     * @param one        One branch
     */
    public DecisionTreeImpl(TruthTable truthTable, String literal, DecisionTree zero, DecisionTree one)
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
}
