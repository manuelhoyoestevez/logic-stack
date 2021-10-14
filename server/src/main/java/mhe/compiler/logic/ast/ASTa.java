package mhe.compiler.logic.ast;

import java.util.ArrayList;
import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;

public class ASTa extends AST {

    public ASTa() {
        super(LogicSemanticCategory.ANDLOGI, true, null);
    }

    public ASTa(AbstractSyntaxTree<LogicSemanticCategory> a, AbstractSyntaxTree<LogicSemanticCategory> o) {
        this();
        this.getChildren().add(a);
        this.getChildren().addAll(o.getChildren());
    }

    @Override
    public String getShape() {
        return quotify("rectangle");
    }

    @Override
    public String getLabel() {
        return quotify("ASTa &");
    }

    @Override
    public String getColor() {
        return quotify("blue");
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        switch(this.getChildren().size()) {
        case 0: return null;
        case 1: return this.getFirstChild().toJson(literalsOrder);
        default:
            List<String> newChildren = new ArrayList<String>();

            for(AbstractSyntaxTree<LogicSemanticCategory> child : this.getChildren()) {
                String aux = child.toJson(null);
                if(aux != null) {
                    newChildren.add(aux);
                }
            }

            return opJson("and", newChildren, literalsOrder);
        }
    }
}
