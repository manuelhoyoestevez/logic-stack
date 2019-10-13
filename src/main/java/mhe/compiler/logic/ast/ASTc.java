package mhe.compiler.logic.ast;

import mhe.compiler.ASTInterface;

public class ASTc extends AST {

    public ASTc() {
        super(CONDLOGI, true, null);
    }

    public ASTc(ASTInterface a, ASTInterface c) {
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
    public String toJson() {
        ASTInterface first = this.getFirstChild();
        ASTInterface second = this.getSecondChild();

        if(first == null) {
            return null;
        }
        else if(second == null) {
            return first.toJson();
        }
        else {
            return orJson(notJson(first.toJson()), second.toJson());
        }
    }
}
