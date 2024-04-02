package org.btik.platformioplus.ui.home.action;

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

import static org.btik.platformioplus.service.PlatformIoPlusConst.*;

/**
 * @author lustre
 * @since 2022/10/23 10:41
 */
public class PioHomeProcessListener implements ProcessListener  {
    private static final Logger LOG = Logger.getInstance(PioHomeProcessListener.class);
    PlatformIoHomeService homeService;

    private final PioHomeToolWindow pioHomeToolWindow;
    private final PioHomeOptionPanel pioHomeOptionPanel;

    private boolean firstLine = true;

    @Override
    public void startNotified(@NotNull ProcessEvent processEvent) {
        ProcessHandler processHandler = processEvent.getProcessHandler();
        homeService.attachProcessHandler(processHandler);
    }

    @Override
    public void processTerminated(@NotNull ProcessEvent processEvent) {
        String log = "\nexit " + processEvent.getExitCode() + "\n";
        homeService.printLog(log);
        pioHomeOptionPanel.print(log);
        LOG.info("exit code:" + processEvent.getExitCode() + " ,text:" + processEvent.getText());
    }

    @Override
    public void onTextAvailable(@NotNull ProcessEvent processEvent, @NotNull Key key) {
        if (firstLine) {
            firstLine = false;
            homeService.clearLog();
            pioHomeOptionPanel.clearConsole();
        }
        String text = processEvent.getText();
        pioHomeOptionPanel.print(text);
        homeService.printLog(text);
        LOG.info(text);
        if (!text.contains(" URL => http://")) {
            return;
        }
        String[] split = text.split("=>");
        String url = split[1].trim();

        homeService.pioHomeUrl(url);
        pioHomeToolWindow.loadURL(url, true);
    }

    public PioHomeProcessListener(@NotNull Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(HOME_WINDOW);
        if (toolWindow == null) {
            throw new RuntimeException("Tool window not found");
        }
        Content content = toolWindow.getContentManager().findContent(PIO_HOME_CONTENT_ID);
        pioHomeToolWindow = (PioHomeToolWindow) content.getComponent();
        Content optContent = toolWindow.getContentManager().findContent(PIO_HOME_OPT_CONTENT_ID);
        pioHomeOptionPanel = (PioHomeOptionPanel) optContent.getComponent();
        homeService = ApplicationManager.getApplication().getService(PlatformIoHomeService.class);
    }

}
