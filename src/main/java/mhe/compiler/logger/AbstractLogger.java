package mhe.compiler.logger;

public abstract class AbstractLogger implements Logger {
    private int tabLevel = 0;

    public int getTabLevel() {
        return tabLevel;
    }

    @Override
    public Logger incTabLevel() {
        ++this.tabLevel;
        return this;
    }

    @Override
    public Logger decTabLevel() {
        --this.tabLevel;
        return this;
    }
}
