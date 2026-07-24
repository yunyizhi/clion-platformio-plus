package org.btik.platformioplus.ui.task.tree.execute;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.openapi.project.Project;
import org.btik.platformioplus.icon.PlatformIoPlusIcon;
import org.btik.platformioplus.run.config.PioConsoleRunProfile;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.ui.task.tree.model.CommandNode;
import org.btik.platformioplus.ui.task.tree.model.LockCommandNode;
import org.btik.platformioplus.util.CmdTaskExecutor;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author lustre
 * @since 2022/10/20 0:29
 */
public class TreeNodeCmdExecutor {

    private static final ConcurrentHashMap<String, LockCommandProcessListener> LOCK_PROCESS_MAP = new ConcurrentHashMap<>();


    public static void execute(Project project, CommandNode commandNode, Supplier<List<String>> getEnvsFunction) {
        String platformioLocation = PioConf.findPlatformio();
        if (platformioLocation == null) {
            PioConf.notifyPlatformioNotFound();
            return;
        }
        String command = buildCommand(commandNode, getEnvsFunction);
        if (commandNode instanceof LockCommandNode lockCommandNode) {
            executeLockCommand(project, lockCommandNode, platformioLocation, command);
            return;
        }
        execTask(project, commandNode.toString(), platformioLocation, command, null);
    }

    private static void executeLockCommand(Project project, LockCommandNode lockCommandNode, String platformioLocation, String command) {
        String lock = lockCommandNode.getLock();
        LockCommandProcessListener listener = LOCK_PROCESS_MAP.compute(lock, (key, oldVal) -> {
            if (oldVal == null) {
                return new LockCommandProcessListener(lock);
            }
            if (oldVal.isAlive()) {
                oldVal.destroy();
            }
            return new LockCommandProcessListener(lock);
        });
        execTask(project, lockCommandNode.toString(), platformioLocation, command, listener);
    }

    private static void execTask(Project project, String name, String platformioLocation, String command, LockCommandProcessListener listener) {
        PtyCommandLine commandLine = new PtyCommandLine();
        commandLine.setExePath(platformioLocation);
        commandLine.setWorkDirectory(project.getBasePath());
        commandLine.setCharset(Charset.forName(System.getProperty("sun.jnu.encoding", "UTF-8")));
        commandLine.withConsoleMode(true);
        String trimmedCommand = command.trim();
        if (!trimmedCommand.isEmpty()) {
            commandLine.addParameters(trimmedCommand.split("\\s+"));
        }
        PioConsoleRunProfile runProfile = new PioConsoleRunProfile(name, PlatformIoPlusIcon.PIOPLUS, commandLine);
        if (listener != null) {
            runProfile.addProcessListener(listener);
        }
        try {
            CmdTaskExecutor.execute(project, runProfile);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    static void unlock(String lock, LockCommandProcessListener listener) {
        LOCK_PROCESS_MAP.compute(lock, (key, oldVal) -> {
            // 当锁命令的监听器是自身时注销掉，而不是自身时，则无需操作
            if (oldVal == null || oldVal == listener) {
                return null;
            }
            return oldVal;
        });
    }

    private static String buildCommand(CommandNode commandNode, Supplier<List<String>> getEnvsFunction) {
        String envParamKey = commandNode.getEnvParamKey();
        if (null == envParamKey || envParamKey.isEmpty()) {
            return commandNode.getCommand();
        }

        StringBuilder envParamsBuilder = new StringBuilder();
        for (String env : getEnvsFunction.get()) {
            envParamsBuilder.append(envParamKey).append(env).append(' ');
        }
        return commandNode.getCommand() + envParamsBuilder;
    }

}
