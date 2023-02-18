package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;


/**
 * @author lustre
 * @since 2022/12/18 17:03
 */
public class PlatformioIniChangeCloseAnAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        PioIniChangeHandler service = project.getService(PioIniChangeHandler.class);
        service.saveChangeAndHide();
    }
}
