package mhe.logic;

import java.util.List;

public interface LogicFunction {
    List<String> getLiterals();
    String toJsonString();
}
