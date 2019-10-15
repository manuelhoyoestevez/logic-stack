package mhe.compiler.logic.ast;

import java.util.List;

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
    public String toJson(List<String> literalsOrder) {
        ASTInterface a = this.getFirstChild();
        ASTInterface b = this.getSecondChild();

        if(a == null) {
            return null;
        }
        else if(b == null) {
            return a.toJson(literalsOrder);
        }
        else {
            String A = a.toJson(literalsOrder);
            String B = b.toJson(literalsOrder);
            String notA = notJson(A, literalsOrder);
            String notB = notJson(B, literalsOrder);
            String AornotB = orJson(A, notB, literalsOrder);
            String notAorB = orJson(notA, B, literalsOrder);
            return andJson(AornotB, notAorB, literalsOrder);
        }
    }
}
