package mhe.logic.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import mhe.logic.Builder;
import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.ExpressionTreeType;
import mhe.logic.TruthTable;
import mhe.logic.decisiontree.AbstractDecisionTree;
import mhe.logic.exception.InvalidTreeExpressionOperator;
import mhe.logic.exception.InvalidTruthTableLiteralsException;
import mhe.logic.exception.InvalidTruthTableValuesException;
import mhe.logic.exception.JsonParseException;
import mhe.logic.exception.TooManyLiteralsException;
import mhe.logic.expressiontree.AbstractExpressionTree;
import mhe.logic.truthtable.CompleteTruthTable;

public class AbstractBuilder implements Builder {

    public static JSONParser jsonParser = new JSONParser();
    protected static JSONObject parse(String jsonString) throws JsonParseException {
        try {
            return (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException ex) {
            throw new JsonParseException(ex);
        }
    }

    @Override
    public TruthTable fromJsonToTruthTable(String jsonString) throws JsonParseException, InvalidTruthTableLiteralsException, InvalidTruthTableValuesException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DecisionTree fromJsonToDecisionTree(String jsonString) throws JsonParseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExpressionTree fromJsonToExpressionTree(String jsonString) throws JsonParseException, InvalidTreeExpressionOperator {
        JSONObject json = parse(jsonString);

        String operator = (String) json.get("operator");

        if(operator == null) {
            throw new InvalidTreeExpressionOperator();
        }

        operator = operator.toLowerCase();

        JSONArray currentChildren = (JSONArray) json.get("children");
        TreeSet<ExpressionTree> newChildren = new TreeSet<ExpressionTree>();

        if(currentChildren != null) {
            for(Object child : currentChildren) {
                newChildren.add(this.fromJsonToExpressionTree(child.toString()));
            }
        }

        switch(operator) {
            case "literal": return new AbstractExpressionTree(ExpressionTreeType.LITERAL,  true, (String) json.get("literal"), newChildren);
            case "not":     return new AbstractExpressionTree(ExpressionTreeType.NOT,      false, null, newChildren);
            case "and":     return new AbstractExpressionTree(ExpressionTreeType.OPERATOR, true,  null, newChildren);
            case "or":      return new AbstractExpressionTree(ExpressionTreeType.OPERATOR, false, null, newChildren);
            default:        throw  new InvalidTreeExpressionOperator(operator);
        }
    }

    @Override
    public TruthTable fromExpressionTreeToTruthTable(ExpressionTree expressionTree) throws TooManyLiteralsException {
        List<Boolean> values = new ArrayList<Boolean>();
        List<String> literals = expressionTree.getLiterals();

        int n = literals.size();

        if(n > 30) {
            throw new TooManyLiteralsException(n, 30);
        }

        int r = 1 << n; // 2^n

        for(int i = 0; i < r; i++) {
            int check = r;
            HashMap<String, Boolean> row = new HashMap<String, Boolean>();

            for(String literal : literals) {
                check = check >> 1;
                row.put(literal, (i & check) == check);
            }

            values.add(expressionTree.reduceBy(row).getMode());
        }

        return new CompleteTruthTable(literals, values);
    }

    @Override
    public DecisionTree fromTruthTableToDecisionTree(TruthTable truthTable) {
        return truthTable.isLeaf()
            ? new AbstractDecisionTree(
                    new ArrayList<String>(),
                    null,
                    truthTable.getAverage(),
                    truthTable.getEntropy(),
                    null,
                    null
            )
            : new AbstractDecisionTree(
                    truthTable.getLiterals(),
                    truthTable.getLiteral(),
                    truthTable.getAverage(),
                    truthTable.getEntropy(),
                    this.fromTruthTableToDecisionTree(truthTable.reduceBy(truthTable.getLiteral(), false)),
                    this.fromTruthTableToDecisionTree(truthTable.reduceBy(truthTable.getLiteral(),  true))
            );
    }

    @Override
    public ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree expressionTree) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DecisionTree fromTruthTableToExpressionTree(TruthTable expressionTree) {
        // TODO Auto-generated method stub
        return null;
    }
}
