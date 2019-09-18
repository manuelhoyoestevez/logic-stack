package mhe.compiler.mhe;


/** Categorías léxicas reconocidas
 * @author Manuel Hoyo Estévez
 */
public interface LexicalCategoryMHE {
	/** */
	public final static int ERROR = -1;
	/** */
	public final static int SKIP  =  0;
	/** Fin de flujo de entrada: se representa con el símbolo del dólar '$' */
    public final static int END = 1;
    /** Identificador: secuencia de caracteres alfanumericos no reservada */
    public final static int IDENTIFIER = 2;
    /** Número entero: secuencia de caracteres numéricos */
    public final static int INTEGER = 3;
    /** Número decimal: secuencia decaracteres numéricos con un punto entre ellos */
    public final static int DECIMAL = 4;
    /** Carácter: repersentación de un caracter ASCII encerrado entre comillas simples */
    public final static int CHARACTER = 5;
    /** Cadena de caracteres: secuencia de caracteres encerrada entre comillas dobles */
    public final static int STRING = 6;
    /** Booleano: las palabras reservadas 'true' o 'false' */
    public final static int BOOLEAN = 7;
    /** Palabra reservada 'exit' */
    public final static int EXIT = 8;
    /** Palabra reservada 'load' */
    public final static int LOAD = 9;
    /** Palabra reservada 'save' */
    public final static int SAVE = 10;
    /** Palabra reservada 'show' */
    public final static int SHOW = 11;
    /** Palabra reservada 'list' */
    public final static int LIST = 12;
    /** Palabra reservada 'test' */
    public final static int TEST = 13;
    /** Palabra reservada 'blank' */
    public final static int BLANK = 14;
    /** Palabra reservada 'alphabet' */
    public final static int ALPHABET = 15;
    /** Palabra reservada 'comment' */
    public final static int COMMENT = 16;
    /** Palabra reservada 'token' */
    public final static int TOKEN = 17;
    /** Palabra reservada 'return' */
    public final static int RETURN = 18;
    /** Palabra reservada 'grammar' */
    public final static int GRAMMAR = 19;
    /** Palabra reservada 'semantic' */
    public final static int SEMANTIC = 20;
    /** Palabra reservada 'lambda' */
    public final static int LAMBDA = 21;
    /** Paréntesis abierto '(' */
    public final static int LPAREN = 22;
    /** Paréntesis cerrado ')' */
    public final static int RPAREN = 23;
    /** Corchete abierto '[' */
    public final static int LCORCH = 24;
    /** Corchete cerrado ']' */
    public final static int RCORCH = 25;
    /** Llave abierta '{' */
    public final static int LKEY = 26;
    /** Llave cerrada '}' */
    public final static int RKEY = 27;
    /** Coma ',' */
    public final static int COLON = 28;
    /** Punto '.' */
    public final static int POINT = 29;
    /** Dos puntos ':' */
    public final static int TWOPOINT = 30;
    /** Punto y coma ';' */
    public final static int SEMICOLON = 31;
    /** Signo de interrogación '?' */
    public final static int HOOK = 32;
    /** Signo m�s '+' */
    public final static int PLUS = 33;
    /** Asignación con suma '+=' */
    public final static int PLUSEQ = 34;
    /** Operador de incremento '++' */
    public final static int INC = 35;
    /** Signo menos '-' */
    public final static int MINUS = 36;
    /** Asignación con resta '-=' */
    public final static int MINUSEQ = 37;
    /** Operador de decremento '--' */
    public final static int DEC = 38;
    /** Operador de implicacion hacia la derecha'->' */
    public final static int IMPLRIGHT = 39;
    /** Asterisco '*' */
    public final static int STAR = 40;
    /** Asignación con multiplicacion '*=' */
    public final static int STAREQ = 41;
    /** Operador de potencia '**' */
    public final static int POW = 42;
    /** Barra inclinada '/' */
    public final static int DIV = 43;
    /** Asignacion con division '/=' */
    public final static int DIVEQ = 44;
    /** signo de porcentaje '%' */
    public final static int PERCENT = 45;
    /** Signo igual '=' */
    public final static int EQUAL = 46;
    /** Operador de igualdad '==' */
    public final static int EQUALEQ = 47;
    /** Excalamación '!' */
    public final static int NOT = 48;
    /** Operador de distincion '!=' */
    public final static int NOTEQUAL = 49;
    /** Signo mayor '>' */
    public final static int BIGGER = 50;
    /** Signo mayor o igual '>=' */
    public final static int BIGGEREQ = 51;
    /** Operador de desplazamiento hacia la derecha '>>' */
    public final static int MOVERIGHT = 52;
    /** Signo menor '<' */
    public final static int SMALLER = 53;
    /** Signo menor o igual '<=' */
    public final static int SMALLEREQ = 54;
    /** Operador de desplazamiento hacia la izquierda '<<' */
    public final static int MOVELEFT = 55;
    /** Operador de implicacion hacia la izquierda '<-' */
    public final static int IMPLLEFT = 56;
    /** Operador de doble implicacion '<>' */
    public final static int IMPLDOUBLE = 57;
    /** Barra vertical '|' */
    public final static int BAR = 58;
    /** Operador de disyunción OR logico '||' */
    public final static int ORLOG = 59;
    /** Barra vertical con signo igual '|=' */
    public final static int BAREQ = 60;
    /** Ampersand '&' */
    public final static int AMPERSAND = 61;
    /** Operador de conjunción  AND logico '&&' */
    public final static int ANDLOG = 62;
}
