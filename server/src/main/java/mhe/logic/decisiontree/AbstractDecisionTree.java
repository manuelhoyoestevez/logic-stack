package mhe.logic.decisiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhe.graphviz.GraphVizDefaultLink;
import mhe.graphviz.GraphVizLink;
import mhe.graphviz.GraphVizNode;
import mhe.logic.AbstractLogicFunction;
import mhe.logic.DecisionTree;
import mhe.logic.DecisionTreeType;

public class AbstractDecisionTree extends AbstractLogicFunction implements DecisionTree {
    private DecisionTreeType type;
    private Boolean mode;
    private String literal;
    private Double average;
    private Double entropy;
    private DecisionTree zero;
    private DecisionTree one;

    public AbstractDecisionTree(List<String> literals, String literal, Double average, Double entropy, DecisionTree zero, DecisionTree one) {
        super();
        this.setLiterals(literals);
        this.literal = literal;
        this.average = average;
        this.entropy = entropy;
        this.zero = zero;
        this.one = one;

        if(this.isLeaf()) {
            this.type = DecisionTreeType.LEAF;
            this.mode = this.getLeafValue();
        }
        else if(this.zero.isLeaf()) {
            this.mode = !this.zero.getLeafValue();
            if(this.one.isLeaf()) {
                this.type = DecisionTreeType.LITERAL;
            }
            else if(this.zero.getLeafValue()) {
                this.type = DecisionTreeType.LATERAL_1;
            }
            else {
                this.type = DecisionTreeType.LATERAL_0;
            }
        }
        else if(this.one.isLeaf()) {
            this.mode = this.one.getLeafValue();
            if(this.zero.isLeaf()) {
                this.type = DecisionTreeType.LITERAL;
            }
            else if(this.one.getLeafValue()) {
                this.type = DecisionTreeType.LATERAL_1;
            }
            else {
                this.type = DecisionTreeType.LATERAL_0;
            }
        }
        else {
            this.mode = null;
            this.type = DecisionTreeType.COMPLETE;
        }
    }

    @Override
    public DecisionTreeType getType() {
        return this.type;
    }

    @Override
    public Boolean getMode() {
        return this.mode;
    }

    @Override
    public DecisionTree reduceBy(String literal, Boolean value) {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put(literal, value);
        return this.reduceBy(map);
    }

    @Override
    public DecisionTree reduceBy(Map<String, Boolean> values) {
        // TODO Auto-generated method stub
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
    public Boolean isLeaf() {
        return this.entropy == 0.0;
    }

    @Override
    public Boolean getLeafValue() {
        return this.isLeaf() ? this.getAverage() == 0.0 ? false : true : null;
    }

    @Override
    public Double getEntropy() {
        return this.entropy;
    }

    @Override
    public Double getAverage() {
        return this.average;
    }

    @Override
    public int getSerial() {
        return this.hashCode();
    }

    @Override
    public String getColor() {
        switch(this.getType()) {
            case COMPLETE:  return "\"black\"";
            default:        return "\"" + (this.getMode() ? "blue" : "red") + "\"";
        }
    }

    @Override
    public String getShape() {
        switch(this.getType()) {
            case LEAF:      return "\"rectangle\"";
            case COMPLETE:  return "\"octagon\"";
            case LATERAL_0: return "\"invtrapezium\"";
            case LATERAL_1: return "\"trapezium\"";
            case LITERAL:   return "\"ellipse\"";
            default:        return "\"rectangle\"";
        }

    }

    @Override
    public String getLabel() {
        switch(this.getType()) {
            case LEAF:      return "\"" + Math.round(this.getAverage()) + "\"";
            case COMPLETE:  return "\"" + (this.getLiteral() + " (" + (Math.round(this.getEntropy() * 100.0) / 100.0) + "): ") + this.getAverage() + "\"";
            case LATERAL_0: return "\"" + (this.getLiteral() + " (" + (Math.round(this.getEntropy() * 100.0) / 100.0) + "): ") + this.getAverage() + "\"";
            case LATERAL_1: return "\"" + (this.getLiteral() + " (" + (Math.round(this.getEntropy() * 100.0) / 100.0) + "): ") + this.getAverage() + "\"";
            case LITERAL:   return "\"" + this.getLiteral() + "\"";
            default:        return "\"-\"";
        }
    }

    @Override
    public int compareTo(GraphVizNode arg0) {
        return this.getSerial() - arg0.getSerial();
    }

    @Override
    public Collection<GraphVizLink> getLinks() {
        ArrayList<GraphVizLink> ret = new ArrayList<GraphVizLink>();

        if(this.getType() != DecisionTreeType.LITERAL) {
            if(this.zero != null) {
                ret.add(new GraphVizDefaultLink(this, this.zero, null, "\"" + this.getLiteral() + " = 0\"", null));
            }
            if(this.one != null) {
                ret.add(new GraphVizDefaultLink(this, this.one, null, "\"" + this.getLiteral() + " = 1\"", null));
            }
        }

        return ret;
    }

    @Override
    public String toJsonString() {
        if(this.isLeaf()) {
            return this.getLeafValue() ? "true" : "false";
        }

        String ret = "{\"literal\":\"" + this.getLiteral() + "\"";
        ret+= ",\"expression\":\"" + this.getSerial() + "\"";
        ret+= ",\"entropy\":" + this.getEntropy();
        ret+= ",\"average\":" + this.getAverage();
        ret+= ",\"false\":" + this.zero.toJsonString();
        ret+= ",\"true\":" + this.one.toJsonString();
        return ret + "}";
    }
}
