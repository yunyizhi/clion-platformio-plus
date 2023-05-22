package org.btik.platformioplus.setting.cmake.build.type;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.openapi.wm.impl.status.TextPanel;
import com.intellij.ui.ClickListener;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ui.JBUI;
import com.jetbrains.cidr.cpp.cmake.CMakeSettings;
import com.jetbrains.cidr.cpp.cmake.CMakeSettingsListener;
import com.jetbrains.cidr.cpp.cmake.model.CMakeModelConfigurationData;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;
import org.btik.platformioplus.service.PlatformIoPlusService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static org.btik.platformioplus.util.SysConf.$sys;

/**
 * @author lustre
 * @since 2023/3/16 0:28
 */
public class PioBuildTypeStatusBarWidget extends EditorBasedWidget implements StatusBarWidget.Multiframe, CustomStatusBarWidget, BuildTypeChangeListener {

    private final TextPanel.WithIconAndArrows myComponent;

    private final Project project;

    public PioBuildTypeStatusBarWidget(Project project) {
        super(project);
        this.project = project;
        myComponent = new TextPanel.WithIconAndArrows();
        myComponent.setBorder(JBUI.CurrentTheme.StatusBar.Widget.border());
        myComponent.setIcon(IconLoader.getIcon("/pioplus/pio_cmake.svg", getClass()));
        PlatformIoPlusService service = project.getService(PlatformIoPlusService.class);
        service.registerUIComponent(this::setEnable);
        service.setCurrentCmakeBuildTypeListener(this);
    }

    @Override
    public @NotNull @NonNls String ID() {
        return $sys("pio.cmake.build.type.widget.id");
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        super.install(statusBar);

        myComponent.setToolTipText($sys("pio.cmake.build.type.widget.name"));
        new ClickListener() {
            @Override
            public boolean onClick(@NotNull MouseEvent event, int clickCount) {
                showSelectBuildTypePopup();
                return true;
            }
        }.installOn(myComponent, true);
        update();
    }

    private void setEnable(boolean enable) {
        myComponent.setVisible(enable);
    }

    @Override
    public void dispose() {
        setEnable(false);
    }


    private void showSelectBuildTypePopup() {
        CMakeWorkspace cmakeWorkspace = CMakeWorkspace.getInstance(project);
        List<CMakeModelConfigurationData> cMakeModelConfigurationData = cmakeWorkspace.getModelConfigurationData();
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        if (!cMakeModelConfigurationData.isEmpty()) {
            List<String> buildTypes = cMakeModelConfigurationData.get(0).getRegisteredBuildTypes();
            for (String buildType : buildTypes) {
                actionGroup.add(new CheckBuildTypeAction(buildType, this));
            }
        }
        JComponent component = getComponent();
        DataContext dataContext = DataManager.getInstance().getDataContext(component);
        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup($sys("pio.cmake.build.type.widget.title"),
                actionGroup, dataContext, JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
        RelativePoint pos = JBPopupFactory.getInstance().guessBestPopupLocation(component);
        popup.showInScreenCoordinates(component, pos.getScreenPoint());
    }


    @NotNull
    @Override
    public StatusBarWidget copy() {
        return new PioBuildTypeStatusBarWidget(getProject());
    }

    @Override
    public JComponent getComponent() {
        return myComponent;
    }

    @Override
    public void update() {
        CMakeWorkspace cmakeWorkspace = CMakeWorkspace.getInstance(project);
        List<CMakeSettings.Profile> profiles = cmakeWorkspace.getSettings().getProfiles();
        if (!profiles.isEmpty()) {
            myComponent.setText(profiles.get(0).getBuildType());
            myComponent.setVisible(true);
        }
    }

    /**
     * 当使用settings更新了buildType,状态栏也要更新
     */
    public static class UpdateHook implements CMakeSettingsListener {
        @NotNull
        private final Project myProject;

        public UpdateHook(@NotNull Project project) {
            myProject = project;
        }

        @Override
        public void profilesChanged(@NotNull List<CMakeSettings.Profile> old, @NotNull List<CMakeSettings.Profile> current) {
            PlatformIoPlusService service = myProject.getService(PlatformIoPlusService.class);
            if (service != null) {
                service.updateBuildType();
            }
        }
    }
}
