package mhe.compiler.logic.ast;

import io.vertx.core.json.JsonObject;
import mhe.compiler.ASTInterface;

public class ASTp extends AST {

    public ASTp() {
        super(CODELOGI,true,null);
    }

    public ASTp(ASTInterface s, ASTInterface p) {
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

    public ASTInterface cloneEmpty() {
        return new ASTp();
    }

    @Override
    public JsonObject toJson() {
        for(ASTInterface child : this.getChildren()) {
            if(child.getType() == RETURNLOGI) {
                return child.toJson();
            }
        }
        return null;
    }
}
