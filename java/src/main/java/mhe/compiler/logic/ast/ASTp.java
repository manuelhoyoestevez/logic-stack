package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSintaxTree;

public class ASTp extends AST {

    public ASTp() {
        super(LogicSemanticCategory.CODELOGI,true,null);
    }

    public ASTp(AbstractSintaxTree<LogicSemanticCategory> s, AbstractSintaxTree<LogicSemanticCategory> p) {
        this();
        this.getChildren().add(s);
        this.getChildren().addAll(p.getChildren());
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "ASTp code";
    }

    @Override
    public String getColor() {
        return "black";
    }

    public AbstractSintaxTree<LogicSemanticCategory> cloneEmpty() {
        return new ASTp();
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        for(AbstractSintaxTree<LogicSemanticCategory> child : this.getChildren()) {
            if(child.getType() == LogicSemanticCategory.RETURNLOGI) {
                return child.toJson(literalsOrder);
            }
        }
        return null;
    }
}
