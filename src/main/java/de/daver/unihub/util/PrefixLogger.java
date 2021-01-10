package de.daver.unihub.util;

import java.util.logging.Logger;

public class PrefixLogger {

    private final Logger logger;
    private final String prefix;

    public PrefixLogger(String name){
        this.logger = Logger.getLogger(name);
        this.prefix = "[" + name + "]";
    }

    public void info(String message){
        this.logger.info(prefix + message);
    }

    public void warn(String message){
        this.logger.warning(prefix + message);
    }

    public void error(Exception e){
        this.warn(e.getMessage());
    }
}
