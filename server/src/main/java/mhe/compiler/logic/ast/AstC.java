package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.LambdaAbstractSyntaxTree;

public class AstC extends Ast implements LambdaAbstractSyntaxTree<LogicSemanticCategory> {
    public final boolean notLambda;

    public AstC() {
        super(LogicSemanticCategory.CONDLOGI);
        this.notLambda = false;
    }

    public AstC(LambdaAbstractSyntaxTree<LogicSemanticCategory> a, LambdaAbstractSyntaxTree<LogicSemanticCategory> c) {
        super(LogicSemanticCategory.CONDLOGI);
        this.notLambda = true;
        this.getChildren().add(a);

        if(c.isNotLambda()) {
            this.getChildren().add(c);
        }
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
        return "ASTc ->";
    }

    @Override
    public String getColor() {
        return "purple";
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        AbstractSyntaxTree<LogicSemanticCategory> first = getFirstChild();
        AbstractSyntaxTree<LogicSemanticCategory> second = getSecondChild();

        if(first == null) {
            return null;
        }
        else if(second == null) {
            return first.toJson(literalsOrder);
        }
        else {
            return orJson(notJson(first.toJson(literalsOrder), literalsOrder), second.toJson(literalsOrder), literalsOrder);
        }
    }
}
