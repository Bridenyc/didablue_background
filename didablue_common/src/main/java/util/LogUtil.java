package util;


import org.apache.log4j.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

@SuppressWarnings("all")
public class LogUtil {


    private Logger logger = null;

    private static final String FQCN = LogUtil.class.getName();

    private static ConsoleAppender consoleAppender = null;

    private static HashMap<String, LogUtil> debugServiceMap = null;

    private static HashMap<String, DailyRollingFileAppender> fileAppenderMap = null;

    private static DailyRollingFileAppender totalLogFileAppender = null;

    private static String logDir = "./log";

    private static String idc_log_level = "idc.log.level";

    /** 是否打印日志 **/
    public static boolean   showLog      = true;

    static SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    /**
     * 简单日志打印
     *
     * @param msg
     */
    public static void print(String msg) {
        if (showLog) {

            String source = null;

            try {
                StackTraceElement st = Thread.currentThread().getStackTrace()[2];
                source = "[" + st.getFileName() + "] - " + st.getMethodName() + "("
                        + st.getLineNumber() + ")";
            } catch (Exception e) {
            }

            System.out.println(fm.format(new Date()) + " - " + source + ":" + msg);
        }
    }

    public static void error(String msg){
        if (showLog) {

            String source = null;

            try {
                StackTraceElement st = Thread.currentThread().getStackTrace()[2];
                source = "[" + st.getFileName() + "] - " + st.getMethodName() + "("
                        + st.getLineNumber() + ")";
            } catch (Exception e) {
            }

            System.err.println(fm.format(new Date()) + " - " + source + ":" + msg);
        }
    }

    public synchronized static LogUtil getInstances(final String module, final Class caller) {
        if (fileAppenderMap == null) {
            fileAppenderMap = new HashMap<String, DailyRollingFileAppender>();
        }
        if (debugServiceMap == null) {
            Enumeration e = Logger.getRootLogger().getAllAppenders();
            while (e.hasMoreElements()) {
                Appender appender = (Appender) e.nextElement();
                if (appender instanceof ConsoleAppender) {
                    consoleAppender = (ConsoleAppender) appender;
                }
                if(appender instanceof DailyRollingFileAppender){
                    totalLogFileAppender = (DailyRollingFileAppender)appender;
                }
            }
            debugServiceMap = new HashMap<String, LogUtil>();
            Properties prop = loadLogProperties();
            if (null != prop) {
                if (prop.getProperty("idc_log_level") != null) {
                    idc_log_level =  prop.getProperty("idc_log_level");
                }
                if (prop.getProperty("idc.log.file.dir") != null) {
                    logDir = prop.getProperty("idc.log.file.dir");
                }
            }
            if (consoleAppender != null) {
                consoleAppender.setThreshold(Level.toLevel(idc_log_level));
                consoleAppender.setLayout(new PatternLayout(" %-5p:%d{yyyy-MM-dd HH:mm:ss,SSS}:%m%n      location--> [%l]%n"));
            }
            if(null != totalLogFileAppender){
                totalLogFileAppender.setThreshold(Level.toLevel(idc_log_level));
                totalLogFileAppender.setLayout(new PatternLayout(" %-5p:%d{yyyy-MM-dd HH:mm:ss,SSS}:%m%n      location--> [%l]%n"));
            }
        }
        String fullName = logDir +File.separator +  module + ".log";
        if (debugServiceMap.containsKey(fullName)) {
            return debugServiceMap.get(fullName);
        }
        LogUtil debug = new LogUtil(fullName, caller.getName());
        debugServiceMap.put(fullName, debug);
        return debug;
    }
    public synchronized static LogUtil getInstances(final Class caller) {
        if(null == totalLogFileAppender){
            Enumeration e = Logger.getRootLogger().getAllAppenders();
            while (e.hasMoreElements()) {
                Appender appender = (Appender) e.nextElement();
                if(appender instanceof DailyRollingFileAppender){
                    totalLogFileAppender = (DailyRollingFileAppender)appender;
                }
            }
        }
        if(null == consoleAppender){
            Enumeration e = Logger.getRootLogger().getAllAppenders();
            while (e.hasMoreElements()) {
                Appender appender = (Appender) e.nextElement();
                if(appender instanceof ConsoleAppender){
                    consoleAppender = (ConsoleAppender)appender;
                }
            }
        }
        LogUtil debug = new LogUtil(caller.getName());
        return debug;
    }

    private static DailyRollingFileAppender getFileAppender(final String fileName) throws IOException {
        if (fileAppenderMap.containsKey(fileName)) {
            return fileAppenderMap.get(fileName);
        }
        DailyRollingFileAppender fileAppender = new DailyRollingFileAppender(new PatternLayout(" %-5p:%d{yyyy-MM-dd HH:mm:ss,SSS}:%m%n      location--> [%l]%n"), fileName, "'.'yyyy-MM-dd");
        fileAppender.setThreshold(Level.toLevel(idc_log_level));
        fileAppenderMap.put(fileName, fileAppender);
        return fileAppender;
    }

    /**
     * 构造函数。
     *
     * @param name
     *            The name of the logger to retrieve
     */
    private LogUtil(final String module, final String name) {
        try {
            logger = Logger.getLogger(name);
            logger.setAdditivity(false);
            logger.addAppender(getFileAppender(module));
            logger.addAppender(totalLogFileAppender);
            logger.addAppender(consoleAppender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private LogUtil(final String name){
        logger = Logger.getLogger(name);
        logger.setAdditivity(false);
        logger.addAppender(totalLogFileAppender);
        logger.addAppender(consoleAppender);
    }

    /**
     * 输出DEBUG级别的调试信息。
     *
     * @param msg
     *            调试信息参数
     */
    public void debug(final Object msg) {
        logger.log(FQCN, Level.DEBUG, msg, null);
    }

    /**
     * 输出DEBUG级别的调试信息和抛出的异常跟踪信息。
     *
     * @param msg
     *            调试信息参数
     * @param t
     *            异常跟踪信息参数
     */
    public void debug(final Object msg, final Throwable t) {
        logger.log(FQCN, Level.DEBUG, msg, t);
    }

    /**
     * 输出INFO级别的调试信息。
     *
     * @param msg
     *            调试信息参数
     */
    public void info(final Object msg) {
        logger.log(FQCN, Level.INFO, msg, null);
    }

    /**
     * 输出INFO级别的调试信息和抛出的异常跟踪信息。
     *
     * @param msg
     *            调试信息参数
     * @param t
     *            异常跟踪信息参数
     */
    public void info(final Object msg, final Throwable t) {
        logger.log(FQCN, Level.INFO, msg, t);
    }

    /**
     * 输出WARN级别的调试信息。
     *
     * @param msg
     *            调试信息参数
     */
    public void warn(final Object msg) {
        logger.log(FQCN, Level.WARN, msg, null);
    }

    /**
     * 输出WARN级别的调试信息和抛出的异常跟踪信息。
     *
     * @param msg
     *            调试信息参数
     * @param t
     *            异常跟踪信息参数
     */
    public void warn(final Object msg, final Throwable t) {
        logger.log(FQCN, Level.WARN, msg, t);
    }

    /**
     * 输出ERROR级别的调试信息。
     *
     * @param msg
     *            调试信息参数
     */
    public void error(final Object msg) {
        logger.log(FQCN, Level.ERROR, msg, null);
    }

    /**
     * 输出ERROR级别的调试信息和抛出的异常跟踪信息。
     *
     * @param msg
     *            调试信息参数
     * @param t
     *            异常跟踪信息参数
     */
    public void error(final Object msg, final Throwable t) {
        logger.log(FQCN, Level.ERROR, msg, t);
    }

    /**
     * 输出FATAL级别的调试信息。
     *
     * @param msg
     *            调试信息参数
     */
    public void fatal(final Object msg) {
        logger.log(FQCN, Level.FATAL, msg, null);
    }

    /**
     * 输出FATAL级别的调试信息和异常跟踪信息。
     *
     * @param msg
     *            调试信息参数
     * @param t
     *            异常跟踪信息参数
     */
    public void fatal(final Object msg, final Throwable t) {
        logger.log(FQCN, Level.FATAL, msg, t);
    }

    /**
     * 判断是否可以输出DEBUG级别的调试信息。
     *
     * @return 若可以输出DEBUG级别的调试信息，返回true；否则返回false。
     */
    public boolean isDebugEnabled() {
        return logger.isEnabledFor(Level.DEBUG);
    }

    /**
     * 判断是否可以输出INFO级别的调试信息。
     *
     * @return 若可以输出INFO级别的调试信息，返回true；否则返回false。
     */
    public boolean isInfoEnabled() {
        return logger.isEnabledFor(Level.INFO);
    }

    /**
     * 判断是否可以输出WARN级别的调试信息。
     *
     * @return 若可以输出WARN级别的调试信息，返回true；否则返回false。
     */
    public boolean isWarnEnabled() {
        return logger.isEnabledFor(Level.WARN);
    }

    /**
     * 判断是否可以输出ERROR级别的调试信息。
     *
     * @return 若可以输出ERROR级别的调试信息，返回true；否则返回false。
     */
    public boolean isErrorEnabled() {
        return logger.isEnabledFor(Level.ERROR);
    }

    /**
     * 判断是否可以输出FATAL级别的调试信息。
     *
     * @return 若可以输出FATAL级别的调试信息，返回true；否则返回false。
     */
    public boolean isFatalEnabled() {
        return logger.isEnabledFor(Level.FATAL);
    }



    private static Properties loadLogProperties() {
        FileInputStream fileInputStream = null;
        Properties prop = null;
        try {
            prop = new Properties();
            String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            InputStream in = LogUtil.class.getClassLoader().getResourceAsStream("log4j.properties");
            prop.load(in);
            return prop;

        } catch (Exception e) {
            e.printStackTrace();
            return prop;
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
