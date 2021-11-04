package mhe.compiler.logic.ast;

import java.util.List;
import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.NoLambdaAbstractSyntaxTree;

/**
 * AstAssignment.
 */
public class AstAssignment extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory> {

    private final String id;

    /**
     * Constructor.
     *
     * @param id Variable
     * @param e Valor
     */
    public AstAssignment(String id, AbstractSyntaxTree<LogicSemanticCategory> e) {
        super(LogicSemanticCategory.ASIGLOGI);
        this.id = id;
        this.getChildren().add(e);
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        return null;
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "AstAssignment " + id;
    }

    @Override
    public String getColor() {
        return "orange";
    }
}
