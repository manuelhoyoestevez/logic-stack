package com.mhe.dev.logic.stack.infrastructure.rest.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhe.dev.logic.stack.core.compiler.CompilerInterface;
import com.mhe.dev.logic.stack.core.compiler.MheCompiler;
import com.mhe.dev.logic.stack.core.logic.LogicConverter;
import com.mhe.dev.logic.stack.core.logic.LogicConverterImpl;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper.DecisionTreeMapper;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper.ExpressionTreeMapper;
import com.mhe.dev.logic.stack.infrastructure.rest.spring.mapper.TruthTableMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Application Config Module.
 */
@Configuration
@ComponentScan({"com.mhe.dev.logic.stack"})
public class ApplicationConfig
{
    @Bean
    CompilerInterface compilerInterface()
    {
        return new MheCompiler();
    }

    @Bean
    ExpressionTreeMapper expressionTreeMapper(ObjectMapper objectMapper)
    {
        return new ExpressionTreeMapper(objectMapper);
    }

    @Bean
    TruthTableMapper truthTableMapper()
    {
        return new TruthTableMapper();
    }

    @Bean
    DecisionTreeMapper decisionTreeMapper()
    {
       return new DecisionTreeMapper();
    }

    @Bean
    LogicConverter logicConverter()
    {
        return new LogicConverterImpl();
    }
}
