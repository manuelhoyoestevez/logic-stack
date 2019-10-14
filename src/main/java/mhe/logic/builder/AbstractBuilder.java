package mhe.logic.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
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
import mhe.logic.exception.InvalidDecisionTreeParameterException;
import mhe.logic.exception.InvalidExpressionTreeOperatorException;
import mhe.logic.exception.InvalidTruthTableLiteralsException;
import mhe.logic.exception.InvalidTruthTableValuesException;
import mhe.logic.exception.JsonParseException;
import mhe.logic.exception.TooManyLiteralsException;
import mhe.logic.expressiontree.AbstractExpressionTree;
import mhe.logic.truthtable.CompleteTruthTable;
import mhe.logic.truthtable.IncompleteTruthTable;

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
    @SuppressWarnings("unchecked")
    public TruthTable fromJsonToTruthTable(String jsonString) throws JsonParseException, InvalidTruthTableLiteralsException, InvalidTruthTableValuesException {
        JSONObject json = parse(jsonString);

        JSONArray literals = (JSONArray) json.get("literals");
        JSONObject values = (JSONObject) json.get("values");

        HashMap<Integer, Boolean> valuesMap = new HashMap<Integer,Boolean>();

        Set<Entry<String, Boolean>> entryset = values.entrySet();

        for(Entry<String, Boolean> entry : entryset) {
            valuesMap.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }

        return new IncompleteTruthTable(literals, valuesMap);
    }

    @Override
    public DecisionTree fromJsonToDecisionTree(String jsonString) throws JsonParseException, InvalidDecisionTreeParameterException {
        switch(jsonString) {
            case "false":
                return new AbstractDecisionTree(new ArrayList<String>(), null, 0.0, 0.0, null, null);
            case "true":
                return new AbstractDecisionTree(new ArrayList<String>(), null, 1.0, 0.0, null, null);
            default:
                JSONObject json = parse(jsonString);

                String literal = (String) json.get("literal");

                if(literal == null) {
                    throw new InvalidDecisionTreeParameterException("literal", literal);
                }

                Double average = (Double) json.get("average");

                if(average == null || average < 0.0 || average > 1.0) {
                    throw new InvalidDecisionTreeParameterException("average", average);
                }

                Double entropy = (Double) json.get("entropy");

                if(entropy == null || entropy < 0.0 || entropy > 1.0) {
                    throw new InvalidDecisionTreeParameterException("entropy", entropy);
                }

                ArrayList<String> literals = new ArrayList<String>();

                if(literal != null) {
                    literals.add(literal);
                }

                String falseString = json.get("false").toString();
                DecisionTree zero = this.fromJsonToDecisionTree(falseString);

                for(String lit : zero.getLiterals()) {
                    if(!literals.contains(lit)) {
                        literals.add(lit);
                    }
                }

                String trueString = json.get("true").toString();
                DecisionTree one = this.fromJsonToDecisionTree(trueString);

                for(String lit : one.getLiterals()) {
                    if(!literals.contains(lit)) {
                        literals.add(lit);
                    }
                }

                return new AbstractDecisionTree(literals, literal, average, entropy, zero, one);
        }
    }

    @Override
    public ExpressionTree fromJsonToExpressionTree(String jsonString) throws JsonParseException, InvalidExpressionTreeOperatorException {
        JSONObject json = parse(jsonString);

        String operator = (String) json.get("operator");

        if(operator == null) {
            throw new InvalidExpressionTreeOperatorException();
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
            default:        throw  new InvalidExpressionTreeOperatorException(operator);
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
