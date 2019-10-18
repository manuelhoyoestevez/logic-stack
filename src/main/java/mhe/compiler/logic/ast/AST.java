package mhe.compiler.logic.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSintaxTree;
import mhe.graphviz.GraphVizDefaultLink;
import mhe.graphviz.GraphVizLink;
import mhe.graphviz.GraphVizNode;

/** Abstract Syntax Tree generic node for Regular Expressions Parser
 * @author Manuel Hoyo Estévez
 */
public abstract class AST implements AbstractSintaxTree<LogicSemanticCategory>, GraphVizNode {
    /** Serial Counter */
    private static int s = 0;
    /** Serial Number */
    private int k;
    /** Type */
    private LogicSemanticCategory t;
    /** Value */
    private boolean v;
    /** Name */
    private String n;
    /** Children */
    private LinkedList<AbstractSintaxTree<LogicSemanticCategory>> children;

    @Override
    public LinkedList<AbstractSintaxTree<LogicSemanticCategory>> getChildren(){
        return this.children;
    }

    public static String quotify(String str) {
        return "\"" + str + "\"";
    }

    public static final AST ASTzero    = new ASTconst(false);
    public static final AST ASTone     = new ASTconst(true);
    public static final AST ASTlambda  = new ASTlambda();

    public AST(LogicSemanticCategory t, boolean v, String n){
        this.k = ++s;
        this.t = t;
        this.v = v;
        this.n = n;
        this.children = new LinkedList<AbstractSintaxTree<LogicSemanticCategory>>();
    }

    public static AST constant(boolean v) {
        return v ? ASTone : ASTzero;
    }

    @Override
    public AbstractSintaxTree<LogicSemanticCategory> getFirstChild() {
        if(this.getChildren().isEmpty()) {
            return null;
        }
        else {
            return this.getChildren().getFirst();
        }
    }

    @Override
    public AbstractSintaxTree<LogicSemanticCategory> getSecondChild() {
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
    public boolean isLambda(){
        return false;
    }

    @Override
    public Collection<GraphVizLink> getLinks() {
        Collection<GraphVizLink> ret = new ArrayList<GraphVizLink>();

        for(AbstractSintaxTree<LogicSemanticCategory> e : this.getChildren()) {
            ret.add(new GraphVizDefaultLink(this,e));
        }
        this.hashCode();
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
        String r = "";

        for(int i = 0;i < p;i++) {
            r += " ";
        }

        r += this.getLabel() + '\n';

        for(AbstractSintaxTree<LogicSemanticCategory> e : this.getChildren()) {
            r += ((AST)e).toString(p + 1);
        }

        return r;
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

        String json = quotify("order") + ":[";

        boolean f = true;

        for(String literal : order) {
            if(f) {
                f = false;
            }
            else {
                json += ",";
            }
            json += quotify(literal);
        }

        return json + "],";
    }

    public static String opJson(String operator, List<String> nodes, List<String> order) {
        String json = "{"
            + quotify("operator") + ":" + quotify(operator) + ","
            + orderJson(order)
            + quotify("children") + ":[";

        boolean f = true;

        for(String node : nodes) {
            if(f) {
                f = false;
            }
            else {
                json += ",";
            }

            json += node;
        }

        return json + "]}";
    }

    public static String notJson(String node, List<String> order) {
        return opJson("not", Arrays.asList(node), order);
    }

    public static String orJson(String nodeA, String nodeB, List<String> order) {
        return opJson("or", Arrays.asList(nodeA, nodeB), order);
    }

    public static String andJson(String nodeA, String nodeB, List<String> order) {
        return opJson("and", Arrays.asList(nodeA, nodeB), order);
    }
}
