package mhe.logic.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import mhe.logic.Builder;
import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.ExpressionTreeType;
import mhe.logic.TruthTable;
import mhe.logic.decisiontree.AbstractDecisionTree;
import mhe.logic.exception.InvalidTreeExpressionOperator;
import mhe.logic.exception.InvalidTruthTableLiteralsException;
import mhe.logic.exception.InvalidTruthTableValuesException;
import mhe.logic.exception.TooManyLiteralsException;
import mhe.logic.expressiontree.AbstractExpressionTree;
import mhe.logic.truthtable.CompleteTruthTable;

public class AbstractBuilder implements Builder {

    @Override
    public TruthTable fromJsonToTruthTable(JSONObject json) throws InvalidTruthTableLiteralsException, InvalidTruthTableValuesException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DecisionTree fromJsonToDecisionTree(JSONObject json) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExpressionTree fromJsonToExpressionTree(JSONObject json) throws InvalidTreeExpressionOperator {
        String operator = (String) json.get("operator");

        if(operator == null) {
            throw new InvalidTreeExpressionOperator();
        }

        operator = operator.toLowerCase();

        JSONArray currentChildren = (JSONArray) json.get("children");
        TreeSet<ExpressionTree> newChildren = new TreeSet<ExpressionTree>();

        if(currentChildren != null) {
            for(Object child : currentChildren) {
                newChildren.add(this.fromJsonToExpressionTree((JSONObject) child));
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

        int n = expressionTree.getLiterals().size();

        if(n > 30) {
            throw new TooManyLiteralsException(n, 30);
        }

        int r = 1 << n; // 2^n

        LinkedList<String> reversed = new LinkedList<String>();

        for(String literal : expressionTree.getLiterals()) {
            reversed.addFirst(literal);
        }

        for(int i = 0; i < r; i++) {
            int raw = i;
            HashMap<String, Boolean> row = new HashMap<String, Boolean>();

            for(String literal : reversed) {
                row.put(literal, (raw & 1) == 1);
                raw = raw >> 1;
            }

            values.add(expressionTree.reduceBy(row).getMode());
        }

        return new CompleteTruthTable(expressionTree.getLiterals(), values);
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
                    truthTable.getBranchLiteral(),
                    truthTable.getAverage(),
                    truthTable.getEntropy(),
                    this.fromTruthTableToDecisionTree(truthTable.reduceBy(truthTable.getBranchLiteral(), false)),
                    this.fromTruthTableToDecisionTree(truthTable.reduceBy(truthTable.getBranchLiteral(),  true))
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
