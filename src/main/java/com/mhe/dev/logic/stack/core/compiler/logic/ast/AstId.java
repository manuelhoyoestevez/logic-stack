package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.NoLambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstId.
 */
public class AstId extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory>
{

    private final String name;

    public AstId(String name)
    {
        super(LogicSemanticCategory.LITLOGI);
        this.name = name;
    }

    @Override
    public String getShape()
    {
        return "square";
    }

    @Override
    public String getLabel()
    {
        return name;
    }

    @Override
    public String getColor()
    {
        return "blue";
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        return literalJson(name);
    }
}
