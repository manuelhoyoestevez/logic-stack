package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstId.
 */
public class AstId extends Ast
{

    private final String name;

    public AstId(String name)
    {
        super(LogicSemanticCategory.LITLOGI);
        this.name = name;
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        return literalJson(name);
    }
}
