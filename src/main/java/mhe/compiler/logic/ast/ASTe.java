package mhe.compiler.logic.ast;

import mhe.compiler.ASTInterface;

public class ASTe extends AST {

    public ASTe() {
        super(EQLOGI, true, null);
    }

    public ASTe(ASTInterface c, ASTInterface e) {
        this();
        this.getChildren().add(c);

        if(!e.isLambda()) {
            this.getChildren().add(e);
        }
    }

    @Override
    public String getShape() {
        return "\"rectangle\"";
    }

    @Override
    public String getLabel() {
        return "\"ASTe <>\"";
    }

    @Override
    public String getColor() {
        return "\"green\"";
    }

    @Override
    public String toJson() {
        ASTInterface a = this.getFirstChild();
        ASTInterface b = this.getSecondChild();

        if(a == null) {
            return null;
        }
        else if(b == null) {
            return a.toJson();
        }
        else {
            String A = a.toJson();
            String B = b.toJson();
            String notA = notJson(A);
            String notB = notJson(B);
            String AornotB = orJson(A, notB);
            String notAorB = orJson(notA, B);
            return andJson(AornotB, notAorB);
        }
    }
}
