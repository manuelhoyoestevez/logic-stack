package mhe.logic.expressiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private List<String> weights = null;

    protected List<String> getWeights() {
        return this.weights;
    }

    public AbstractExpressionTree(
            ExpressionTreeType type,
            boolean mode,
            String literal,
            SortedSet<ExpressionTree> children,
            List<String> weights
    ) {
        super();
        this.type     = type;
        this.mode     = mode;
        this.literal  = literal;
        this.children = children;
        this.weights  = weights;

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

        Collections.sort(literals, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int w1 = weights.indexOf(o1);
                int w2 = weights.indexOf(o2);

                if(w1 < 0) {
                    w1 = weights.size();
                }
                if(w2 < 0) {
                    w2 = weights.size();
                }

                return w1 - w2;
            }
        });

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
    public ExpressionTree reduce() {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        return this.reduceBy(map);
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
    public Boolean equivalent(ExpressionTree expressionTree) {
        return this.reduce().getExpression().compareTo(expressionTree.reduce().getExpression()) == 0;
    }

    @Override
    public Boolean complementary(ExpressionTree expressionTree) {
        return this.generateNot().reduce().equivalent(expressionTree.reduce());
    }

    @Override
    public String getShape() {
        switch(this.type) {
            case OPERATOR:
                return quotify(this.getChildren().isEmpty() ? "rectangle" : "ellipse");
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
                if(this.getMode()) {
                    return quotify(this.getLiteral());
                }
                else {
                    return quotify("!" + this.getLiteral());
                }
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
                if(this.getMode()) {
                    return quotify("blue");
                }
                else {
                    return quotify("red");
                }
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
                        new TreeSet<ExpressionTree>(),
                        this.getWeights()
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
                        newChildren,
                        this.getWeights()
                );
        }
        return this;
    }

    protected static ExpressionTree addToChildren(Boolean mode, Set<ExpressionTree> children, ExpressionTree newChild) {
        for(ExpressionTree child : children) {

            if(child.equivalent(newChild)) {
                return null;
            }
            if(child.complementary(newChild)){
                return new AbstractExpressionTree(
                        ExpressionTreeType.OPERATOR,
                        !mode,
                        null,
                        new TreeSet<ExpressionTree>(),
                        new ArrayList<String>()
                );
            }
        }

        children.add(newChild);

        return null;
    }

    @Override
    public ExpressionTree reduceBy(Map<String, Boolean> values) {
        ExpressionTree ret = null, child;

        switch(this.getType()) {
            case LITERAL:
                Boolean value = values.get(this.getLiteral());
                return value == null ? this : new AbstractExpressionTree(
                        ExpressionTreeType.OPERATOR,
                        value == this.getMode(),
                        null,
                        new TreeSet<ExpressionTree>(),
                        this.getWeights()
                );

            case NOT:
                child = this.getChildren().first();
                return child.generateNot().reduceBy(values);

            case OPERATOR:
                ExpressionTree finalChild;
                SortedSet<ExpressionTree> newChildren = new TreeSet<ExpressionTree>();
                for(ExpressionTree c : this.getChildren()) {
                    child = c.reduceBy(values);

                    switch(child.getType()) {
                        case NOT: // Error de programacion
                        case LITERAL:
                            finalChild = addToChildren(this.getMode(), newChildren, child);

                            if(finalChild != null) {
                                return finalChild;
                            }

                            break;
                        case OPERATOR:
                            if(this.getMode() == child.getMode()) {
                                for(ExpressionTree d : child.getChildren()) {
                                    finalChild = addToChildren(this.getMode(), newChildren, d);

                                    if(finalChild != null) {
                                        return finalChild;
                                    }
                                }
                            }
                            else if(child.isFinal()){
                                return child;
                            }
                            else {
                                finalChild = addToChildren(this.getMode(), newChildren, child);

                                if(finalChild != null) {
                                    return finalChild;
                                }
                            }
                            break;
                    }
                }

                return newChildren.size() == 1
                        ? newChildren.first()
                        : new AbstractExpressionTree(
                                ExpressionTreeType.OPERATOR,
                                this.getMode(),
                                null,
                                newChildren,
                                this.getWeights()
                        );
        }
        return ret;
    }

    @Override
    public String getExpression() {
        String ret = "";
        switch(this.getType()) {
            case LITERAL:
                ret += (this.getMode() ? "" : "!") + this.getLiteral();
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

    @Override
    public String toJsonString() {
        switch(this.getType()) {
        case LITERAL:
            return "{"
            + quotify("operator") + ":" + quotify("literal") + ","
            + quotify("literal") + ":" + quotify(literal) + "}";
        case NOT:
            return "{"
            + quotify("operator") + ":" + quotify("not") + ","
            + quotify("children") + ":[" + this.getChildren().first().toJsonString() + "]}";
        case OPERATOR:
            String jsonString = "{"
            + quotify("operator") + ":" + quotify(this.getMode() ? "and" : "or");

            String jsonChildren = "";

            if(!this.getChildren().isEmpty()) {
                boolean f = true;
                jsonChildren += "," + quotify("children") + ":[";

                for(ExpressionTree child : this.getChildren()) {
                    if(f) {
                        f = false;
                    }
                    else {
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
    public String toString() {
        return this.getExpression();
    }
}
