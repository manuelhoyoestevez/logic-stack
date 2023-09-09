package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstConst.
 */
public class AstConst extends Ast
{
    private final boolean value;
    private final String name;

    /**
     * Constructor.
     *
     * @param value Constant value
     */
    public AstConst(boolean value)
    {
        super(LogicSemanticCategory.CONSTLOGI);
        this.value = value;
        this.name = value ? "1" : "0";
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        return constJson(this.getValue());
    }

    public boolean getValue()
    {
        return value;
    }
}
