package com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.dev.logic.stack.core.model.LogicExpressionTree;
import com.mhe.dev.logic.stack.core.model.LogicExpressionType;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.LogicExpressionTreeDto;
import java.util.List;
import java.util.stream.Collectors;

public class LogicExpressionTreeMapper
{
    private final ObjectMapper objectMapper;

    public LogicExpressionTreeMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public LogicExpressionTreeDto jsonToLogicExpressionTreeDto(String json) throws JsonProcessingException
    {
        return objectMapper.readValue(json, LogicExpressionTreeDto.class);
    }

    public LogicExpressionTreeDto toDto(LogicExpressionTree entity)
    {
        if (entity == null)
        {
            return null;
        }

        return new LogicExpressionTreeDto()
            .operator(LogicExpressionTreeDto.OperatorEnum.fromValue(entity.getOperator().getOperator()))
            .literal(entity.getLiteral())
            .order(entity.getOrder())
            .children(entity.getChildren().stream().map(this::toDto).collect(Collectors.toList()));
    }

    public LogicExpressionTree fromDto(LogicExpressionTreeDto dto)
    {
        if (dto == null)
        {
            return null;
        }

        List<LogicExpressionTreeDto> children = dto.getChildren();

        return new LogicExpressionTree(
            LogicExpressionType.fromOperator(dto.getOperator().getValue()),
            dto.getLiteral(),
            dto.getOrder(),
            children == null ? null : children.stream().map(this::fromDto).collect(Collectors.toList())
        );
    }
}
