package mhe.compiler.logic.ast;

import io.vertx.core.json.JsonObject;
import mhe.compiler.ASTInterface;

public class ASTn extends AST {

    public ASTn() {
        super(NOTLOGI, true, null);
    }

    public ASTn(ASTInterface l) {
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
    public JsonObject toJson() {
        ASTInterface first = this.getFirstChild();
        if(first == null) {
            return null;
        }
        else {
            return notJson(first.toJson());
        }
    }
}
