package mhe.service;

import io.vertx.core.json.JsonObject;
import mhe.LogicService;
import mhe.graphviz.GraphViz;
import mhe.logic.Builder;
import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.TruthTable;
import mhe.logic.exception.InvalidDecisionTreeParameterException;
import mhe.logic.exception.InvalidExpressionTreeOperatorException;
import mhe.logic.exception.InvalidTruthTableLiteralsException;
import mhe.logic.exception.InvalidTruthTableValuesException;
import mhe.logic.exception.JsonParseException;
import mhe.logic.exception.TooManyLiteralsException;

public class AbstractLogicService implements LogicService {
    private Builder builder;

    public AbstractLogicService(Builder builder) {
        this.builder = builder;
    }

    @Override
    public JsonObject fromExpressionTreeToTruthTable(JsonObject payload) throws JsonParseException, InvalidExpressionTreeOperatorException, TooManyLiteralsException {
        ExpressionTree expressionTree = this.builder.fromJsonToExpressionTree(payload.getJsonObject("expressionTree").toString());
        TruthTable truthTable = this.builder.fromExpressionTreeToTruthTable(expressionTree);

        return new JsonObject()
        .put("truthTable", new JsonObject(truthTable.toJsonString()))
        .put("expressionTreeGraph", GraphViz.drawTree(expressionTree, "expressionTree"))
        .put("reducedExpressionTreeGraph", GraphViz.drawTree(expressionTree.reduce(), "reducedExpressionTree"));
    }

    @Override
    public JsonObject fromTruthTableToDecisionTree(JsonObject payload) throws JsonParseException, InvalidTruthTableLiteralsException, InvalidTruthTableValuesException {
        TruthTable truthTable = this.builder.fromJsonToTruthTable(payload.getJsonObject("truthTable").toString());
        DecisionTree decisionTree = this.builder.fromTruthTableToDecisionTree(truthTable, payload.getBoolean("maximize", false));

        JsonObject ret = new JsonObject();
        String jsonString = decisionTree.toJsonString();

        switch(jsonString) {
            case "false": ret.put("decisionTree", false); break;
            case "true":  ret.put("decisionTree", true);  break;
            default:      ret.put("decisionTree", new JsonObject(jsonString));
        }

        ret.put("decisionTreeGraph", GraphViz.drawTree(decisionTree, "expressionTree"));

        return ret;
    }

    @Override
    public JsonObject fromDecisionTreeToExpressionTree(JsonObject payload) throws JsonParseException, InvalidDecisionTreeParameterException {
        Object rawDecisionTree = payload.getValue("decisionTree").toString();
        DecisionTree decisionTree = this.builder.fromJsonToDecisionTree(rawDecisionTree.toString());
        ExpressionTree expressionTree = this.builder.fromDecisionTreeToExpressionTree(decisionTree).reduce();

        return new JsonObject()
        .put("logicExpression", expressionTree.getExpression())
        .put("expressionTree", new JsonObject(expressionTree.toJsonString()))
        .put("expressionTreeGraph", GraphViz.drawTree(expressionTree, "expressionTree"));
    }

    @Override
    public JsonObject fromDecisionTreeToTruthTable(JsonObject payload) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonObject fromTruthTableToExpressionTree(JsonObject payload) {
        // TODO Auto-generated method stub
        return null;
    }
}
