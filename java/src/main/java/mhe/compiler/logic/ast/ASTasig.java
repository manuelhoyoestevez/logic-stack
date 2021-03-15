package mhe.compiler.logic.ast;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSintaxTree;

public class ASTasig extends AST {

    public ASTasig(String n) {
        super(LogicSemanticCategory.ASIGLOGI, true, n);
    }

    public ASTasig(String n, AbstractSintaxTree<LogicSemanticCategory> e) {
        this(n);
        this.getChildren().add(e);
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "ASTasig " + this.getName();
    }

    @Override
    public String getColor() {
        return "orange";
    }
}
