package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.ArrayList;
import java.util.List;

/**
 * AstO.
 */
public class AstO extends Ast
{

    public AstO()
    {
        super(LogicSemanticCategory.ORLOGI);
    }

    /**
     * Constructor.
     *
     * @param n Left operand.
     * @param o Right operand.
     */
    public AstO(AbstractSyntaxTree<LogicSemanticCategory> n, AbstractSyntaxTree<LogicSemanticCategory> o)
    {
        super(LogicSemanticCategory.ORLOGI);
        this.getChildren().add(n);
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
                    String aux = child.toJson(literalsOrder);
                    if (aux != null)
                    {
                        newChildren.add(aux);
                    }
                }

                return opJson("or", newChildren, literalsOrder);
        }
    }
}
