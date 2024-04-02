package org.btik.platformioplus.ui.home;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.tools.Tool;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.ui.home.action.PioHomeProcessListener;
import org.btik.platformioplus.util.SysConf;

/**
 * @author lustre
 * @since 2024/4/2 22:27
 */
public class RunPioHomeTool {

    public static void run(final DataContext dataContext, PioHomeProcessListener pioHomeProcessListener) {
        Tool tool = new Tool();
        tool.setName("Pio Home");
        String platformioLocation = PioConf.findPlatformio();
        if (platformioLocation == null) {
            PioConf.notifyPlatformioNotFound();
            return;
        }
        tool.setProgram(platformioLocation);
        tool.setUseConsole(false);
        tool.setParameters(SysConf.getF("pio.home.parameters"));
        ApplicationManager.getApplication().invokeLater(() -> tool.execute(null, dataContext, 0,
                pioHomeProcessListener));
    }
}
