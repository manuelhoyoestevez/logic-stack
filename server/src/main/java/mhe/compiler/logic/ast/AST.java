package mhe.compiler.logic.ast;

import java.util.*;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.graphviz.GraphVizDefaultLink;
import mhe.graphviz.GraphVizLink;
import mhe.graphviz.GraphVizNode;

/** Abstract Syntax Tree generic node for Regular Expressions Parser
 * @author Manuel Hoyo Estévez
 */
public abstract class AST implements AbstractSyntaxTree<LogicSemanticCategory>, GraphVizNode {
    /** Serial Counter */
    private static int s = 0;
    /** Serial Number */
    private final int k;
    /** Type */
    private final LogicSemanticCategory t;
    /** Value */
    private final boolean v;
    /** Name */
    private final String n;
    /** Children */
    private final LinkedList<AbstractSyntaxTree<LogicSemanticCategory>> children;

    @Override
    public LinkedList<AbstractSyntaxTree<LogicSemanticCategory>> getChildren(){
        return this.children;
    }

    public static String quotify(String str) {
        return "\"" + str + "\"";
    }

    public static final AST ASTzero    = ASTconst.ZERO;
    public static final AST ASTone     = ASTconst.ONE;
    public static final AST ASTlambda  = ASTLambda.LAMBDA;

    public AST(LogicSemanticCategory t, boolean v, String n) {
        this.k = ++s;
        this.t = t;
        this.v = v;
        this.n = n;
        this.children = new LinkedList<>();
    }

    public static AST constant(boolean v) {
        return v ? ASTone : ASTzero;
    }

    @Override
    public AbstractSyntaxTree<LogicSemanticCategory> getFirstChild() {
        if(this.getChildren().isEmpty()) {
            return null;
        }
        else {
            return this.getChildren().getFirst();
        }
    }

    @Override
    public AbstractSyntaxTree<LogicSemanticCategory> getSecondChild() {
        if(this.getChildren().size() > 1) {
            return this.getChildren().get(1);
        }
        else {
            return null;
        }
    }

    @Override
    public int getSerial(){
        return this.k;
    }

    @Override
    public LogicSemanticCategory getType() {
        return this.t;
    }

    public boolean getValue(){
        return this.v;
    }

    @Override
    public String getName(){
        return this.n;
    }

    @Override
    public boolean isNotLambda(){
        return true;
    }

    @Override
    public Collection<GraphVizLink> getLinks() {
        Collection<GraphVizLink> ret = new ArrayList<GraphVizLink>();

        for(AbstractSyntaxTree<LogicSemanticCategory> e : this.getChildren()) {
            ret.add(new GraphVizDefaultLink(this,e));
        }

        return ret;
    }

    @Override
    public int compareTo(GraphVizNode node){
        Integer x = this.getSerial();
        Integer y = node.getSerial();
        return x.compareTo(y);
    }

    @Override
    public String toString(){
        return this.toString(0);
    }

    private String toString(int p){
        StringBuilder r = new StringBuilder();

        for(int i = 0;i < p;i++) {
            r.append(" ");
        }

        r.append(this.getLabel()).append('\n');

        for(AbstractSyntaxTree<LogicSemanticCategory> e : this.getChildren()) {
            r.append(((AST) e).toString(p + 1));
        }

        return r.toString();
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        return null;
    }

    public static String constJson(boolean value) {
        return "{" + quotify("operator") + ":" + quotify(value ? "and" : "or") + "}";
    }

    public static String literalJson(String literal) {
        return "{"
            + quotify("operator") + ":" + quotify("literal") + ","
            + quotify("literal") + ":" + quotify(literal) + "}";
    }

    public static String orderJson(List<String> order) {
        if (order == null){
            return "";
        }

        StringBuilder json = new StringBuilder(quotify("order") + ":[");

        boolean f = true;

        for(String literal : order) {
            if(f) {
                f = false;
            }
            else {
                json.append(",");
            }
            json.append(quotify(literal));
        }

        return json + "],";
    }

    static String opJson(String operator, List<String> nodes, List<String> order) {
        StringBuilder json = new StringBuilder("{"
                + quotify("operator") + ":" + quotify(operator) + ","
                + orderJson(order)
                + quotify("children") + ":[");

        boolean f = true;

        for(String node : nodes) {
            if(f) {
                f = false;
            }
            else {
                json.append(",");
            }

            json.append(node);
        }

        return json + "]}";
    }

    public static String notJson(String node, List<String> order) {
        return opJson("not", Collections.singletonList(node), order);
    }

    public static String orJson(String nodeA, String nodeB, List<String> order) {
        return opJson("or", Arrays.asList(nodeA, nodeB), order);
    }

    public static String andJson(String nodeA, String nodeB, List<String> order) {
        return opJson("and", Arrays.asList(nodeA, nodeB), order);
    }
}
