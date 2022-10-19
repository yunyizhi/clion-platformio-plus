package org.btik.platformioplus.execute;

import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.ide.DataManager;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.tools.Tool;
import org.btik.platformioplus.ui.model.CommandNode;

import java.awt.*;
import java.io.File;

/**
 * @author lustre
 * @since 2022/10/20 0:29
 */
public class Runner {


    public static void run(Component component, CommandNode commandNode) {
        Tool tool = new Tool();
        tool.setName(commandNode.toString());
        String platformioLocation;
        if (SystemInfo.isWindows) {
            platformioLocation = PathEnvironmentVariableUtil.findExecutableInWindowsPath("platformio", null);
        } else {
            File file = PathEnvironmentVariableUtil.findInPath("platformio");
            platformioLocation = file == null ? null : file.getAbsolutePath();
        }
        tool.setProgram(platformioLocation);
        tool.setUseConsole(true);
        tool.setParameters(commandNode.getCommand());
        tool.execute(null, DataManager.getInstance().getDataContext(component),
                ExecutionEnvironment.getNextUnusedExecutionId(), null);
    }
}
