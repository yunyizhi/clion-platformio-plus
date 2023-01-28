package org.btik.platformioplus.ui.home.action;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.tools.Tool;
import org.btik.platformioplus.setting.PioConf;
import org.btik.platformioplus.util.ByteUtil;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


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
            openHome(toolWindow.getComponent());
        }

    }

    private void openHome(@NotNull JComponent component) {
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

        lastPioHomeProcessListener = new PioHomeProcessListener(component, CommonDataKeys.PROJECT.getData(dataContext));
        tool.execute(null, dataContext, 0, lastPioHomeProcessListener);


    }
}
