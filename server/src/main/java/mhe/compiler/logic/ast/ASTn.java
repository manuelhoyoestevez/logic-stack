package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSintaxTree;

public class ASTn extends AST {

    public ASTn() {
        super(LogicSemanticCategory.NOTLOGI, true, null);
    }

    public ASTn(AbstractSintaxTree<LogicSemanticCategory> l) {
        this();
        this.getChildren().add(l);
    }

    @Override
    public String getShape() {
        return "\"circle\"";
    }

    @Override
    public String getLabel() {
        return "\"ASTn !\"";
    }

    @Override
    public String getColor() {
        return "\"orange\"";
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        AbstractSintaxTree<LogicSemanticCategory> first = this.getFirstChild();
        if(first == null) {
            return null;
        }
        else {
            return notJson(first.toJson(literalsOrder), literalsOrder);
        }
    }
}
