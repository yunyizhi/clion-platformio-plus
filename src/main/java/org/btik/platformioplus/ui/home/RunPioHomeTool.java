package org.btik.platformioplus.ui.home;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.ui.home.action.PioHomeProcessListener;
import org.btik.platformioplus.util.SysConf;

import java.nio.charset.Charset;

/**
 * @author lustre
 * @since 2024/4/2 22:27
 */
public class RunPioHomeTool {

    private static final Logger LOG = Logger.getInstance(RunPioHomeTool.class);

    public static void run(final DataContext dataContext, PioHomeProcessListener pioHomeProcessListener) {
        String platformioLocation = PioConf.findPlatformio();
        if (platformioLocation == null) {
            PioConf.notifyPlatformioNotFound();
            return;
        }
        ApplicationManager.getApplication().invokeLater(() -> {
            Project project = CommonDataKeys.PROJECT.getData(dataContext);
            if (project == null) {
                return;
            }
            GeneralCommandLine commandLine = new GeneralCommandLine(platformioLocation);
            commandLine.setCharset(Charset.forName(System.getProperty("sun.jnu.encoding", "UTF-8")));
            String homeParameters = SysConf.getF("pio.home.parameters").trim();
            if (!homeParameters.isEmpty()) {
                commandLine.addParameters(homeParameters.split("\\s+"));
            }
            // Pio Home 是常驻的本地服务进程，直接以 ProcessHandler 方式后台启动，
            // 不进入「运行」工具窗口，避免被用户误关闭导致 Pio Home 界面失效。
            // 进程生命周期由 PlatformIoHomeService 统一管理（attach/shutDown）。
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                try {
                    KillableColoredProcessHandler processHandler = new KillableColoredProcessHandler(commandLine);
                    if (pioHomeProcessListener != null) {
                        processHandler.addProcessListener(pioHomeProcessListener);
                    }
                    processHandler.startNotify();
                } catch (Exception e) {
                    LOG.error("start pio home process failed", e);
                }
            });
        });
    }
}
