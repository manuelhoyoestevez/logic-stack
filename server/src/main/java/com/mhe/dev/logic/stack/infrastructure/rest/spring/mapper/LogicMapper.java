package com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.dev.logic.stack.core.logic.model.DecisionTree;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTree;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTreeImpl;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTreeType;
import com.mhe.dev.logic.stack.core.logic.model.TruthTable;
import com.mhe.dev.logic.stack.core.logic.model.TruthTableImpl;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.DecisionTreeDto;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.ExpressionTreeDto;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.TruthTableDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * LogicMapper.
 */
public class LogicMapper
{
    private final ObjectMapper objectMapper;

    public LogicMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public ExpressionTreeDto jsonToExpressionTreeDto(String json) throws JsonProcessingException
    {
        return objectMapper.readValue(json, ExpressionTreeDto.class);
    }

    private ExpressionTreeDto.OperatorEnum toOperatorEnum(ExpressionTreeType type, boolean mode)
    {
        if (type == null)
        {
            return null;
        }

        switch (type)
        {
            case LITERAL:
                return ExpressionTreeDto.OperatorEnum.LITERAL;
            case NOT:
                return ExpressionTreeDto.OperatorEnum.NOT;
            case OPERATOR:
                return mode
                    ? ExpressionTreeDto.OperatorEnum.OR
                    : ExpressionTreeDto.OperatorEnum.AND;
            default: return null;
        }
    }

    /**
     * Map to Dto.
     *
     * @param expressionTree entity
     * @return dto
     */
    public ExpressionTreeDto toExpressionTreeDto(ExpressionTree expressionTree)
    {
        if (expressionTree == null)
        {
            return null;
        }

        return new ExpressionTreeDto()
            .expression(expressionTree.getExpression())
            .operator(toOperatorEnum(expressionTree.getType(), expressionTree.getMode()))
            .literal(expressionTree.getLiteral())
            .order(expressionTree.getWeights())
            .children(
                expressionTree.getChildren().stream().map(this::toExpressionTreeDto).collect(Collectors.toList()));
    }

    private ExpressionTreeType fromOperatorEnumToExpressionTreeType(ExpressionTreeDto.OperatorEnum operatorEnum)
    {
        if (operatorEnum == null)
        {
            return null;
        }

        switch (operatorEnum)
        {
            case AND:
            case OR:
                return ExpressionTreeType.OPERATOR;
            case NOT:
                return ExpressionTreeType.NOT;
            case LITERAL:
                return ExpressionTreeType.LITERAL;
            default:
                return null;
        }
    }

    private boolean fromOperatorEnumToBoolean(ExpressionTreeDto.OperatorEnum operatorEnum)
    {
        if (operatorEnum == null)
        {
            return false;
        }

        return operatorEnum.equals(ExpressionTreeDto.OperatorEnum.AND);
    }

    private SortedSet<ExpressionTree> fromChildrenList(List<ExpressionTreeDto> children)
    {
        if (children == null)
        {
            return new TreeSet<>();
        }

        return children
            .stream()
            .map(this::fromExpressionTreeDto)
            .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Map Dto into entity.
     *
     * @param dto Dto
     * @return Entity
     */
    public ExpressionTree fromExpressionTreeDto(ExpressionTreeDto dto)
    {
        if (dto == null)
        {
            return null;
        }

        List<String> order = dto.getOrder();

        return new ExpressionTreeImpl(
            fromOperatorEnumToExpressionTreeType(dto.getOperator()),
            fromOperatorEnumToBoolean(dto.getOperator()),
            dto.getLiteral(),
            fromChildrenList(dto.getChildren()),
            order == null ? new ArrayList<>() : dto.getOrder()
        );
    }

    /**
     * Map entity into Dto.
     *
     * @param decisionTree Entity
     * @return Dto
     */
    public DecisionTreeDto toDecisionTreeDto(DecisionTree decisionTree)
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
            .expression(decisionTree.getExpression())
            .truthTable(toTruthTableDto(decisionTree.getTruthTable()))
            .zero(toDecisionTreeDto(decisionTree.getSubDecisionTree(false)))
            .one(toDecisionTreeDto(decisionTree.getSubDecisionTree(true)));
    }

    /**
     * Map entity into Dto.
     *
     * @param truthTable Entity
     * @return Dto
     */
    public TruthTableDto toTruthTableDto(TruthTable truthTable)
    {
        if (truthTable == null)
        {
            return null;
        }

        return new TruthTableDto()
            .literals(truthTable.getLiterals())
            .values(truthTable.getValuesAsString());
    }

    private Map<Integer, Boolean> listToMap(String values)
    {
        Map<Integer, Boolean> map = new HashMap<>();

        if (values == null)
        {
            return map;
        }

        for (int i = 0; i < values.length(); i++)
        {
            switch (values.charAt(i))
            {
                case '0':
                    map.put(i, false);
                    break;
                case '1':
                    map.put(i, true);
                    break;
                default:
            }
        }

        return map;
    }

    /**
     * Map Dto into entity.
     *
     * @param dto Dto
     * @return Entity
     */
    public TruthTable fromTruthTableDto(TruthTableDto dto)
    {
        if (dto == null)
        {
            return null;
        }

        return new TruthTableImpl(dto.getLiterals(), listToMap(dto.getValues()));
    }
}
