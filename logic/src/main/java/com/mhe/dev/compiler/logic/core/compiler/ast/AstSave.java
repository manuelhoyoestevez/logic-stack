package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstSave.
 */
public class AstSave extends Ast
{
    private final String name;

    public AstSave(String name)
    {
        super(LogicSemanticCategory.SAVELOGI);
        this.name = name;
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        return null;
    }
}
