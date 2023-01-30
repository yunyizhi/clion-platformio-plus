package org.btik.platformioplus.ini.reload;

import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.tools.Tool;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NotNull;


/**
 * @author lustre
 * @since 2022/12/17 15:05
 */
public class PlatformioIniChangeAnAction extends AnAction {

    private final String command;

    public PlatformioIniChangeAnAction() {
        this.command = SysConf.get("pio.re.init.parameters");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Tool tool = new Tool();
        tool.setName("Load Changes");
        String platformioLocation = PioConf.findPlatformio();
        if (platformioLocation == null) {
            PioConf.notifyPlatformioNotFound();
            return;
        }
        tool.setProgram(platformioLocation);
        tool.setUseConsole(true);
        tool.setParameters(command);
        tool.execute(e, e.getDataContext(),
                ExecutionEnvironment.getNextUnusedExecutionId(), null);
        Project project = e.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        ToolBarStatus service = project.getService(ToolBarStatus.class);
        service.saveChangeAndHide();

    }

}
