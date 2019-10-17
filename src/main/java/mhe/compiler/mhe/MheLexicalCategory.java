package mhe.compiler.mhe;


/** Categorías léxicas reconocidas
 * @author Manuel Hoyo Estévez
 */
public enum MheLexicalCategory {
    /** */
    ERROR,
    /** */
    SKIP,
    /** Fin de flujo de entrada: se representa con el símbolo del dólar '$' */
    END,
    /** Identificador: secuencia de caracteres alfanumericos no reservada */
    IDENTIFIER,
    /** Número entero: secuencia de caracteres numéricos */
    INTEGER,
    /** Número decimal: secuencia decaracteres numéricos con un punto entre ellos */
    DECIMAL,
    /** Carácter: repersentación de un caracter ASCII encerrado entre comillas simples */
    CHARACTER,
    /** Cadena de caracteres: secuencia de caracteres encerrada entre comillas dobles */
    STRING,
    /** Booleano: las palabras reservadas 'true' o 'false' */
    BOOLEAN,
    /** Palabra reservada 'exit' */
    EXIT,
    /** Palabra reservada 'load' */
    LOAD,
    /** Palabra reservada 'save' */
    SAVE,
    /** Palabra reservada 'show' */
    SHOW,
    /** Palabra reservada 'list' */
    LIST,
    /** Palabra reservada 'test' */
    TEST,
    /** Palabra reservada 'blank' */
    BLANK,
    /** Palabra reservada 'alphabet' */
    ALPHABET,
    /** Palabra reservada 'comment' */
    COMMENT,
    /** Palabra reservada 'token' */
    TOKEN,
    /** Palabra reservada 'return' */
    RETURN,
    /** Palabra reservada 'grammar' */
    GRAMMAR,
    /** Palabra reservada 'semantic' */
    SEMANTIC,
    /** Palabra reservada 'lambda' */
    LAMBDA,
    /** Paréntesis abierto '(' */
    LPAREN,
    /** Paréntesis cerrado ')' */
    RPAREN,
    /** Corchete abierto '[' */
    LCORCH,
    /** Corchete cerrado ']' */
    RCORCH,
    /** Llave abierta '{' */
    LKEY,
    /** Llave cerrada '}' */
    RKEY,
    /** Coma ',' */
    COLON,
    /** Punto '.' */
    POINT,
    /** Dos puntos ':' */
    TWOPOINT,
    /** Punto y coma ';' */
    SEMICOLON,
    /** Signo de interrogación '?' */
    HOOK,
    /** Signo m�s '+' */
    PLUS,
    /** Asignación con suma '+=' */
    PLUSEQ,
    /** Operador de incremento '++' */
    INC,
    /** Signo menos '-' */
    MINUS,
    /** Asignación con resta '-=' */
    MINUSEQ,
    /** Operador de decremento '--' */
    DEC,
    /** Operador de implicacion hacia la derecha'->' */
    IMPLRIGHT,
    /** Asterisco '*' */
    STAR,
    /** Asignación con multiplicacion '*=' */
    STAREQ,
    /** Operador de potencia '**' */
    POW,
    /** Barra inclinada '/' */
    DIV,
    /** Asignacion con division '/=' */
    DIVEQ,
    /** signo de porcentaje '%' */
    PERCENT,
    /** Signo igual '=' */
    EQUAL,
    /** Operador de igualdad '==' */
    EQUALEQ,
    /** Excalamación '!' */
    NOT,
    /** Operador de distincion '!=' */
    NOTEQUAL,
    /** Signo mayor '>' */
    BIGGER,
    /** Signo mayor o igual '>=' */
    BIGGEREQ,
    /** Operador de desplazamiento hacia la derecha '>>' */
    MOVERIGHT,
    /** Signo menor '<' */
    SMALLER,
    /** Signo menor o igual '<=' */
    SMALLEREQ,
    /** Operador de desplazamiento hacia la izquierda '<<' */
    MOVELEFT,
    /** Operador de implicacion hacia la izquierda '<-' */
    IMPLLEFT,
    /** Operador de doble implicacion '<>' */
    IMPLDOUBLE,
    /** Barra vertical '|' */
    BAR,
    /** Operador de disyunción OR logico '||' */
    ORLOG,
    /** Barra vertical con signo igual '|=' */
    BAREQ,
    /** Ampersand '&' */
    AMPERSAND,
    /** Operador de conjunción  AND logico '&&' */
    ANDLOG
}
