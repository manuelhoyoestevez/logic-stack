package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.ArrayList;
import java.util.List;

/**
 * AstA.
 */
public class AstA extends Ast
{
    public AstA()
    {
        super(LogicSemanticCategory.ANDLOGI);
    }

    /**
     * Constructor .
     *
     * @param a Left operand.
     * @param o Right operand.
     */
    public AstA(AbstractSyntaxTree<LogicSemanticCategory> a, AbstractSyntaxTree<LogicSemanticCategory> o)
    {
        super(LogicSemanticCategory.ANDLOGI);
        this.getChildren().add(a);
        this.getChildren().addAll(o.getChildren());
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        switch (this.getChildren().size())
        {
            case 0:
                return null;
            case 1:
                return this.getFirstChild().toJson(literalsOrder);
            default:
                List<String> newChildren = new ArrayList<>();

                for (AbstractSyntaxTree<LogicSemanticCategory> child : this.getChildren())
                {
                    String aux = child.toJson(null);
                    if (aux != null)
                    {
                        newChildren.add(aux);
                    }
                }

                return opJson("and", newChildren, literalsOrder);
        }
    }
}
