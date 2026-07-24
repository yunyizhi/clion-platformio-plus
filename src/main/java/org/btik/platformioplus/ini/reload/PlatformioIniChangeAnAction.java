package org.btik.platformioplus.ini.reload;

import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import org.btik.platformioplus.icon.PlatformIoPlusIcon;
import org.btik.platformioplus.run.config.PioConsoleRunProfile;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.util.CmdTaskExecutor;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;

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
        String platformioLocation = PioConf.findPlatformio();
        if (platformioLocation == null) {
            PioConf.notifyPlatformioNotFound();
            return;
        }
        Project project = e.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        PtyCommandLine commandLine = new PtyCommandLine();
        commandLine.setExePath(platformioLocation);
        commandLine.setWorkDirectory(project.getBasePath());
        commandLine.setCharset(Charset.forName(System.getProperty("sun.jnu.encoding", "UTF-8")));
        commandLine.withConsoleMode(true);
        String trimmedCommand = command.trim();
        if (!trimmedCommand.isEmpty()) {
            commandLine.addParameters(trimmedCommand.split("\\s+"));
        }
        PioConsoleRunProfile runProfile = new PioConsoleRunProfile("Load Changes", PlatformIoPlusIcon.PIOPLUS, commandLine);
        try {
            CmdTaskExecutor.execute(project, runProfile);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        PioIniChangeHandler service = project.getService(PioIniChangeHandler.class);
        service.saveChangeAndHide();
    }

}
