package mhe.logic.expressiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import mhe.graphviz.GraphVizDefaultLink;
import mhe.graphviz.GraphVizLink;
import mhe.graphviz.GraphVizNode;
import mhe.logic.AbstractLogicFunction;
import mhe.logic.ExpressionTree;
import mhe.logic.ExpressionTreeType;

public class AbstractExpressionTree extends AbstractLogicFunction implements ExpressionTree {

    private boolean mode = false;
    private String literal = null;
    private ExpressionTreeType type = null;
    private SortedSet<ExpressionTree> children = null;

    public AbstractExpressionTree(
            ExpressionTreeType type,
            boolean mode,
            String literal,
            SortedSet<ExpressionTree> children
    ) {
        super();
        this.type     = type;
        this.mode     = mode;
        this.literal  = literal;
        this.children = children;

        ArrayList<String> literals = new ArrayList<String>();

        if(this.literal != null) {
            literals.add(this.literal);
        }

        for(ExpressionTree child : this.getChildren()) {
            for(String lit : child.getLiterals()) {
                if(!literals.contains(lit)) {
                    literals.add(lit);
                }
            }
        }

        this.setLiterals(literals);
    }

    public static String quotify(String str) {
        return "\"" + str + "\"";
    }

    @Override
    public int getSerial() {
        return this.hashCode();
    }

    @Override
    public Boolean getMode() {
        return this.mode;
    }

    @Override
    public String getLiteral() {
        return this.literal;
    }

    @Override
    public ExpressionTreeType getType() {
        return this.type;
    }

    @Override
    public SortedSet<ExpressionTree> getChildren() {
        return this.children;
    }

    @Override
    public Boolean isFinal() {
        return this.getChildren().isEmpty();
    }

    @Override
    public ExpressionTree reduceBy(String literal, Boolean value) {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put(literal, value);
        return this.reduceBy(map);
    }

    @Override
    public int compareTo(GraphVizNode gnode) {
        ExpressionTree node = (ExpressionTree) gnode;
        int ret = this.getExpression().compareTo(node.getExpression());

        if(ret == 0) {
            ret = this.hashCode() - gnode.hashCode();
        }

        return ret;
    }

    @Override
    public Collection<GraphVizLink> getLinks() {
        Collection<GraphVizLink> ret = new ArrayList<GraphVizLink>();

        for(ExpressionTree child : this.getChildren()) {
            ret.add(new GraphVizDefaultLink(this, child));
        }

        return ret;
    }

    @Override
    public Boolean equivalent(ExpressionTree node) {
        return this.getExpression().compareTo(node.getExpression()) == 0;
    }

    @Override
    public String getShape() {
        switch(this.type) {
            case OPERATOR:
                return quotify(this.getChildren().isEmpty() ? "ellipse" : "ellipse");
            case LITERAL:
                return quotify("rectangle");
            case NOT:
                return quotify("ellipse");
        }
        return null;
    }

    @Override
    public String getLabel() {
        switch(this.type) {
            case OPERATOR:
                if(this.getChildren().isEmpty()) {
                    if(this.getMode()) {
                        return quotify("1"+ " " + this.getLiterals().toString());
                    }
                    else {
                        return quotify("0"+ " " + this.getLiterals().toString());
                    }
                }
                else {
                    if(this.getMode()) {
                        return quotify("&"+ " " + this.getLiterals().toString());
                    }
                    else {
                        return quotify("|"+ " " + this.getLiterals().toString());
                    }
                }
            case LITERAL:
                return quotify(this.getLiteral());
            case NOT:
                return quotify("!"+ " " + this.getLiterals().toString());
        }
        return null;
    }

    @Override
    public String getColor() {
        switch(this.type) {
            case OPERATOR:
                if(this.getChildren().isEmpty()) {
                    if(this.getMode()) {
                        return quotify("blue");
                    }
                    else {
                        return quotify("red");
                    }
                }
                else {
                    if(this.getMode()) {
                        return quotify("green");
                    }
                    else {
                        return quotify("purple");
                    }
                }
            case LITERAL:
                return quotify("black");
            case NOT:
                return quotify("orange");
        }
        return null;
    }

    @Override
    public ExpressionTree generateNot() {
        switch(this.getType()) {
            case NOT:
                return this.getChildren().first();

            case LITERAL:
                return new AbstractExpressionTree(
                        ExpressionTreeType.LITERAL,
                        !this.getMode(),
                        this.getLiteral(),
                        new TreeSet<ExpressionTree>()
                );

            case OPERATOR:
                SortedSet<ExpressionTree> newChildren = new TreeSet<ExpressionTree>();

                for(ExpressionTree child : this.getChildren()) {
                    newChildren.add(child.generateNot());
                }

                return new AbstractExpressionTree(
                        ExpressionTreeType.OPERATOR,
                        !this.getMode(),
                        null,
                        newChildren
                );
        }
        return this;
    }

    @Override
    public ExpressionTree reduceBy(Map<String, Boolean> values) {
        ExpressionTree ret = null, child;
        SortedSet<ExpressionTree> newChildren = new TreeSet<ExpressionTree>();

        switch(this.getType()) {
            case LITERAL:
                Boolean value = values.get(this.getLiteral());
                return value == null ? this : new AbstractExpressionTree(
                        ExpressionTreeType.OPERATOR,
                        value,
                        null,
                        new TreeSet<ExpressionTree>()
                );

            case NOT:
                child = this.getChildren().first();
                return child.generateNot().reduceBy(values);

            case OPERATOR:
                for(ExpressionTree c : this.getChildren()) {
                    child = c.reduceBy(values);

                    switch(child.getType()) {
                        case LITERAL:
                        case NOT:
                            newChildren.add(child);
                            break;
                        case OPERATOR:
                            if(this.getMode() == child.getMode()) {
                                for(ExpressionTree d : child.getChildren()) {
                                    newChildren.add(d);
                                }
                            }
                            else if(child.isFinal()){
                                return child;
                            }
                            else {
                                newChildren.add(child);
                            }
                            break;
                    }
                }

                return newChildren.size() == 1
                        ? (ExpressionTree) newChildren.first()
                        : new AbstractExpressionTree(
                                ExpressionTreeType.OPERATOR,
                                this.getMode(),
                                null,
                                newChildren
                        );
        }
        return ret;
    }

    @Override
    public String getExpression() {
        String ret = "";
        switch(this.getType()) {
            case LITERAL:
                ret += this.getLiteral();
                break;

            case NOT:
                ret += "!" + this.getChildren().first().getExpression();
                break;

            case OPERATOR:
                switch(this.getChildren().size()) {
                    case 0:
                        ret += this.getMode() ? "1" : "0";
                        break;
                    case 1:
                        ret += this.getChildren().first().getExpression();
                        break;
                    default:
                        boolean f = true;

                        ret += this.getMode() ? "[" : "{";

                        for(ExpressionTree child: this.getChildren()) {
                            if(f) {
                                f = false;
                            }
                            else {
                                ret += ",";
                            }
                            ret += child.getExpression();
                        }
                        ret += this.getMode() ? "]" : "}";
                }
                break;
        }
        return ret;
    }
}
