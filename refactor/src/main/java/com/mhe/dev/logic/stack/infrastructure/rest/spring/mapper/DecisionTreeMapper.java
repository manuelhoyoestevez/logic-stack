package com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper;

import com.mhe.dev.logic.stack.core.logic.model.DecisionTree;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.DecisionTreeDto;

public class DecisionTreeMapper
{
    public DecisionTreeDto toDto(DecisionTree decisionTree)
    {
        if (decisionTree == null)
        {
            return null;
        }

        return new DecisionTreeDto()
            .type(decisionTree.getType().name())
            .mode(decisionTree.getMode())
            .literal(decisionTree.getLiteral())
            .average(decisionTree.getAverage())
            .entropy(decisionTree.getEntropy())
            .zero(toDto(decisionTree.getSubDecisionTree(false)))
            .one(toDto(decisionTree.getSubDecisionTree(true)));
    }
}
