package mhe.logic;

import java.util.List;

/**
 * LogicFunction.
 */
public interface LogicFunction {
    List<String> getLiterals();

    String toJsonString();
}
