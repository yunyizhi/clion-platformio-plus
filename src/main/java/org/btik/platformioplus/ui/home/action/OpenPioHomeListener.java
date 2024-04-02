package org.btik.platformioplus.ui.home.action;

import com.intellij.ide.DataManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import org.btik.platformioplus.service.PlatformIoHomeService;
import org.btik.platformioplus.ui.home.PioHomeOptionPanel;
import org.btik.platformioplus.ui.home.PioHomeToolWindow;
import org.btik.platformioplus.ui.home.RunPioHomeTool;
import org.btik.platformioplus.util.Note;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static org.btik.platformioplus.service.PlatformIoPlusConst.*;
import static org.btik.platformioplus.util.Note.NOTIFICATION_GROUP;


/**
 * @author lustre
 * @since 2022/10/23 2:02
 */
public class OpenPioHomeListener implements ToolWindowManagerListener {
    private static final Logger LOG = Logger.getInstance(PioHomeProcessListener.class);
    private static final String ID = "Pio Home";

    PlatformIoHomeService service;

    @Override
    public void toolWindowShown(@NotNull ToolWindow toolWindow) {
        String id = toolWindow.getId();
        if (!ID.equals(id)) {
            return;
        }
        if (service == null) {
            service = ApplicationManager.getApplication().getService(PlatformIoHomeService.class);
        }
        // 每次切换到Home window都刷一次
        if (!tryExistUrl(toolWindow.getProject())) {
            openHome(toolWindow.getComponent(), toolWindow.getProject());
        }

    }

    private boolean tryExistUrl(@NotNull Project project) {
        String pioHomeUrl = service.pioHomeUrl();
        if (null == pioHomeUrl || pioHomeUrl.isEmpty()) {
            return false;
        }
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(HOME_WINDOW);
        if (toolWindow == null) {
            return false;
        }
        Content content = toolWindow.getContentManager().findContent(PIO_HOME_CONTENT_ID);
        PioHomeToolWindow pioHomeOptionPanel = (PioHomeToolWindow) content.getComponent();
        pioHomeOptionPanel.loadURL(pioHomeUrl);
        Content optContent = toolWindow.getContentManager().findContent(PIO_HOME_OPT_CONTENT_ID);
        PioHomeOptionPanel optContentComponent = (PioHomeOptionPanel) optContent.getComponent();
        optContentComponent.clearConsole();
        service.readLog(optContentComponent::print);
        return true;
    }

    private void openHome(@NotNull JComponent component, @NotNull Project project) {
        PioHomeProcessListener lastPioHomeProcessListener;
        try {
            lastPioHomeProcessListener = new PioHomeProcessListener(project);
        } catch (Exception e) {
            NOTIFICATION_GROUP
                    .createNotification(Note.getMsg("unexpected.exception"),
                            Note.getMsg("unexpected.exception"), NotificationType.ERROR)
                    .notify(project);
            LOG.info(e);
            return;
        }
        final DataContext dataContext = DataManager.getInstance().getDataContext(component);
        RunPioHomeTool.run(dataContext, lastPioHomeProcessListener);
    }
}
