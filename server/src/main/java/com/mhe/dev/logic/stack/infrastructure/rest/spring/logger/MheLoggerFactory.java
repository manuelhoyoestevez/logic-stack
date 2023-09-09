package com.mhe.dev.logic.stack.infrastructure.rest.spring.logger;

import com.mhe.dev.compiler.lib.core.MheLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MheLoggerFactory.
 */
public class MheLoggerFactory
{
    public static MheLogger getLogger(Class<?> clazz)
    {
        Logger logger = LoggerFactory.getLogger(clazz);
        return new MheLoggerBridge(logger);
    }
}
