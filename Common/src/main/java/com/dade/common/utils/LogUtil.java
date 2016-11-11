package com.dade.common.utils;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dade on 2016/11/11.
 */
public final class LogUtil {

    static final Logger logger = Logger.getLogger(LogUtil.class);

    public static void infoSimply(Object message)
    {
        logger.info(message);
    }

    /**
     * discourage to use
     */
    public static void debug(Object message)
    {
        StackTraceElement ste = getCurrentElement();
        String className = getClassName(ste);
        logger.debug(getTemplate(ste, className, message));
    }

    /**
     * discourage to use
     */
    public static void debug(Object message, Throwable t)
    {
        StackTraceElement ste = getCurrentElement();
        String className = getClassName(ste);
        logger.debug(getTemplate(ste, className, message), t);
    }

    public static void info(Object message) {
        StackTraceElement ste = getCurrentElement();
        String className = getClassName(ste);
        logger.info(getTemplate(ste, className, message));
    }

    public static void infoFormat(String messageFmt, Object ... params) {
        StackTraceElement ste = getCurrentElement();
        String className = getClassName(ste);
        logger.info(getTemplate(ste, className, String.format(messageFmt, params)));
    }

    public static void info(Object message, Throwable t) {
        StackTraceElement ste = getCurrentElement();
        String className = getClassName(ste);
        logger.info(getTemplate(ste, className, message), t);
    }

    public static void warn(Object message) {
        StackTraceElement ste = getCurrentElement();
        String className = getClassName(ste);
        logger.warn(getTemplate(ste, className, message));
    }

    public static void warn(Object message, Throwable t) {
        StackTraceElement ste = getCurrentElement();
        String className = getClassName(ste);
        logger.warn(getTemplate(ste, className, message), t);
    }

    public static void error(Object message) {
        StackTraceElement ste = getCurrentElement();
        String className = getClassName(ste);
        logger.error(getTemplate(ste, className, message));
    }

    public static void error(Object message, Throwable t) {
        StackTraceElement ste = getCurrentElement();
        String className = getClassName(ste);
        logger.error(getTemplate(ste, className, message), t);
    }

    private static String getTemplate(StackTraceElement ste, String className, Object message) {
        return "[" + className + "." + ste.getMethodName() + "." + ste.getLineNumber() + "] " + message;
    }

    private static String getClassName(StackTraceElement ste) {
        String className = ste.getClassName();
        List<String> classNames = Arrays.asList(className.split("\\."));
        if (className.contains("com.hunterplus.server")) {
            classNames = classNames.subList(3, classNames.size());
        }
        return classNames.stream().collect(Collectors.joining("."));
    }

    private static StackTraceElement getCurrentElement() {
        StackTraceElement[] list = Thread.currentThread().getStackTrace();
        return list[3];
    }

}
