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
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.ExpressionTreeDto;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.dto.TruthTableDto;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper.DecisionTreeMapper;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper.ExpressionTreeMapper;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper.TruthTableMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogicController implements LogicApi
{
    private final CompilerInterface compiler;
    private final ExpressionTreeMapper expressionTreeMapper;
    private final TruthTableMapper truthTableMapper;
    private final DecisionTreeMapper decisionTreeMapper;
    private final LogicConverter logicConverter;

    public LogicController(
        CompilerInterface compiler,
        ExpressionTreeMapper expressionTreeMapper,
        TruthTableMapper truthTableMapper,
        DecisionTreeMapper decisionTreeMapper,
        LogicConverter logicConverter
    )
    {
        this.compiler = compiler;
        this.expressionTreeMapper = expressionTreeMapper;
        this.truthTableMapper = truthTableMapper;
        this.decisionTreeMapper = decisionTreeMapper;
        this.logicConverter = logicConverter;
    }

    @Override
    public ResponseEntity<ExpressionTreeDto> parseExpression(String expression) {
        try
        {
            String json = compiler.expressionToJson(expression);

            ExpressionTreeDto dto = expressionTreeMapper.jsonToLogicExpressionTreeDto(json);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (CompilerException | JsonProcessingException exception)
        {
            throw RestExceptionHandler.apiException(HttpStatus.BAD_REQUEST, exception);
        }
    }

    @Override
    public ResponseEntity<TruthTableDto> expressionToTruthTable(String expression) {
        try
        {
            String json = compiler.expressionToJson(expression);

            ExpressionTreeDto dto = expressionTreeMapper.jsonToLogicExpressionTreeDto(json);

            ExpressionTree expressionTree = expressionTreeMapper.fromDto(dto);

            TruthTable truthTable = logicConverter.fromExpressionTreeToTruthTable(expressionTree);

            return new ResponseEntity<>(truthTableMapper.toDto(truthTable), HttpStatus.OK);
        } catch (CompilerException | JsonProcessingException exception)
        {
            throw RestExceptionHandler.apiException(HttpStatus.BAD_REQUEST, exception);
        }
    }

    @Override
    public ResponseEntity<DecisionTreeDto> truthTableToDecisionTree(TruthTableDto body, Boolean max)
    {
        TruthTable truthTable = truthTableMapper.fromDto(body);

        DecisionTree decisionTree = logicConverter.fromTruthTableToDecisionTree(truthTable, max);

        return new ResponseEntity<>(decisionTreeMapper.toDto(decisionTree), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ExpressionTreeDto> truthTableToExpressionTree(TruthTableDto body, Boolean max) {
        TruthTable truthTable = truthTableMapper.fromDto(body);

        DecisionTree decisionTree = logicConverter.fromTruthTableToDecisionTree(truthTable, max);

        ExpressionTree expressionTree = logicConverter.fromDecisionTreeToExpressionTree(decisionTree);

        return new ResponseEntity<>(expressionTreeMapper.toDto(expressionTree), HttpStatus.OK);
    }
}
