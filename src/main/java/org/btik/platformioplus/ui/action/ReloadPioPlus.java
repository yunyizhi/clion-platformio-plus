package org.btik.platformioplus.ui.action;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.btik.platformioplus.service.PlatformIoPlusService;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;

import static org.btik.platformioplus.service.PlatformIoPlusService.WINDOW_ARRAY;
import static org.btik.platformioplus.util.Note.NOTIFICATION_GROUP;
import static org.btik.platformioplus.util.Note.getMsg;

/**
 * @author lustre
 * @since 2022/10/23 17:44
 */
public class ReloadPioPlus extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            NOTIFICATION_GROUP.createNotification(getMsg("notification.group.platformio-plus"),
                    getMsg("load.pio.plus.failed"), NotificationType.WARNING).notify(null);
            return;
        }
        for (String id : WINDOW_ARRAY) {
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(id);
            if(toolWindow != null) {
                toolWindow.setAvailable(true);
            }
        }

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        if (!ActionPlaces.isMainMenuOrActionSearch(e.getPlace())) {
            VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
            presentation.setEnabledAndVisible(file != null
                    && PioConf.FILE_NAME.equals(file.getName()));
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
