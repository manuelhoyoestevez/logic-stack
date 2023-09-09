package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstP.
 */
public class AstP extends Ast
{
    public AstP()
    {
        super(LogicSemanticCategory.CODELOGI);
    }

    /**
     * Constructor.
     *
     * @param s Left operand.
     * @param p Right operand.
     */
    public AstP(AbstractSyntaxTree<LogicSemanticCategory> s, AbstractSyntaxTree<LogicSemanticCategory> p)
    {
        super(LogicSemanticCategory.CODELOGI);
        this.getChildren().add(s);
        this.getChildren().addAll(p.getChildren());
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        for (AbstractSyntaxTree<LogicSemanticCategory> child : this.getChildren())
        {
            if (child.getType() == LogicSemanticCategory.RETURNLOGI)
            {
                return child.toJson(literalsOrder);
            }
        }
        return null;
    }
}
