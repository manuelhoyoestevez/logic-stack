package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstAssignment.
 */
public class AstAssignment extends Ast
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
}
