package mhe.compiler.logic.ast;

import java.util.ArrayList;
import java.util.List;

import mhe.compiler.ASTInterface;

public class ASTa extends AST {

    public ASTa() {
        super(ANDLOGI, true, null);
    }

    public ASTa(ASTInterface a, ASTInterface o) {
        this();
        this.getChildren().add(a);
        this.getChildren().addAll(o.getChildren());
    }

    @Override
    public String getShape() {
        return quotify("rectangle");
    }

    @Override
    public String getLabel() {
        return quotify("ASTa &");
    }

    @Override
    public String getColor() {
        return quotify("blue");
    }

    public ASTInterface cloneEmpty() {
        return new ASTa();
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        switch(this.getChildren().size()) {
        case 0: return null;
        case 1: return this.getFirstChild().toJson(literalsOrder);
        default:
            List<String> newChildren = new ArrayList<String>();

            for(ASTInterface child : this.getChildren()) {
                String aux = child.toJson(null);
                if(aux != null) {
                    newChildren.add(aux);
                }
            }

            return opJson("and", newChildren, literalsOrder);
        }
    }
}
