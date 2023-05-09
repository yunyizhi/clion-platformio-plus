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
import com.jetbrains.cidr.cpp.cmake.model.CMakeModelConfigurationData;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;
import org.btik.platformioplus.service.PlatformIoPlusService;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * @author lustre
 * @since 2023/3/16 0:28
 */
public class PioBuildTypeStatusBarWidget extends EditorBasedWidget implements StatusBarWidget.Multiframe, CustomStatusBarWidget {

    private final TextPanel.WithIconAndArrows myComponent;

    public PioBuildTypeStatusBarWidget(Project project) {
        super(project);
        myComponent = new TextPanel.WithIconAndArrows();
        myComponent.setBorder(JBUI.CurrentTheme.StatusBar.Widget.border());
        myComponent.setIcon(IconLoader.getIcon("/pioplus/pio_cmake.svg", getClass()));
        PlatformIoPlusService service = project.getService(PlatformIoPlusService.class);
        service.registerUIComponent(this::setEnable);
    }

    @Override
    public @NotNull @NonNls String ID() {
        return SysConf.get("pio.cmake.build.type.widget.id");
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        super.install(statusBar);
        myComponent.setText("pioCmake");
        myComponent.setVisible(true);
        myComponent.setToolTipText(SysConf.get("pio.cmake.build.type.widget.name"));
        new ClickListener() {
            @Override
            public boolean onClick(@NotNull MouseEvent event, int clickCount) {
                showSelectBuildTypePopup();
                return true;
            }
        }.installOn(myComponent, true);
    }

    private void setEnable(boolean enable) {
        myComponent.setVisible(enable);
    }

    @Override
    public void dispose() {
        setEnable(false);
    }


    private void showSelectBuildTypePopup(){
        SelectBuildTypeAction selectBuildTypeAction = new SelectBuildTypeAction();
        JComponent where = getComponent();
        DataContext dataContext = DataManager.getInstance().getDataContext(where);
        DefaultActionGroup popupActionGroup = selectBuildTypeAction.createPopupActionGroup(where, dataContext);
        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup("BuildType",
                popupActionGroup, dataContext, JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);
        RelativePoint pos = JBPopupFactory.getInstance().guessBestPopupLocation(where);
        popup.showInScreenCoordinates(where, pos.getScreenPoint());
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
}
