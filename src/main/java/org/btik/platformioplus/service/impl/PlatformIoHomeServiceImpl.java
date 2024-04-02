package org.btik.platformioplus.service.impl;

import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.ide.AppLifecycleListener;
import org.btik.platformioplus.service.PlatformIoHomeService;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * @author lustre
 * @since 2024/4/1 23:46
 */
public class PlatformIoHomeServiceImpl implements PlatformIoHomeService, AppLifecycleListener {

    private final LinkedList<String> logs = new LinkedList<>();

    private String pioHomeUrl;

    private ProcessHandler processHandler;

    @Override
    public String pioHomeUrl() {
        return pioHomeUrl;
    }

    @Override
    public void pioHomeUrl(String pioHomeUrl) {
        this.pioHomeUrl = pioHomeUrl;
    }

    @Override
    public void printLog(String log) {
        synchronized (logs) {
            while (logs.size() > MAX_LOG_LINE) {
                logs.removeFirst();
            }
            logs.addLast(log);
        }

    }

    @Override
    public void readLog(Consumer<String> logConsumer) {
        for (String log : logs) {
            logConsumer.accept(log);
        }
    }

    @Override
    public void attachProcessHandler(ProcessHandler processHandler) {
        if (this.processHandler != null) {
            shutDown();
        }
        this.processHandler = processHandler;
    }

    @Override
    public void clearLog() {
        synchronized (logs) {
            while (!logs.isEmpty()) {
                logs.removeFirst();
            }
        }
    }

    @Override
    public void shutDown() {
        if (processHandler == null) {
            return;
        }
        if (!isAlive()) {
            return;
        }
        processHandler.destroyProcess();
        if (isAlive()) {
            if (processHandler instanceof OSProcessHandler osProcessHandler) {
                Process process = osProcessHandler.getProcess();
                process.destroy();
                if (isAlive()) {
                    process.destroyForcibly();
                }
            }
        }
    }

    private boolean isAlive() {
        if (processHandler == null) {
            return false;
        }
        if (processHandler instanceof OSProcessHandler) {
            return ((OSProcessHandler) processHandler).getProcess().isAlive();
        }
        return processHandler.getExitCode() != null;
    }
}
