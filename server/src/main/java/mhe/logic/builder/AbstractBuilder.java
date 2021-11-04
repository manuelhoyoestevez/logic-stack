package mhe.logic.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import mhe.logic.Builder;
import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.ExpressionTreeType;
import mhe.logic.TruthTable;
import mhe.logic.decisiontree.AbstractDecisionTree;
import mhe.logic.exception.InvalidDecisionTreeParameterException;
import mhe.logic.exception.InvalidExpressionTreeOperatorException;
import mhe.logic.exception.JsonParseException;
import mhe.logic.exception.TooManyLiteralsException;
import mhe.logic.expressiontree.AbstractExpressionTree;
import mhe.logic.truthtable.CompleteTruthTable;
import mhe.logic.truthtable.IncompleteTruthTable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * AbstractBuilder.
 */
public class AbstractBuilder implements Builder {

    public static JSONParser jsonParser = new JSONParser();

    protected static JSONObject parse(String jsonString) throws JsonParseException {
        try {
            return (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException ex) {
            throw new JsonParseException(ex);
        }
    }

    private static String getString(Object str) {
        return str == null ? null : str.toString();
    }

    private static Double getDouble(Object dbl) {
        return dbl == null ? null : Double.parseDouble(dbl.toString());
    }

    @Override
    public TruthTable fromJsonToTruthTable(String jsonString)
            throws JsonParseException {
        JSONObject json = parse(jsonString);

        JSONArray literals = (JSONArray) json.get("literals");
        JSONObject values = (JSONObject) json.get("values");

        HashMap<Integer, Boolean> valuesMap = new HashMap<>();

        Set<Entry<String, Boolean>> entrySet = values.entrySet();

        for (Entry<String, Boolean> entry : entrySet) {
            valuesMap.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }

        return new IncompleteTruthTable(literals, valuesMap);
    }

    @Override
    public DecisionTree fromJsonToDecisionTree(String jsonString)
            throws JsonParseException, InvalidDecisionTreeParameterException {
        switch (jsonString) {
            case "false":
                return new AbstractDecisionTree(new ArrayList<>(), null, 0.0, 0.0, null, null);
            case "true":
                return new AbstractDecisionTree(new ArrayList<>(), null, 1.0, 0.0, null, null);
            default:
                JSONObject json = parse(jsonString);

                String literal = getString(json.get("literal"));

                if (literal == null) {
                    throw new InvalidDecisionTreeParameterException("literal", null);
                }

                Double average = getDouble(json.get("average"));

                if (average == null || average < 0.0 || average > 1.0) {
                    throw new InvalidDecisionTreeParameterException("average", average);
                }

                Double entropy = getDouble(json.get("entropy"));

                if (entropy == null || entropy < 0.0 || entropy > 1.0) {
                    throw new InvalidDecisionTreeParameterException("entropy", entropy);
                }

                ArrayList<String> literals = new ArrayList<>();

                literals.add(literal);

                String falseString = getString(json.get("false"));
                DecisionTree zero = fromJsonToDecisionTree(falseString);

                for (String lit : zero.getLiterals()) {
                    if (!literals.contains(lit)) {
                        literals.add(lit);
                    }
                }

                String trueString = getString(json.get("true"));
                DecisionTree one = fromJsonToDecisionTree(trueString);

                for (String lit : one.getLiterals()) {
                    if (!literals.contains(lit)) {
                        literals.add(lit);
                    }
                }

                return new AbstractDecisionTree(literals, literal, average, entropy, zero, one);
        }
    }

    @Override
    public ExpressionTree fromJsonToExpressionTree(String jsonString)
            throws JsonParseException, InvalidExpressionTreeOperatorException {
        JSONObject json = parse(jsonString);

        String operator = (String) json.get("operator");

        if (operator == null) {
            throw new InvalidExpressionTreeOperatorException();
        }

        operator = operator.toLowerCase();

        JSONArray currentChildren = (JSONArray) json.get("children");
        TreeSet<ExpressionTree> newChildren = new TreeSet<>();

        if (currentChildren != null) {
            for (Object child : currentChildren) {
                newChildren.add(fromJsonToExpressionTree(child.toString()));
            }
        }

        List<String> weights = new ArrayList<>();
        JSONArray order = (JSONArray) json.get("order");

        if (order != null) {
            for (Object o : order) {
                weights.add(o.toString());
            }
        }

        switch (operator) {
            case "literal":
                return new AbstractExpressionTree(ExpressionTreeType.LITERAL, true, (String) json.get("literal"),
                        newChildren, weights);
            case "not":
                return new AbstractExpressionTree(ExpressionTreeType.NOT, false, null, newChildren, weights);
            case "and":
                return new AbstractExpressionTree(ExpressionTreeType.OPERATOR, true, null, newChildren, weights);
            case "or":
                return new AbstractExpressionTree(ExpressionTreeType.OPERATOR, false, null, newChildren, weights);
            default:
                throw new InvalidExpressionTreeOperatorException(operator);
        }
    }

    @Override
    public TruthTable fromExpressionTreeToTruthTable(ExpressionTree expressionTree) throws TooManyLiteralsException {
        List<Boolean> values = new ArrayList<>();
        List<String> literals = expressionTree.getLiterals();

        int n = literals.size();

        if (n > 12) {
            throw new TooManyLiteralsException(n, 12);
        }

        int r = 1 << n; // 2^n

        for (int i = 0; i < r; i++) {
            int check = r;
            HashMap<String, Boolean> row = new HashMap<>();

            for (String literal : literals) {
                check = check >> 1;
                row.put(literal, (i & check) == check);
            }

            values.add(expressionTree.reduceBy(row).getMode());
        }

        return new CompleteTruthTable(literals, values);
    }

    @Override
    public DecisionTree fromTruthTableToDecisionTree(TruthTable truthTable, Boolean maximize) {
        String literal = maximize ? truthTable.getMaxLiteral() : truthTable.getLiteral();
        return truthTable.isLeaf()
                ? new AbstractDecisionTree(new ArrayList<>(), null, truthTable.getAverage(),
                truthTable.getEntropy(), null, null)
                : new AbstractDecisionTree(truthTable.getLiterals(), literal, truthTable.getAverage(),
                truthTable.getEntropy(),
                fromTruthTableToDecisionTree(truthTable.reduceBy(literal, false), maximize),
                fromTruthTableToDecisionTree(truthTable.reduceBy(literal, true), maximize));
    }

    @Override
    public ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree decisionTree) {
        switch (decisionTree.getType()) {
            case LEAF:
                return new AbstractExpressionTree(ExpressionTreeType.OPERATOR, decisionTree.getMode(), null,
                        new TreeSet<>(), new ArrayList<>());

            case LITERAL:
                return new AbstractExpressionTree(ExpressionTreeType.LITERAL, decisionTree.getMode(),
                        decisionTree.getLiteral(), new TreeSet<>(), new ArrayList<>());

            case LATERAL_1:
                TreeSet<ExpressionTree> chr = new TreeSet<>();

                chr.add(new AbstractExpressionTree(ExpressionTreeType.LITERAL, decisionTree.getMode(),
                        decisionTree.getLiteral(), new TreeSet<>(), new ArrayList<>()));

                chr.add(fromDecisionTreeToExpressionTree(decisionTree.getSubDecisionTree(!decisionTree.getMode())));

                return new AbstractExpressionTree(ExpressionTreeType.OPERATOR, false, null, chr,
                        new ArrayList<>());

            default:
                TreeSet<ExpressionTree> children1 = new TreeSet<>();

                children1.add(new AbstractExpressionTree(ExpressionTreeType.LITERAL, false,
                        decisionTree.getLiteral(), new TreeSet<>(), new ArrayList<>()));

                children1.add(fromDecisionTreeToExpressionTree(decisionTree.getSubDecisionTree(false)));

                TreeSet<ExpressionTree> children2 = new TreeSet<>();

                children2.add(new AbstractExpressionTree(ExpressionTreeType.LITERAL, true,
                        decisionTree.getLiteral(), new TreeSet<>(), new ArrayList<>()));

                children2.add(fromDecisionTreeToExpressionTree(decisionTree.getSubDecisionTree(true)));

                TreeSet<ExpressionTree> children = new TreeSet<>();

                children.add(new AbstractExpressionTree(ExpressionTreeType.OPERATOR, true, null, children1,
                        new ArrayList<>()));

                children.add(new AbstractExpressionTree(ExpressionTreeType.OPERATOR, true, null, children2,
                        new ArrayList<>()));

                return new AbstractExpressionTree(ExpressionTreeType.OPERATOR, false, null, children,
                        new ArrayList<>());
        }
    }
}
