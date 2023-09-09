package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstC.
 */
public class AstC extends Ast
{

    public AstC()
    {
        super(LogicSemanticCategory.CONDLOGI);
    }

    /**
     * Constructor.
     *
     * @param a Left operand
     * @param c Right operand
     */
    public AstC(AbstractSyntaxTree<LogicSemanticCategory> a, AbstractSyntaxTree<LogicSemanticCategory> c)
    {
        super(LogicSemanticCategory.CONDLOGI);
        this.getChildren().add(a);

        if (!c.getChildren().isEmpty())
        {
            this.getChildren().add(c);
        }
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        AbstractSyntaxTree<LogicSemanticCategory> first = getFirstChild();
        AbstractSyntaxTree<LogicSemanticCategory> second = getSecondChild();

        if (first == null)
        {
            return null;
        } else if (second == null)
        {
            return first.toJson(literalsOrder);
        } else
        {
            return orJson(notJson(first.toJson(literalsOrder), literalsOrder), second.toJson(literalsOrder),
                literalsOrder);
        }
    }
}
