package com.mhe.dev.compiler.logic.core.compiler.logger.ast;

import com.mhe.dev.compiler.logic.core.compiler.logger.LogicSemanticCategory;
import com.mhe.dev.compiler.logic.core.compiler.logger.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.logger.LambdaAbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.logger.NoLambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstP.
 */
public class AstP extends Ast implements LambdaAbstractSyntaxTree<LogicSemanticCategory>
{
    public final boolean notLambda;

    public AstP()
    {
        super(LogicSemanticCategory.CODELOGI);
        this.notLambda = false;
    }

    /**
     * Constructor.
     *
     * @param s Left operand.
     * @param p Right operand.
     */
    public AstP(NoLambdaAbstractSyntaxTree<LogicSemanticCategory> s,
                LambdaAbstractSyntaxTree<LogicSemanticCategory> p)
    {
        super(LogicSemanticCategory.CODELOGI);
        this.notLambda = true;
        this.getChildren().add(s);
        this.getChildren().addAll(p.getChildren());
    }

    @Override
    public boolean isNotLambda()
    {
        return notLambda;
    }

    @Override
    public String getShape()
    {
        return "rectangle";
    }

    @Override
    public String getLabel()
    {
        return "AstP code";
    }

    @Override
    public String getColor()
    {
        return "black";
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