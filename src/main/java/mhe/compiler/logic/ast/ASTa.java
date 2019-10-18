package mhe.compiler.logic.ast;

import java.util.ArrayList;
import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSintaxTree;

public class ASTa extends AST {

    public ASTa() {
        super(LogicSemanticCategory.ANDLOGI, true, null);
    }

    public ASTa(AbstractSintaxTree<LogicSemanticCategory> a, AbstractSintaxTree<LogicSemanticCategory> o) {
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

    public AbstractSintaxTree<LogicSemanticCategory> cloneEmpty() {
        return new ASTa();
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        switch(this.getChildren().size()) {
        case 0: return null;
        case 1: return this.getFirstChild().toJson(literalsOrder);
        default:
            List<String> newChildren = new ArrayList<String>();

            for(AbstractSintaxTree<LogicSemanticCategory> child : this.getChildren()) {
                String aux = child.toJson(null);
                if(aux != null) {
                    newChildren.add(aux);
                }
            }

            return opJson("and", newChildren, literalsOrder);
        }
    }
}
