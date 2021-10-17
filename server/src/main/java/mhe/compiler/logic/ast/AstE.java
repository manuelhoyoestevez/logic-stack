package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.LambdaAbstractSyntaxTree;

public class AstE extends Ast implements LambdaAbstractSyntaxTree<LogicSemanticCategory> {
    public final boolean notLambda;

    public AstE() {
        super(LogicSemanticCategory.EQLOGI);
        notLambda = false;
    }

    public AstE(LambdaAbstractSyntaxTree<LogicSemanticCategory> c, LambdaAbstractSyntaxTree<LogicSemanticCategory> e) {
        super(LogicSemanticCategory.EQLOGI);
        notLambda = true;
        this.getChildren().add(c);

        if(e.isNotLambda()) {
            this.getChildren().add(e);
        }
    }

    @Override
    public boolean isNotLambda() {
        return notLambda;
    }

    @Override
    public String getShape() {
        return "\"rectangle\"";
    }

    @Override
    public String getLabel() {
        return "\"AstE <>\"";
    }

    @Override
    public String getColor() {
        return "\"green\"";
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        AbstractSyntaxTree<LogicSemanticCategory> a = this.getFirstChild();
        AbstractSyntaxTree<LogicSemanticCategory> b = this.getSecondChild();

        if(a == null) {
            return null;
        }
        else if(b == null) {
            return a.toJson(literalsOrder);
        }
        else {
            String A = a.toJson(literalsOrder);
            String B = b.toJson(literalsOrder);
            String notA = notJson(A, literalsOrder);
            String notB = notJson(B, literalsOrder);
            String AOrNotB = orJson(A, notB, literalsOrder);
            String notAorB = orJson(notA, B, literalsOrder);
            return andJson(AOrNotB, notAorB, literalsOrder);
        }
    }
}