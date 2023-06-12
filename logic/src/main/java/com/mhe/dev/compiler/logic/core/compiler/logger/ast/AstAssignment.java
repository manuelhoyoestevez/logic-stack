package com.mhe.dev.compiler.logic.core.compiler.logger.ast;

import com.mhe.dev.compiler.logic.core.compiler.logger.LogicSemanticCategory;
import com.mhe.dev.compiler.logic.core.compiler.logger.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.logger.NoLambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstAssignment.
 */
public class AstAssignment extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory>
{

    private final String id;

    /**
     * Constructor.
     *
     * @param id Variable
     * @param e  Valor
     */
    public AstAssignment(String id, AbstractSyntaxTree<LogicSemanticCategory> e)
    {
        super(LogicSemanticCategory.ASIGLOGI);
        this.id = id;
        this.getChildren().add(e);
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
        return "AstAssignment " + id;
    }

    @Override
    public String getColor()
    {
        return "orange";
    }
}
