package mhe.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.vertx.core.json.JsonObject;
import mhe.LogicService;
import mhe.graphviz.GraphViz;
import mhe.logic.Builder;
import mhe.logic.ExpressionTree;
import mhe.logic.TruthTable;
import mhe.logic.exception.InvalidTreeExpressionOperator;
import mhe.logic.exception.JsonParseException;
import mhe.logic.exception.TooManyLiteralsException;

public class AbstractLogicService implements LogicService {
    private static JSONParser jsonParser = new JSONParser();

    private Builder builder;

    public AbstractLogicService(Builder builder) {
        this.builder = builder;
    }

    @Override
    public JsonObject fromExpressionTreeToTruthTable(JsonObject payload) throws JsonParseException, InvalidTreeExpressionOperator, TooManyLiteralsException {
        ExpressionTree expressionTree = this.builder.fromJsonToExpressionTree(payload.toString());
        TruthTable truthTable = this.builder.fromExpressionTreeToTruthTable(expressionTree);

        return new JsonObject()
        .put("truthTable", JSON2json(truthTable.toJson()))
        .put("expressionTreeGraph", GraphViz.drawTree(expressionTree, "expressionTree"))
        .put("reducedExpressionTreeGraph", GraphViz.drawTree(expressionTree.reduce(), "reducedExpressionTree"));
    }

    @Override
    public JsonObject fromTruthTableToDecisionTree(JsonObject payload) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonObject fromDecisionTreeToExpressionTree(JsonObject payload) {
        // TODO Auto-generated method stub
        return null;
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

    public static JSONObject json2JSON(JsonObject payload) {
        try {
            return (JSONObject) jsonParser.parse(payload.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonObject JSON2json(JSONObject payload) {
        return new JsonObject(payload.toString());
    }
}
