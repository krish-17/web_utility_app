package com.wau;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogAPIImpl implements LogAPI {
    private static Logger logger;
    private static LogAPIImpl log = null;

    private LogAPIImpl() {
    }

    public static LogAPIImpl instance() {
        if (null == log && null == logger) {
            log = new LogAPIImpl();
            logger = LogManager.getLogger(WebUtilityAppApplication.class);
        }
        return log;
    }

    @Override
    public void infoLog(String message) {
        logger.info(message);
    }

    @Override
    public void errorLog(String message) {
        logger.error(message);
    }

}
