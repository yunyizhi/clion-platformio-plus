package org.btik.platformioplus.ui.home.action;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.tools.Tool;
import com.intellij.ui.content.Content;
import org.btik.platformioplus.service.PlatformIoHomeService;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.ui.home.PioHomeOptionPanel;
import org.btik.platformioplus.ui.home.PioHomeToolWindow;
import org.btik.platformioplus.util.ByteUtil;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static org.btik.platformioplus.service.PlatformIoPlusConst.*;


/**
 * @author lustre
 * @since 2022/10/23 2:02
 */
public class OpenPioHomeListener implements ToolWindowManagerListener {
    private static final String ID = "Pio Home";

    private PioHomeProcessListener lastPioHomeProcessListener;

    @Override
    public void toolWindowShown(@NotNull ToolWindow toolWindow) {
        String id = toolWindow.getId();

        if (ID.equals(id)) {
            if (lastPioHomeProcessListener != null && lastPioHomeProcessListener.isAlive()) {
                return;
            }
            if (!tryExistUrl(toolWindow.getProject(), toolWindow.getComponent())) {
                openHome(toolWindow.getComponent(), toolWindow.getProject());
            }

        }

    }

    private boolean tryExistUrl(@NotNull Project project, @NotNull JComponent component) {
        PlatformIoHomeService service = ApplicationManager.getApplication().getService(PlatformIoHomeService.class);
        if (service == null) {
            return false;
        }
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
        return true;
    }

    private void openHome(@NotNull JComponent component, @NotNull Project project) {
        Tool tool = new Tool();
        tool.setName("Pio Home");
        String platformioLocation = PioConf.findPlatformio();
        if (platformioLocation == null) {
            PioConf.notifyPlatformioNotFound();
            return;
        }
        tool.setProgram(platformioLocation);
        tool.setUseConsole(false);
        tool.setParameters(SysConf.getF("pio.home.parameters", ByteUtil.uuidStr(16)));

        final DataContext dataContext = DataManager.getInstance().getDataContext(component);

        lastPioHomeProcessListener = new PioHomeProcessListener(component, project);
        ApplicationManager.getApplication().invokeLater(() -> tool.execute(null, dataContext, 0, lastPioHomeProcessListener));
    }
}
