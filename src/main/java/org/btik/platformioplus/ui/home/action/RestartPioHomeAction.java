package org.btik.platformioplus.ui.home.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.btik.platformioplus.service.PlatformIoHomeService;
import org.btik.platformioplus.ui.home.RunPioHomeTool;
import org.jetbrains.annotations.NotNull;

/**
 * @author lustre
 * @since 2024/4/1 21:54
 */
public class RestartPioHomeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }
        PlatformIoHomeService service = ApplicationManager.getApplication().getService(PlatformIoHomeService.class);
        service.shutDown();
        RunPioHomeTool.run(anActionEvent.getDataContext(), new PioHomeProcessListener(project));
    }
}
