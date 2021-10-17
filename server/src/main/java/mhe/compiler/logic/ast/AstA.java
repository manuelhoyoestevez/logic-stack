package mhe.compiler.logic.ast;

import java.util.ArrayList;
import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.LambdaAbstractSyntaxTree;

public class AstA extends Ast implements LambdaAbstractSyntaxTree<LogicSemanticCategory> {
    public final boolean notLambda;

    public AstA() {
        super(LogicSemanticCategory.ANDLOGI);
        this.notLambda = false;
    }

    public AstA(LambdaAbstractSyntaxTree<LogicSemanticCategory> a, LambdaAbstractSyntaxTree<LogicSemanticCategory> o) {
        super(LogicSemanticCategory.ANDLOGI);
        this.notLambda = true;
        this.getChildren().add(a);
        this.getChildren().addAll(o.getChildren());
    }

    @Override
    public boolean isNotLambda() {
        return notLambda;
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "AstA &";
    }

    @Override
    public String getColor() {
        return "blue";
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        switch(this.getChildren().size()) {
        case 0: return null;
        case 1: return this.getFirstChild().toJson(literalsOrder);
        default:
            List<String> newChildren = new ArrayList<>();

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
