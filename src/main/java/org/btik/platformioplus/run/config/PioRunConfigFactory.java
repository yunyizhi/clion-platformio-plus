package org.btik.platformioplus.run.config;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static org.btik.platformioplus.util.SysConf.$sys;


/**
 * @author lustre
 * @since 2023/5/18 1:53
 */
public class PioRunConfigFactory extends ConfigurationFactory {

    public PioRunConfigFactory(@NotNull ConfigurationType type) {
        super(type);
    }

    @Override
    public @NotNull @NonNls String getId() {
        return $sys("pio.plus.run.config.type.factory.id");
    }

    @Override
    public boolean isEditableInDumbMode() {
        return true;
    }

    @Override
    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new PioPlusRunConfig(project, this);
    }
}
