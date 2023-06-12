package com.mhe.dev.compiler.logic.core.compiler.logger.ast;

import com.mhe.dev.compiler.logic.core.compiler.logger.LogicSemanticCategory;
import com.mhe.dev.compiler.logic.core.compiler.logger.NoLambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstShow.
 */
public class AstShow extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory>
{
    private final String name;

    public AstShow(String name)
    {
        super(LogicSemanticCategory.SHOWLOGI);
        this.name = name;
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
        return "show " + name;
    }

    @Override
    public String getColor()
    {
        return "green";
    }
}
