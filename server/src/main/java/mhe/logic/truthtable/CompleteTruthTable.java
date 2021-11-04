package mhe.logic.truthtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mhe.logic.TruthTable;

/**
 * CompleteTruthTable.
 */
public class CompleteTruthTable extends AbstractTruthTable {
    private final Map<Integer, Boolean> values;

    /**
     * Constructor.
     *
     * @param literals List of literals.
     * @param values List of values
     */
    public CompleteTruthTable(List<String> literals, List<Boolean> values) {
        super(literals);
        this.values = new ArrayMap(values);

        for (int i = 0; i < values.size(); i++) {
            addValue(position2map(i, getReversedLiterals()), values.get(i));
        }

        setBranchLiteral();
    }

    @Override
    public Map<Integer, Boolean> getValues() {
        return values;
    }

    @Override
    public TruthTable reduceBy(Map<String, Boolean> values) {
        Set<String> removedLiterals = values.keySet();
        List<String> newLiterals = new ArrayList<>();
        List<Boolean> newValues = new ArrayList<>();

        for (String lit : getLiterals()) {
            if (!removedLiterals.contains(lit)) {
                newLiterals.add(lit);
            }
        }

        for (int i = 0; i < getValues().size(); i++) {
            Map<String, Boolean> auxValues = position2map(i, getReversedLiterals());

            if (subset(auxValues, values)) {
                newValues.add(getValues().get(i));
            }
        }

        return new CompleteTruthTable(newLiterals, newValues);
    }
}
