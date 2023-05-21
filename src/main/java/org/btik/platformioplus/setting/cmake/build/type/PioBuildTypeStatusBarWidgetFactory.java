package org.btik.platformioplus.setting.cmake.build.type;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.btik.platformioplus.service.PlatformIoPlusService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static org.btik.platformioplus.util.SysConf.$sys;

/**
 * @author lustre
 * @since 2023/3/16 0:21
 */
public class PioBuildTypeStatusBarWidgetFactory implements StatusBarWidgetFactory {

    @Override
    public @NotNull @NonNls String getId() {
        return $sys("pio.cmake.build.type.widget.factory.id");
    }

    @Override
    public @NotNull @NlsContexts.ConfigurableName String getDisplayName() {
        return $sys("pio.cmake.build.type.widget.factory.name");
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        PlatformIoPlusService service = project.getService(PlatformIoPlusService.class);
        return service != null;
    }

    @Override
    public @NotNull StatusBarWidget createWidget(@NotNull Project project) {
        return new PioBuildTypeStatusBarWidget(project);
    }

}
