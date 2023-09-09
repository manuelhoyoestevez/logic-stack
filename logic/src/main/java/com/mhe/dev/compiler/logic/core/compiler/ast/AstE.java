package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstE.
 */
public class AstE extends Ast
{
    public AstE()
    {
        super(LogicSemanticCategory.EQLOGI);
    }

    /**
     * Constructor.
     *
     * @param c Left operand.
     * @param e Right operand.
     */
    public AstE(AbstractSyntaxTree<LogicSemanticCategory> c, AbstractSyntaxTree<LogicSemanticCategory> e)
    {
        super(LogicSemanticCategory.EQLOGI);
        this.getChildren().add(c);

        if (!e.getChildren().isEmpty())
        {
            this.getChildren().add(e);
        }
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        AbstractSyntaxTree<LogicSemanticCategory> a = this.getFirstChild();
        AbstractSyntaxTree<LogicSemanticCategory> b = this.getSecondChild();

        if (a == null)
        {
            return null;
        }

        if (b == null)
        {
            return a.toJson(literalsOrder);
        }

        String aj = a.toJson(literalsOrder);
        String bj = b.toJson(literalsOrder);
        String notA = notJson(aj, literalsOrder);
        String notB = notJson(bj, literalsOrder);
        return andJson(orJson(aj, notB, literalsOrder), orJson(notA, bj, literalsOrder), literalsOrder);
    }
}
