package org.btik.platformioplus.setting.cmake.build.type;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.IconLikeCustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.util.Consumer;
import org.btik.platformioplus.util.SysConf;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * @author lustre
 * @since 2023/3/16 0:28
 */
public class PioBuildTypeStatusBarWidget extends JLabel implements CustomStatusBarWidget, IconLikeCustomStatusBarWidget {
    public PioBuildTypeStatusBarWidget(Project project) {

    }

    @Override
    public @NotNull @NonNls String ID() {
        return SysConf.get("pio.cmake.build.type.widget.id");
    }

    @Override
    public void install(@NotNull StatusBar statusBar) {
        setIcon(IconLoader.getIcon("/pioplus/pio_cmake.svg", getClass()));
    }

    @Override
    public void dispose() {

    }


    @Override
    public JComponent getComponent() {
        return this;
    }
}
