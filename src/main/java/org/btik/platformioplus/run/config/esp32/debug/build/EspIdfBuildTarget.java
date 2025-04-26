package org.btik.platformioplus.run.config.esp32.debug.build;

import com.jetbrains.cidr.execution.CidrBuildTarget;

import org.btik.platformioplus.icon.PlatformIoPlusIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

import static org.btik.platformioplus.util.Note.$i18n;

/**
 * @author lustre
 * @since 2024/9/3 0:07
 */
public class EspIdfBuildTarget implements CidrBuildTarget<EspIdfBuildConf> {
    private final String projectName;
    private final EspIdfBuildConf espIdfBuildConf;

    public EspIdfBuildTarget(String projectName) {
        this.projectName = projectName;
        this.espIdfBuildConf = new EspIdfBuildConf();
    }

    @Override
    public @NotNull String getName() {
        return $i18n("esp.idf.debug.type");
    }

    @Override
    public @NotNull String getProjectName() {
        return projectName;
    }

    @Override
    public @Nullable Icon getIcon() {
        return PlatformIoPlusIcon.PIOPLUS_13;
    }

    @Override
    public boolean isExecutable() {
        return true;
    }

    @Override
    public @NotNull List<EspIdfBuildConf> getBuildConfigurations() {
        return List.of(espIdfBuildConf);
    }
}
