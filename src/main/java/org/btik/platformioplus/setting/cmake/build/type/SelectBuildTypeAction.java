package org.btik.platformioplus.setting.cmake.build.type;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions;
import com.jetbrains.cidr.cpp.cmake.model.CMakeModelConfigurationData;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * @author lustre
 * @since 2023/5/9 15:38
 */
public class SelectBuildTypeAction extends ComboBoxAction implements DumbAware {


    protected @NotNull DefaultActionGroup createPopupActionGroup(@NotNull JComponent button, @NotNull DataContext dataContext) {
        return getActions(dataContext.getData(CommonDataKeys.PROJECT));
    }


    public DefaultActionGroup getActions(Project project) {
        CMakeWorkspace cmakeWorkspace = CMakeWorkspace.getInstance(project);
        List<CMakeModelConfigurationData> cMakeModelConfigurationData = cmakeWorkspace.getModelConfigurationData();
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        if (!cMakeModelConfigurationData.isEmpty()) {
            List<String> buildTypes = cMakeModelConfigurationData.get(0).getRegisteredBuildTypes();
            for (String buildType : buildTypes) {
                actionGroup.add(new CheckBuildTypeAction(buildType));
            }
        }
        return actionGroup;
    }

    static class CheckBuildTypeAction extends AnAction {

        public CheckBuildTypeAction(@Nullable @NlsActions.ActionText String text) {
            super(text);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            String buildType = e.getPresentation().getText();
            System.out.println(buildType);
        }


    }

}
