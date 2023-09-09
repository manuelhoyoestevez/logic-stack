package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstExit.
 */
public class AstExit extends Ast
{

    public AstExit()
    {
        super(LogicSemanticCategory.EXITLOGI);
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        return null;
    }
}
