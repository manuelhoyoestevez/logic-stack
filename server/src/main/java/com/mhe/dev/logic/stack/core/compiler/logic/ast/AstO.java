package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import java.util.ArrayList;
import java.util.List;
import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.AbstractSyntaxTree;
import com.mhe.dev.logic.stack.core.compiler.model.LambdaAbstractSyntaxTree;

/**
 * AstO.
 */
public class AstO extends Ast implements LambdaAbstractSyntaxTree<LogicSemanticCategory> {
    public final boolean notLambda;

    public AstO() {
        super(LogicSemanticCategory.ORLOGI);
        this.notLambda = false;
    }

    /**
     * Constructor.
     *
     * @param n Left operand.
     * @param o Right operand.
     */
    public AstO(AbstractSyntaxTree<LogicSemanticCategory> n, LambdaAbstractSyntaxTree<LogicSemanticCategory> o) {
        super(LogicSemanticCategory.ORLOGI);
        this.notLambda = true;
        this.getChildren().add(n);
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
        return "AstO |";
    }

    @Override
    public String getColor() {
        return "red";
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        switch (this.getChildren().size()) {
            case 0:
                return null;
            case 1:
                return this.getFirstChild().toJson(literalsOrder);
            default:
                List<String> newChildren = new ArrayList<>();

                for (AbstractSyntaxTree<LogicSemanticCategory> child : this.getChildren()) {
                    String aux = child.toJson(literalsOrder);
                    if (aux != null) {
                        newChildren.add(aux);
                    }
                }

                return opJson("or", newChildren, literalsOrder);
        }
    }
}
