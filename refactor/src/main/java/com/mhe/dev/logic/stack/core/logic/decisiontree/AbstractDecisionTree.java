package com.mhe.dev.logic.stack.core.logic.decisiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizDefaultLink;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizLink;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;
import com.mhe.dev.logic.stack.core.logic.DecisionTree;
import com.mhe.dev.logic.stack.core.logic.DecisionTreeType;

/**
 * AbstractDecisionTree.
 */
public class AbstractDecisionTree implements DecisionTree {
    private final List<String> literals;
    private final DecisionTreeType type;
    private final Boolean mode;
    private final String literal;
    private final Double average;
    private final Double entropy;
    private final DecisionTree zero;
    private final DecisionTree one;

    /**
     * Constructor.
     *
     * @param literals Literal list
     * @param literal Current literal
     * @param average Average
     * @param entropy Entropy
     * @param zero Zero
     * @param one One
     */
    public AbstractDecisionTree(
        List<String> literals,
        String literal,
        double average,
        double entropy,
        DecisionTree zero,
        DecisionTree one
    ) {
        super();
        this.literals = literals;
        this.literal = literal;
        this.average = average;
        this.entropy = entropy;
        this.zero = zero;
        this.one = one;

        if (this.isLeaf()) {
            this.type = DecisionTreeType.LEAF;
            this.mode = this.getLeafValue();
            return;
        }

        if (this.zero.isLeaf()) {
            this.mode = !this.zero.getLeafValue();
            if (this.one.isLeaf()) {
                this.type = DecisionTreeType.LITERAL;
            } else if (this.zero.getLeafValue()) {
                this.type = DecisionTreeType.LATERAL_1;
            } else {
                this.type = DecisionTreeType.LATERAL_0;
            }
            return;
        }
        if (this.one.isLeaf()) {
            this.mode = this.one.getLeafValue();
            if (this.zero.isLeaf()) {
                this.type = DecisionTreeType.LITERAL;
            } else if (this.one.getLeafValue()) {
                this.type = DecisionTreeType.LATERAL_1;
            } else {
                this.type = DecisionTreeType.LATERAL_0;
            }
            return;
        }

        this.mode = null;
        this.type = DecisionTreeType.COMPLETE;
    }

    @Override
    public List<String> getLiterals() {
        return literals;
    }

    @Override
    public DecisionTreeType getType() {
        return this.type;
    }

    @Override
    public boolean getMode() {
        return this.mode;
    }

    @Override
    public DecisionTree reduceBy(String literal, Boolean value) {
        return null;
    }

    @Override
    public DecisionTree getSubDecisionTree(boolean value) {
        return value ? this.one : this.zero;
    }

    @Override
    public String getLiteral() {
        return this.literal;
    }

    @Override
    public boolean isLeaf() {
        return this.entropy == 0.0;
    }

    @Override
    public boolean getLeafValue() {
        if (!isLeaf()) {
            throw new RuntimeException("not leaf!!");
        }
        return average != 0.0;
    }

    @Override
    public double getEntropy() {
        return this.entropy;
    }

    @Override
    public double getAverage() {
        return this.average;
    }

    @Override
    public String getColor() {
        if (this.getType() == DecisionTreeType.COMPLETE) {
            return "\"black\"";
        }

        return "\"" + (this.getMode() ? "blue" : "red") + "\"";
    }

    @Override
    public String getShape() {
        switch (this.getType()) {
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
    public String getLabel() {
        switch (this.getType()) {
            case LEAF:
                return "\"" + Math.round(this.getAverage()) + "\"";
            case COMPLETE:
            case LATERAL_0:
            case LATERAL_1:
                return "\"" + (this.getLiteral() + " (" + (Math.round(this.getEntropy() * 100.0) / 100.0) + "): ")
                        + this.getAverage() + "\"";
            case LITERAL:
                return "\"" + this.getLiteral() + "\"";
            default:
                return "\"-\"";
        }
    }

    @Override
    public int compareTo(GraphVizNode arg0) {
        return this.hashCode() - arg0.hashCode();
    }

    @Override
    public Collection<GraphVizLink> getLinks() {
        ArrayList<GraphVizLink> ret = new ArrayList<>();

        if (this.getType() != DecisionTreeType.LITERAL) {
            if (this.zero != null) {
                ret.add(new GraphVizDefaultLink(this, this.zero, null, "\"" + this.getLiteral() + " = 0\"", null));
            }
            if (this.one != null) {
                ret.add(new GraphVizDefaultLink(this, this.one, null, "\"" + this.getLiteral() + " = 1\"", null));
            }
        }

        return ret;
    }

    @Override
    public String toJsonString() {
        if (this.isLeaf()) {
            return this.getLeafValue() ? "true" : "false";
        }

        String ret = "{\"literal\":\"" + this.getLiteral() + "\"";
        ret += ",\"expression\":\"" + this.hashCode() + "\"";
        ret += ",\"entropy\":" + this.getEntropy();
        ret += ",\"average\":" + this.getAverage();
        ret += ",\"false\":" + this.zero.toJsonString();
        ret += ",\"true\":" + this.one.toJsonString();
        return ret + "}";
    }
}
