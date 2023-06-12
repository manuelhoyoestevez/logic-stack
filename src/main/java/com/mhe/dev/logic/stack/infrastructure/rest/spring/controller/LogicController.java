package com.mhe.dev.logic.stack.infrastructure.rest.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mhe.dev.logic.stack.core.compiler.CompilerInterface;
import com.mhe.dev.logic.stack.core.compiler.exception.CompilerException;
import com.mhe.dev.logic.stack.core.logic.LogicConverter;
import com.mhe.dev.logic.stack.core.logic.model.DecisionTree;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTree;
import com.mhe.dev.logic.stack.core.logic.model.TruthTable;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.api.LogicApi;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.DecisionTreeDto;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.ExpressionDto;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.ExpressionTreeDto;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.TruthTableDto;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper.LogicMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * LogicController.
 */
@RestController
public class LogicController implements LogicApi
{
    private final CompilerInterface compiler;
    private final LogicMapper logicMapper;
    private final LogicConverter logicConverter;

    /**
     * Constructor.
     *
     * @param compiler CompilerInterface
     * @param logicMapper LogicMapper
     * @param logicConverter LogicConverter
     */
    public LogicController(
        CompilerInterface compiler,
        LogicMapper logicMapper,
        LogicConverter logicConverter
    )
    {
        this.compiler = compiler;
        this.logicMapper = logicMapper;
        this.logicConverter = logicConverter;
    }

    @Override
    public ResponseEntity<ExpressionTreeDto> parseExpression(ExpressionDto expressionDto)
    {
        try
        {
            String json = compiler.expressionToJson(expressionDto.getExpression());

            ExpressionTreeDto dto = logicMapper.jsonToExpressionTreeDto(json);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (CompilerException | JsonProcessingException exception)
        {
            throw RestExceptionHandler.apiException(HttpStatus.BAD_REQUEST, exception);
        }
    }

    @Override
    public ResponseEntity<TruthTableDto> expressionToTruthTable(ExpressionDto expressionDto)
    {
        try
        {
            String json = compiler.expressionToJson(expressionDto.getExpression());

            ExpressionTreeDto dto = logicMapper.jsonToExpressionTreeDto(json);

            ExpressionTree expressionTree = logicMapper.fromExpressionTreeDto(dto);

            TruthTable truthTable = logicConverter.fromExpressionTreeToTruthTable(expressionTree);

            return new ResponseEntity<>(logicMapper.toTruthTableDto(truthTable), HttpStatus.OK);
        } catch (CompilerException | JsonProcessingException exception)
        {
            throw RestExceptionHandler.apiException(HttpStatus.BAD_REQUEST, exception);
        }
    }

    @Override
    public ResponseEntity<DecisionTreeDto> truthTableToDecisionTree(TruthTableDto body, Boolean max)
    {
        TruthTable truthTable = logicMapper.fromTruthTableDto(body);

        DecisionTree decisionTree = logicConverter.fromTruthTableToDecisionTree(truthTable, max);

        return new ResponseEntity<>(logicMapper.toDecisionTreeDto(decisionTree), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DecisionTreeDto> expressionToDecisionTree(ExpressionDto expressionDto, Boolean max)
    {
        try
        {
            String json = compiler.expressionToJson(expressionDto.getExpression());

            ExpressionTreeDto dto = logicMapper.jsonToExpressionTreeDto(json);

            ExpressionTree expressionTree = logicMapper.fromExpressionTreeDto(dto);

            TruthTable truthTable = logicConverter.fromExpressionTreeToTruthTable(expressionTree);

            DecisionTree decisionTree = logicConverter.fromTruthTableToDecisionTree(truthTable, max);

            return new ResponseEntity<>(logicMapper.toDecisionTreeDto(decisionTree), HttpStatus.OK);
        } catch (CompilerException | JsonProcessingException exception)
        {
            throw RestExceptionHandler.apiException(HttpStatus.BAD_REQUEST, exception);
        }
    }
}
