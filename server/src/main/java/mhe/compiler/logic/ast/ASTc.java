package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;

public class ASTc extends AST {

    public ASTc() {
        super(LogicSemanticCategory.CONDLOGI, true, null);
    }

    public ASTc(AbstractSyntaxTree<LogicSemanticCategory> a, AbstractSyntaxTree<LogicSemanticCategory> c) {
        this();
        this.getChildren().add(a);

        if(c.isNotLambda()) {
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
        AbstractSyntaxTree<LogicSemanticCategory> first = this.getFirstChild();
        AbstractSyntaxTree<LogicSemanticCategory> second = this.getSecondChild();

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
