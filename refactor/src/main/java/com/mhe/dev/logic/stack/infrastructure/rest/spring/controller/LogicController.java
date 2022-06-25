package com.mhe.dev.logic.stack.infrastructure.rest.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mhe.dev.logic.stack.core.compiler.CompilerInterface;
import com.mhe.dev.logic.stack.core.compiler.exception.CompilerException;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.api.LogicApi;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.LogicExpressionTreeDto;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper.LogicExpressionTreeMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogicController implements LogicApi
{
    private final CompilerInterface compiler;
    private final LogicExpressionTreeMapper logicExpressionTreeMapper;

    public LogicController(
        CompilerInterface compiler,
        LogicExpressionTreeMapper logicExpressionTreeMapper
    )
    {
        this.compiler = compiler;
        this.logicExpressionTreeMapper = logicExpressionTreeMapper;
    }

    @Override
    public ResponseEntity<LogicExpressionTreeDto> parseLogicExpression(String body) {
        try
        {
            String json = compiler.expressionToJson(body);

            LogicExpressionTreeDto dto = logicExpressionTreeMapper.jsonToLogicExpressionTreeDto(json);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (CompilerException | JsonProcessingException exception)
        {
            throw RestExceptionHandler.apiException(HttpStatus.BAD_REQUEST, exception);
        }
    }
}
