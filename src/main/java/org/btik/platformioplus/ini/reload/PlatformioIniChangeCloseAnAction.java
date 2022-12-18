package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import static org.btik.platformioplus.ini.reload.ChangeHandler.RE_INIT_DO;

/**
 * @author lustre
 * @since 2022/12/18 17:03
 */
public class PlatformioIniChangeCloseAnAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        AnAction action = ActionManager.getInstance().getAction(RE_INIT_DO);
        if (action instanceof PlatformioIniChangeAnAction platformioIniChangeAnAction) {
            platformioIniChangeAnAction.saveChangeAndHide(e);
        }
    }
}
