package com.mhe.dev.compiler.logic.core.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.MheLogger;
import org.junit.jupiter.api.Test;

class LogicCompilerTest
{
    private static final String CODE = ""
            + "A =  cu ->  fc; // Un custom system SIEMPRE es de fecha cerrada\n"
            + "B =  cu -> !md; // Un custom system NUNCA es multidraw\n"
            + "C =  cu -> !dc; // Un custom system NUNCA es un producto destacado\n"
            + "D =  cu -> !dv; // Un custom system NUNCA tiene descuento por volumen\n"
            + "E =  cu -> !ps; // Un custom system SIEMPRE tiene una única participación\n"
            + "F =  cu -> !tk; // Un custom system NUNCA puede ser pagado con tickets\n"
            + "G = !fc -> !md; // Un producto de fecha abierta NUNCA es multidraw\n"
            + "H =  dv -> !pr; // Un producto con descuentos por volumen NUNCA puede tener promociones directas\n"
            + "return [A, B, C, D, E, F, G, H];";

    private static final String JSON = "{\"operator\":\"and\",\"order\":[\"pr\",\"ps\",\"cu\",\"dv\",\"md\",\"tk\",\"fc\",\"dc\"],\"children\":[{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"literal\",\"literal\":\"fc\"}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"md\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"dc\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"dv\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"ps\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"cu\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"tk\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"fc\"}]}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"md\"}]}]},{\"operator\":\"or\",\"children\":[{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"dv\"}]},{\"operator\":\"not\",\"children\":[{\"operator\":\"literal\",\"literal\":\"pr\"}]}]}]}";

    @Test
    public void lottoTest() throws CompilerException
    {
        MheLogger logger = new TestLogger();
        LogicCompiler logicCompiler = new LogicCompiler();

        String jsonString = logicCompiler.expressionToJson(CODE, logger);

        assertEquals(JSON, jsonString);
    }

    static class TestLogger implements MheLogger
    {

        @Override
        public void stream(int row, int col, String message)
        {

        }

        @Override
        public void lexer(int row, int col, String message)
        {
            //System.out.println(" [" + row + ", " + col + "] \t" + message);
        }

        @Override
        public void parser(int row, int col, String message)
        {

        }

        @Override
        public void semantic(int row, int col, String message)
        {

        }

        @Override
        public void warn(int row, int col, String message)
        {

        }

        @Override
        public void error(int row, int col, String message)
        {

        }
    }
}