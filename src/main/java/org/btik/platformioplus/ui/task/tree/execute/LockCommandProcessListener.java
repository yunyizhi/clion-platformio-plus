package org.btik.platformioplus.ui.task.tree.execute;

import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import org.jetbrains.annotations.NotNull;

/**
 * @author lustre
 * @since 2023/1/28 14:05
 */
public class LockCommandProcessListener implements ProcessListener {
    private ProcessHandler processHandler;

    private final String lock;

    @Override
    public void startNotified(@NotNull ProcessEvent event) {
        this.processHandler = event.getProcessHandler();
    }

    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
        this.processHandler = null;
        TreeNodeCmdExecutor.unlock(lock, this);
    }

    public boolean isAlive() {
        if (processHandler == null) {
            return false;
        }
        if (processHandler instanceof OSProcessHandler) {
            return ((OSProcessHandler) processHandler).getProcess().isAlive();
        }
        return processHandler.getExitCode() != null;
    }

    public void destroy() {
        if (!isAlive()) {
            return;
        }

        processHandler.destroyProcess();
        processHandler.removeProcessListener(this);

    }

    public LockCommandProcessListener(String lock) {
        this.lock = lock;
    }
}
