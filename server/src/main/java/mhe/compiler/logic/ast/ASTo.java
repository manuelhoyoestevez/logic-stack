package mhe.compiler.logic.ast;

import java.util.ArrayList;
import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;

public class ASTo extends AST {

    public ASTo() {
        super(LogicSemanticCategory.ORLOGI, true, null);
    }

    public ASTo(AbstractSyntaxTree<LogicSemanticCategory> n, AbstractSyntaxTree<LogicSemanticCategory> o) {
        this();
        this.getChildren().add(n);
        this.getChildren().addAll(o.getChildren());
    }

    @Override
    public String getShape() {
        return quotify("rectangle");
    }

    @Override
    public String getLabel() {
        return quotify("ASTo |");
    }

    @Override
    public String getColor() {
        return quotify("red");
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        switch(this.getChildren().size()) {
        case 0: return null;
        case 1: return this.getFirstChild().toJson(literalsOrder);
        default:
            List<String> newChildren = new ArrayList<String>();

            for(AbstractSyntaxTree<LogicSemanticCategory> child : this.getChildren()) {
                String aux = child.toJson(literalsOrder);
                if(aux != null) {
                    newChildren.add(aux);
                }
            }

            return opJson("or", newChildren, literalsOrder);
        }
    }
}
