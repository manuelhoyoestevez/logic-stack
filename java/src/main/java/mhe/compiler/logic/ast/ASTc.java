package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSintaxTree;

public class ASTc extends AST {

    public ASTc() {
        super(LogicSemanticCategory.CONDLOGI, true, null);
    }

    public ASTc(AbstractSintaxTree<LogicSemanticCategory> a, AbstractSintaxTree<LogicSemanticCategory> c) {
        this();
        this.getChildren().add(a);

        if(!c.isLambda()) {
            this.getChildren().add(c);
        }
    }

    @Override
    public String getShape() {
        return quotify("rectangle");
    }

    @Override
    public String getLabel() {
        return quotify("ASTc ->");
    }

    @Override
    public String getColor() {
        return quotify("purple");
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        AbstractSintaxTree<LogicSemanticCategory> first = this.getFirstChild();
        AbstractSintaxTree<LogicSemanticCategory> second = this.getSecondChild();

        if(first == null) {
            return null;
        }
        else if(second == null) {
            return first.toJson(literalsOrder);
        }
        else {
            return orJson(notJson(first.toJson(literalsOrder), literalsOrder), second.toJson(literalsOrder), literalsOrder);
        }
    }
}
