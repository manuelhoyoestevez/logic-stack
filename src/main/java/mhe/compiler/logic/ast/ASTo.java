package mhe.compiler.logic.ast;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.json.JsonObject;
import mhe.compiler.ASTInterface;

public class ASTo extends AST {

    public ASTo() {
        super(ORLOGI, true, null);
    }

    public ASTo(ASTInterface n, ASTInterface o) {
        this();
        this.getChildren().add(n);
        this.getChildren().addAll(o.getChildren());
    }

    @Override
    public String getShape() {
        return quotify("rectangle");
    }

    @Override
    public String getLabel() {
        return quotify("ASTo |");
    }

    @Override
    public String getColor() {
        return quotify("red");
    }

    public ASTInterface cloneEmpty() {
        return new ASTo();
    }

    @Override
    public JsonObject toJson() {
        switch(this.getChildren().size()) {
        case 0: return null;
        case 1: return this.getFirstChild().toJson();
        default:
            List<JsonObject> newChildren = new ArrayList<JsonObject>();

            for(ASTInterface child : this.getChildren()) {
                JsonObject aux = child.toJson();
                if(aux != null) {
                    newChildren.add(aux);
                }
            }

            return opJson("or", newChildren);
        }
    }
}
