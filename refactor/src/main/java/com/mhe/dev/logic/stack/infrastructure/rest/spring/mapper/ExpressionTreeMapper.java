package com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTree;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTreeImpl;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTreeType;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.ExpressionTreeDto;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ExpressionTreeMapper
{
    private final ObjectMapper objectMapper;

    public ExpressionTreeMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public ExpressionTreeDto jsonToLogicExpressionTreeDto(String json) throws JsonProcessingException
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
            case LITERAL: return ExpressionTreeDto.OperatorEnum.LITERAL;
            case NOT: return ExpressionTreeDto.OperatorEnum.NOT;
            case OPERATOR: return mode
                ? ExpressionTreeDto.OperatorEnum.OR
                : ExpressionTreeDto.OperatorEnum.AND;
        }

        return null;
    }

    public ExpressionTreeDto toDto(ExpressionTree expressionTree)
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
            .children(expressionTree.getChildren().stream().map(this::toDto).collect(Collectors.toList()));
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
        }

        return null;
    }

    private boolean fromOperatorEnumToBoolean(ExpressionTreeDto.OperatorEnum operatorEnum)
    {
        if (operatorEnum == null)
        {
            return false;
        }

        switch (operatorEnum)
        {
            case AND: return true;
            case OR: return false;
        }

        return false;
    }

    private SortedSet<ExpressionTree> fromChildrenList(List<ExpressionTreeDto> children)
    {
        if (children == null)
        {
            return null;
        }

        return children
            .stream()
            .map(this::fromDto)
            .collect(Collectors.toCollection(TreeSet::new));
    }

    public ExpressionTree fromDto(ExpressionTreeDto dto)
    {
        if (dto == null)
        {
            return null;
        }

        List<ExpressionTreeDto> children = dto.getChildren();
        List<String> order = dto.getOrder();

        return new ExpressionTreeImpl(
            fromOperatorEnumToExpressionTreeType(dto.getOperator()),
            fromOperatorEnumToBoolean(dto.getOperator()),
            dto.getLiteral(),
            fromChildrenList(children == null ? new ArrayList<>() : children),
            order == null ? null : dto.getOrder()
        );
    }
}
