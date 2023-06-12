package com.mhe.dev.compiler.logic.core.compiler.logger.ast;

import com.mhe.dev.compiler.logic.core.compiler.logger.LogicSemanticCategory;
import com.mhe.dev.compiler.logic.core.compiler.logger.NoLambdaAbstractSyntaxTree;
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
