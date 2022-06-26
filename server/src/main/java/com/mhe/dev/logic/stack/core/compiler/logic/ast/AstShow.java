package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.NoLambdaAbstractSyntaxTree;
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
