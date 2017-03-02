package ru.altarix.marm.utils;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;


public class MarmTestWatcher extends TestWatcher {

    private Logger log;

    public MarmTestWatcher(String loggerName) {
        this.log = LogManager.getLogger(loggerName);
    }

    @Override
    protected void succeeded(Description description) {
        log.info(description);
        log.info("TEST SUCCESS");
    }

    @Override
    protected void failed(Throwable e, Description description) {
        log.error(description, e);
        log.info("TEST FAIL");
    }
}
