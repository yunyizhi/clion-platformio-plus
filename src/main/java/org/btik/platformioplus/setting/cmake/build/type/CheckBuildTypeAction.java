package org.btik.platformioplus.setting.cmake.build.type;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions;
import com.jetbrains.cidr.cpp.cmake.CMakeSettings;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

/**
 * @author lustre
 * @since 2023/5/9 15:38
 */
public class CheckBuildTypeAction extends AnAction implements DumbAware {

    private final BuildTypeChangeListener buildTypeChangeListener;

    public CheckBuildTypeAction(@Nullable @NlsActions.ActionText String text, BuildTypeChangeListener buildTypeChangeListener) {
        super(text);
        this.buildTypeChangeListener = buildTypeChangeListener;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) {
            return;
        }
        CMakeWorkspace cmakeWorkspace = CMakeWorkspace.getInstance(project);
        CMakeSettings settings = cmakeWorkspace.getSettings();
        String buildType = e.getPresentation().getText();
        settings.setProfiles(Collections.singletonList(new CMakeSettings.Profile(buildType)), true);
        buildTypeChangeListener.update();

    }
}
