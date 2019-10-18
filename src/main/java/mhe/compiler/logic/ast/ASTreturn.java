package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSintaxTree;

public class ASTreturn extends AST {

    public ASTreturn() {
        super(LogicSemanticCategory.RETURNLOGI, true, null);
    }

    public ASTreturn(AbstractSintaxTree<LogicSemanticCategory> e) {
        this();
        this.getChildren().add(e);
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        return this.getChildren().isEmpty() ? null : this.getFirstChild().toJson(literalsOrder);
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "ASTreturn";
    }

    @Override
    public String getColor() {
        return "orange";
    }
}
