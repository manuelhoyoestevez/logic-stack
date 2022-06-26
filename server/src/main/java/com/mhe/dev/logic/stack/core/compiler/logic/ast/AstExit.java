package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.NoLambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstExit.
 */
public class AstExit extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory>
{

    public AstExit()
    {
        super(LogicSemanticCategory.EXITLOGI);
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        return null;
    }

    @Override
    public String getShape()
    {
        return "rectangle";
    }

    @Override
    public String getLabel()
    {
        return "exit";
    }

    @Override
    public String getColor()
    {
        return "cyan";
    }
}
