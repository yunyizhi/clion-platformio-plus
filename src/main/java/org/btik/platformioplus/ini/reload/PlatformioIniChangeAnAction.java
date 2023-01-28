package org.btik.platformioplus.ini.reload;

import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.tools.Tool;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * @author lustre
 * @since 2022/12/17 15:05
 */
public class PlatformioIniChangeAnAction extends AnAction implements ChangeHandler {


    private CharSequence lastSaveCharSequence = "";

    private CharSequence lastUnSaveCharSequence = "";

    private final String command;

    private volatile boolean fileChange;

    private Project project;


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
        this.saveChangeAndHide(e);

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        if (project != null) {
            return;
        }
        this.project = e.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        ToolBarStatus service = project.getService(ToolBarStatus.class);
        service.setVisible(fileChange);


    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(CharSequence charSequence) {
        lastUnSaveCharSequence = charSequence;

        // 文件回退则隐藏，否则依然存在差异则显示
        fileChange = !Objects.equals(lastSaveCharSequence, charSequence);
        if (project != null) {
            ToolBarStatus service = project.getService(ToolBarStatus.class);
            service.setVisible(fileChange);
        }
    }


    void saveChangeAndHide(AnActionEvent e) {
        lastSaveCharSequence = lastUnSaveCharSequence;
        fileChange = false;
        Project project = e.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        // 默认隐藏
        ToolBarStatus service = project.getService(ToolBarStatus.class);
        service.setVisible(false);
    }

    @Override
    public Project getProject() {
        return this.project;
    }
}
