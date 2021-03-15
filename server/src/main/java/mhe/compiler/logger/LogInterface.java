package mhe.compiler.logger;

import java.util.logging.Level;

public interface LogInterface {

    Level getLevel();

    LogType getType();

    String getMessage();

    int getRow();

    int getCol();

    int getTabLevel();

}