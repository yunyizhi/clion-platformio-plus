package org.btik.platformioplus.service;

import com.intellij.execution.process.ProcessHandler;

import java.util.function.Consumer;

/**
 * @author lustre
 * @since 2024/4/1 23:45
 */
public interface PlatformIoHomeService {

    int MAX_LOG_LINE = 100;

    String pioHomeUrl();

    void pioHomeUrl(String pioHomeUrl);

    void printLog(String log);

    void readLog(Consumer<String> logConsumer);

    void attachProcessHandler(ProcessHandler processHandler);

    void clearLog();

    void shutDown();
}
