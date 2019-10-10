package mhe.compiler.logic.ast;

import io.vertx.core.json.JsonObject;
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
    public JsonObject toJson() {
        ASTInterface a = this.getFirstChild();
        ASTInterface b = this.getSecondChild();

        if(a == null) {
            return null;
        }
        else if(b == null) {
            return a.toJson();
        }
        else {
            JsonObject A = a.toJson();
            JsonObject B = b.toJson();
            JsonObject notA = notJson(A);
            JsonObject notB = notJson(B);
            JsonObject AornotB = orJson(A, notB);
            JsonObject notAorB = orJson(notA, B);
            return andJson(AornotB, notAorB);
        }
    }
}
