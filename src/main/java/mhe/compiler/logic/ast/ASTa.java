package mhe.compiler.logic.ast;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.json.JsonObject;
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

            return opJson("and", newChildren);
        }
    }
}
