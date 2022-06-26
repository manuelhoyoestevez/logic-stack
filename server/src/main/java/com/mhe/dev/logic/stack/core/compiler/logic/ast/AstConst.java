package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.NoLambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstConst.
 */
public class AstConst extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory>
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
    public String getShape()
    {
        return "square";
    }

    @Override
    public String getLabel()
    {
        return name;
    }

    @Override
    public String getColor()
    {
        return "red";
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
