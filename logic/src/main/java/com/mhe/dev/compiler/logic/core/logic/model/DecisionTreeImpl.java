package com.mhe.dev.compiler.logic.core.logic.model;

/**
 * DecisionTreeImpl.
 */
public class DecisionTreeImpl implements DecisionTree
{
    private final Boolean mode;
    private final String literal;
    private final TruthTable truthTable;
    private final DecisionTree zero;
    private final DecisionTree one;

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
            this.mode = this.getLeafValue();
            return;
        }

        if (this.zero.isLeaf())
        {
            this.mode = !this.zero.getLeafValue();
            return;
        }
        if (this.one.isLeaf())
        {
            this.mode = this.one.getLeafValue();
            return;
        }

        this.mode = null;
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
