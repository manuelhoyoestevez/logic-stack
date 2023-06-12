package com.mhe.dev.compiler.logic.core.compiler.logger.ast;

import com.mhe.dev.compiler.logic.core.compiler.logger.LogicSemanticCategory;
import com.mhe.dev.compiler.logic.core.compiler.logger.NoLambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstSave.
 */
public class AstSave extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory>
{

    private final String name;

    public AstSave(String name)
    {
        super(LogicSemanticCategory.SAVELOGI);
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
        return "save " + name;
    }

    @Override
    public String getColor()
    {
        return "purple";
    }

}
