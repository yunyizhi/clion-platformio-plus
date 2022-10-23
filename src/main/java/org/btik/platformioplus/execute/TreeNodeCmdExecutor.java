package org.btik.platformioplus.execute;

import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.ide.DataManager;
import com.intellij.tools.Tool;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.ui.model.CommandNode;

import java.awt.*;
/**
 * @author lustre
 * @since 2022/10/20 0:29
 */
public class TreeNodeCmdExecutor {


    public static void execute(Component component, CommandNode commandNode) {
        Tool tool = new Tool();
        tool.setName(commandNode.toString());
        String platformioLocation = PioConf.findPlatformio();
        if (platformioLocation == null) {
            PioConf.notifyPlatformioNotFound();
            return;
        }
        tool.setProgram(platformioLocation);
        tool.setUseConsole(true);
        tool.setParameters(commandNode.getCommand());
        tool.execute(null, DataManager.getInstance().getDataContext(component),
                ExecutionEnvironment.getNextUnusedExecutionId(), null);
    }
}
