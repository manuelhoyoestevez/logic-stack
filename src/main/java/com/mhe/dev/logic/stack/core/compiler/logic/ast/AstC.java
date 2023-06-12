package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.AbstractSyntaxTree;
import com.mhe.dev.logic.stack.core.compiler.model.LambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstC.
 */
public class AstC extends Ast implements LambdaAbstractSyntaxTree<LogicSemanticCategory>
{
    public final boolean notLambda;

    public AstC()
    {
        super(LogicSemanticCategory.CONDLOGI);
        this.notLambda = false;
    }

    /**
     * Constructor.
     *
     * @param a Left operand
     * @param c Right operand
     */
    public AstC(LambdaAbstractSyntaxTree<LogicSemanticCategory> a, LambdaAbstractSyntaxTree<LogicSemanticCategory> c)
    {
        super(LogicSemanticCategory.CONDLOGI);
        this.notLambda = true;
        this.getChildren().add(a);

        if (c.isNotLambda())
        {
            this.getChildren().add(c);
        }
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
        return "ASTc ->";
    }

    @Override
    public String getColor()
    {
        return "purple";
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        AbstractSyntaxTree<LogicSemanticCategory> first = getFirstChild();
        AbstractSyntaxTree<LogicSemanticCategory> second = getSecondChild();

        if (first == null)
        {
            return null;
        } else if (second == null)
        {
            return first.toJson(literalsOrder);
        } else
        {
            return orJson(notJson(first.toJson(literalsOrder), literalsOrder), second.toJson(literalsOrder),
                literalsOrder);
        }
    }
}
