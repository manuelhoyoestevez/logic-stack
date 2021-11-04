package mhe.compiler.logger;

import org.slf4j.Logger;

/**
 * MheLoggerBridge.
 */
public class MheLoggerBridge implements MheLogger {
    private final Logger logger;

    public MheLoggerBridge(Logger logger) {
        this.logger = logger;
    }

    private static String buildMessage(int col, int row, String message) {
        return "[" + col + ", " + row + "] " + message;
    }

    @Override
    public void stream(int col, int row, String message) {
        logger.info(buildMessage(col, row, message));
    }

    @Override
    public void lexer(int col, int row, String message) {
        logger.info(buildMessage(col, row, message));
    }

    @Override
    public void parser(int col, int row, String message) {
        logger.info(buildMessage(col, row, message));
    }

    @Override
    public void semantic(int col, int row, String message) {
        logger.info(buildMessage(col, row, message));
    }

    @Override
    public void warn(int col, int row, String message) {
        logger.warn(buildMessage(col, row, message));
    }

    @Override
    public void error(int col, int row, String message) {
        logger.error(buildMessage(col, row, message));
    }
}
