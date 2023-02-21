package org.btik.platformioplus.ui.task.tree.execute;

import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.ide.DataManager;
import com.intellij.tools.Tool;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.ui.task.tree.model.CommandNode;
import org.btik.platformioplus.ui.task.tree.model.LockCommandNode;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author lustre
 * @since 2022/10/20 0:29
 */
public class TreeNodeCmdExecutor {

    private static final ConcurrentHashMap<String, LockCommandProcessListener> LOCK_PROCESS_MAP = new ConcurrentHashMap<>();


    public static void execute(Component component, CommandNode commandNode, Supplier<List<String>> getEnvsFunction) {
        String platformioLocation = PioConf.findPlatformio();
        if (platformioLocation == null) {
            PioConf.notifyPlatformioNotFound();
            return;
        }
        String command = buildCommand(commandNode, getEnvsFunction);
        if (commandNode instanceof LockCommandNode lockCommandNode) {
            executeLockCommand(component, lockCommandNode, platformioLocation, command);
            return;
        }
        Tool tool = new Tool();
        tool.setName(commandNode.toString());
        tool.setProgram(platformioLocation);
        tool.setUseConsole(true);
        tool.setParameters(command);
        tool.execute(null, DataManager.getInstance().getDataContext(component),
                ExecutionEnvironment.getNextUnusedExecutionId(), null);
    }

    private static void executeLockCommand(Component component, LockCommandNode lockCommandNode, String platformioLocation, String command) {
        Tool tool = new Tool();
        tool.setName(lockCommandNode.toString());
        tool.setProgram(platformioLocation);
        tool.setUseConsole(true);
        tool.setParameters(command);
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
        tool.execute(null, DataManager.getInstance().getDataContext(component),
                ExecutionEnvironment.getNextUnusedExecutionId(), listener);
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
