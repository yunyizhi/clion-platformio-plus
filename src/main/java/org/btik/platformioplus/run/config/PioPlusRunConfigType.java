package org.btik.platformioplus.run.config;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static org.btik.platformioplus.util.SysConf.$sys;

/**
 * @author lustre
 * @since 2023/5/10 22:46
 */
public class PioPlusRunConfigType implements ConfigurationType {

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return $sys("pio.plus.run.config.type");
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) String getConfigurationTypeDescription() {
        return $sys("pio.plus.run.config.type.desc");
    }

    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/pioplus/platformio_13.svg", getClass());
    }

    @Override
    public @NotNull @NonNls String getId() {
        return $sys("pio.plus.run.config.type.id");
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{
                new PioRunConfigFactory(this)
        };
    }
}
