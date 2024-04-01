package org.btik.platformioplus.ui.home.action;

import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import org.btik.platformioplus.service.PlatformIoHomeService;
import org.jetbrains.annotations.NotNull;
import org.btik.platformioplus.ui.home.PioHomeToolWindow;
import org.btik.platformioplus.ui.home.PioHomeOptionPanel;

import javax.swing.*;

import static org.btik.platformioplus.service.PlatformIoPlusConst.*;

/**
 * @author lustre
 * @since 2022/10/23 10:41
 */
public class PioHomeProcessListener implements ProcessListener {
    private static final Logger LOG = Logger.getInstance(PioHomeProcessListener.class);

    private ProcessHandler processHandler;

    private PioHomeToolWindow pioHomeToolWindow;
    private PioHomeOptionPanel pioHomeOptionPanel;

    @Override
    public void startNotified(@NotNull ProcessEvent processEvent) {
        this.processHandler = processEvent.getProcessHandler();
    }

    @Override
    public void processTerminated(@NotNull ProcessEvent processEvent) {
        PlatformIoHomeService service = ApplicationManager.getApplication().getService(PlatformIoHomeService.class);
        service.pioHomeUrl(null);
        LOG.info("exit code:" + processEvent.getExitCode() + " ,text:" + processEvent.getText());
    }

    @Override
    public void onTextAvailable(@NotNull ProcessEvent processEvent, @NotNull Key key) {
        String text = processEvent.getText();
        pioHomeOptionPanel.print(text);
        LOG.info(text);
        if (!text.contains(" URL => http://")) {
            return;
        }
        String[] split = text.split("=>");
        String url = split[1].trim();
        PlatformIoHomeService service = ApplicationManager.getApplication().getService(PlatformIoHomeService.class);
        service.pioHomeUrl(url);
        pioHomeToolWindow.loadURL(url);
    }


    public PioHomeProcessListener(JComponent component, @NotNull Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(HOME_WINDOW);
        if (toolWindow != null) {
            Content content = toolWindow.getContentManager().findContent(PIO_HOME_CONTENT_ID);
            pioHomeToolWindow = (PioHomeToolWindow) content.getComponent();
            Content optContent = toolWindow.getContentManager().findContent(PIO_HOME_OPT_CONTENT_ID);
            pioHomeOptionPanel = (PioHomeOptionPanel) optContent.getComponent();
        }

    }

    public void shutDown() {
        if (processHandler == null) {
            return;
        }
        if (!isAlive()) {
            return;
        }
        processHandler.destroyProcess();
        if (isAlive()) {
            if (processHandler instanceof OSProcessHandler osProcessHandler) {
                Process process = osProcessHandler.getProcess();
                process.destroy();
                if (isAlive()) {
                    process.destroyForcibly();
                }
            }
        }


    }

    public boolean isAlive() {
        if (processHandler == null) {
            return false;
        }
        if (processHandler instanceof OSProcessHandler) {
            return ((OSProcessHandler) processHandler).getProcess().isAlive();
        }
        return processHandler.getExitCode() != null;
    }

}
