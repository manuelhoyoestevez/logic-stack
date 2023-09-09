package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstN.
 */
public class AstN extends Ast
{
    public AstN()
    {
        super(LogicSemanticCategory.NOTLOGI);
    }

    public AstN(AbstractSyntaxTree<LogicSemanticCategory> l)
    {
        this();
        this.getChildren().add(l);
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        AbstractSyntaxTree<LogicSemanticCategory> first = getFirstChild();

        if (first == null)
        {
            return null;
        }

        return notJson(first.toJson(literalsOrder), literalsOrder);
    }
}
