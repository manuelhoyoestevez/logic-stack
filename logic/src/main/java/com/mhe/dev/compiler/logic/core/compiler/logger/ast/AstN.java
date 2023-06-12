package com.mhe.dev.compiler.logic.core.compiler.logger.ast;

import com.mhe.dev.compiler.logic.core.compiler.logger.LogicSemanticCategory;
import com.mhe.dev.compiler.logic.core.compiler.logger.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.logger.NoLambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstN.
 */
public class AstN extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory>
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
    public String getShape()
    {
        return "ellipse";
    }

    @Override
    public String getLabel()
    {
        return "AstN !";
    }

    @Override
    public String getColor()
    {
        return "orange";
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
