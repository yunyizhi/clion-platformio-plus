package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarProvider;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
    public boolean isApplicable(@NotNull DataContext dataContext) {
        Editor editor = dataContext.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return false;
        }
        return PioConf.FILE_NAME.equals(editor.getVirtualFile().getName());
    }

    @Override
    public void register(@NotNull DataContext dataContext, @NotNull FloatingToolbarComponent component, @NotNull Disposable parentDisposable) {
        Project project = dataContext.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        PioIniChangeHandler service = project.getService(PioIniChangeHandler.class);
        service.register(component ,dataContext);
    }

    @NotNull
    @Override
    public ActionGroup getActionGroup() {
        AnAction action = ActionManager.getInstance().getAction(GROUP_ID);
        return (ActionGroup) action;
    }
}
