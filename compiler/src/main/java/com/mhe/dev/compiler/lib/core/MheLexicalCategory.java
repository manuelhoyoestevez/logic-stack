package com.mhe.dev.compiler.lib.core;

/**
 * Categorías léxicas reconocidas.
 *
 * @author Manuel Hoyo Estévez
 */
public enum MheLexicalCategory
{
    /**
     * Carácter filtrable: espacios, tabs, saltos de línea.
     */
    SKIP,
    /**
     * Fin de flujo de entrada: se representa con el símbolo del dólar '$'.
     */
    END,
    /**
     * Identificador: secuencia de caracteres alfanuméricos no reservada.
     */
    IDENTIFIER,
    /**
     * Número entero: secuencia de caracteres numéricos.
     */
    INTEGER,
    /**
     * Número decimal: secuencia de caracteres numéricos con un punto entre ellos.
     */
    DECIMAL,
    /**
     * Carácter: representación de un carácter ASCII encerrado entre comillas simples.
     */
    CHARACTER,
    /**
     * Cadena de caracteres: secuencia de caracteres encerrada entre comillas dobles.
     */
    STRING,
    /**
     * Booleano: las palabras reservadas 'true' o 'false'.
     */
    BOOLEAN,
    /**
     * Palabra reservada 'null'.
     */
    NULL,
    /**
     * Dólar '$'.
     */
    DOLLAR,
    /**
     * Paréntesis abierto '('.
     */
    LPAREN,
    /**
     * Paréntesis cerrado ')'.
     */
    RPAREN,
    /**
     * Corchete abierto '['.
     */
    LCORCH,
    /**
     * Corchete cerrado ']'.
     */
    RCORCH,
    /**
     * Llave abierta '{'.
     */
    LKEY,
    /**
     * Llave cerrada '}'.
     */
    RKEY,
    /**
     * Coma ','.
     */
    COLON,
    /**
     * Punto '.'.
     */
    POINT,
    /**
     * Dos puntos ':'.
     */
    TWOPOINT,
    /**
     * Punto y coma ';'.
     */
    SEMICOLON,
    /**
     * Signo de interrogación '?'.
     */
    HOOK,
    /**
     * Arroba '@'.
     */
    AT,
    /**
     * Signo más '+'.
     */
    PLUS,
    /**
     * Asignación con suma '+='.
     */
    PLUSEQ,
    /**
     * Operador de incremento '++'.
     */
    INC,
    /**
     * Signo menos '-'.
     */
    MINUS,
    /**
     * Asignación con resta '-='.
     */
    MINUSEQ,
    /**
     * Operador de decremento '--'.
     */
    DEC,
    /**
     * Operador de implicación hacia la derecha '->'.
     */
    IMPLRIGHT,
    /**
     * Asterisco '*'.
     */
    STAR,
    /**
     * Barra inclinada '/'.
     */
    DIV,
    /**
     * signo de porcentaje '%'.
     */
    PERCENT,
    /**
     * Signo igual '='.
     */
    EQUAL,
    /**
     * Operador de igualdad '=='.
     */
    EQUALEQ,
    /**
     * Exclamación '!'.
     */
    NOT,
    /**
     * Operador de distinción '!='.
     */
    NOTEQUAL,
    /**
     * Signo mayor '>'.
     */
    BIGGER,
    /**
     * Signo mayor o igual '>='.
     */
    BIGGEREQ,
    /**
     * Operador de desplazamiento hacia la derecha '>>'.
     */
    MOVERIGHT,
    /**
     * Signo menor '<'.
     */
    SMALLER,
    /**
     * Signo menor o igual '<='.
     */
    SMALLEREQ,
    /**
     * Operador de desplazamiento hacia la izquierda '<<'.
     */
    MOVELEFT,
    /**
     * Operador DISTINTO '<>'.
     */
    DISTINCT,
    /**
     * Barra vertical '|'.
     */
    BAR,
    /**
     * Operador de disyunción OR lógico '||'.
     */
    ORLOG,
    /**
     * Ampersand '&'.
     */
    AMPERSAND,
    /**
     * Operador de conjunción AND lógico '&&'.
     */
    ANDLOG
}
