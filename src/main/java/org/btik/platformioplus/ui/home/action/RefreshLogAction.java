package org.btik.platformioplus.ui.home.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import org.btik.platformioplus.service.PlatformIoHomeService;
import org.btik.platformioplus.ui.home.PioHomeOptionPanel;
import org.jetbrains.annotations.NotNull;

import static org.btik.platformioplus.service.PlatformIoPlusConst.*;

/**
 * @author lustre
 * @since 2024/4/2 22:14
 */
public class RefreshLogAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(HOME_WINDOW);
        if (toolWindow == null) {
            return;
        }
        Content optContent = toolWindow.getContentManager().findContent(PIO_HOME_OPT_CONTENT_ID);
        PioHomeOptionPanel optContentComponent = (PioHomeOptionPanel) optContent.getComponent();
        optContentComponent.clearConsole();
        ApplicationManager.getApplication().getService(PlatformIoHomeService.class)
                .readLog(optContentComponent::print);
    }
}

