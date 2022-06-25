package com.mhe.dev.logic.stack.core.compiler.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MheLoggerFactory.
 */
public class MheLoggerFactory {
    public static MheLogger getLogger(Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        return new MheLoggerBridge(logger);
    }
}
