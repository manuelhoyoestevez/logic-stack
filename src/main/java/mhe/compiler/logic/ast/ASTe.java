package mhe.compiler.logic.ast;

import org.json.simple.JSONObject;

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
    public JSONObject toJson() {
        ASTInterface a = this.getFirstChild();
        ASTInterface b = this.getSecondChild();

        if(a == null) {
            return null;
        }
        else if(b == null) {
            return a.toJson();
        }
        else {
            JSONObject A = a.toJson();
            JSONObject B = b.toJson();
            JSONObject notA = notJson(A);
            JSONObject notB = notJson(B);
            JSONObject AornotB = orJson(A, notB);
            JSONObject notAorB = orJson(notA, B);
            return andJson(AornotB, notAorB);
        }
    }
}
