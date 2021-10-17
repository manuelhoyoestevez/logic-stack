package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.LambdaAbstractSyntaxTree;
import mhe.compiler.model.NoLambdaAbstractSyntaxTree;

public class AstP extends Ast implements LambdaAbstractSyntaxTree<LogicSemanticCategory> {
    public final boolean notLambda;

    public AstP() {
        super(LogicSemanticCategory.CODELOGI);
        this.notLambda = false;
    }

    public AstP(NoLambdaAbstractSyntaxTree<LogicSemanticCategory> s, LambdaAbstractSyntaxTree<LogicSemanticCategory> p) {
        super(LogicSemanticCategory.CODELOGI);
        this.notLambda = true;
        this.getChildren().add(s);
        this.getChildren().addAll(p.getChildren());
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
        return "AstP code";
    }

    @Override
    public String getColor() {
        return "black";
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        for(AbstractSyntaxTree<LogicSemanticCategory> child : this.getChildren()) {
            if(child.getType() == LogicSemanticCategory.RETURNLOGI) {
                return child.toJson(literalsOrder);
            }
        }
        return null;
    }
}
