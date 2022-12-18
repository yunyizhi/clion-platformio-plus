package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarProvider;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * 重新加载ini的配置
 *
 * @author lustre
 * @since 2022/12/16 20:37
 */
public class PlatformioIniFloatingToolbarProvider implements FloatingToolbarProvider {

    /**
     * 配置于plugin.xml的id
     */
    public final static String GROUP_ID = "pio.re-init";


    @Override
    public boolean getAutoHideable() {
        return false;
    }

    @Override
    public void register(@NotNull DataContext dataContext, @NotNull FloatingToolbarComponent component, @NotNull Disposable parentDisposable) {
        Project project = dataContext.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        ToolBarStatus service = project.getService(ToolBarStatus.class);
        service.register(component);
    }

    @NotNull
    @Override
    public ActionGroup getActionGroup() {
        AnAction action = ActionManager.getInstance().getAction(GROUP_ID);
        return (ActionGroup) action;
    }
}
