package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstShow.
 */
public class AstShow extends Ast
{
    private final String name;

    public AstShow(String name)
    {
        super(LogicSemanticCategory.SHOWLOGI);
        this.name = name;
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        return null;
    }
}
