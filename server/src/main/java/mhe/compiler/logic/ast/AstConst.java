package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.NoLambdaAbstractSyntaxTree;

public class AstConst extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory> {

    /** Value */
    private final boolean value;
    private final String name;

    public final static AstConst ZERO = new AstConst(false);
    public final static AstConst ONE  = new AstConst(true);

    public AstConst(boolean value) {
        super(LogicSemanticCategory.CONSTLOGI);
        this.value = value;
        this.name = value ? "1" : "0";
    }

    @Override
    public String getShape() {
        return quote("square");
    }

    @Override
    public String getLabel() {
        return quote(name);
    }

    @Override
    public String getColor() {
        return quote("red");
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        return constJson(this.getValue());
    }

    public boolean getValue() {
        return value;
    }
}
