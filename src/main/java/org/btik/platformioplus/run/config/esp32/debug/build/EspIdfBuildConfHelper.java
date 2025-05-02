package org.btik.platformioplus.run.config.esp32.debug.build;

import com.intellij.openapi.project.Project;
import com.jetbrains.cidr.execution.CidrBuildConfigurationHelper;
import org.btik.platformioplus.service.esp32.Esp32ProjectService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author lustre
 * @since 2024/9/3 0:28
 */
public class EspIdfBuildConfHelper extends CidrBuildConfigurationHelper<EspIdfBuildConf, Esp32BuildTarget> {

    private final Project project;

    public EspIdfBuildConfHelper(Project project) {
        this.project = project;
    }

    @Override
    public @NotNull List<Esp32BuildTarget> getTargets() {
        return project.getService(Esp32ProjectService.class).getBuildTargets();
    }
}
