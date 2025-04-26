package org.btik.platformioplus.run.config.esp32.debug;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static org.btik.platformioplus.util.Note.$i18n;
import static org.btik.platformioplus.util.SysConf.$sys;

/**
 * @author lustre
 * @since 2024/9/2 20:57
 */
public class Esp32RunConfigType implements ConfigurationType {
    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return $sys("esp.idf.debug.type.display.name");
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) String getConfigurationTypeDescription() {
        return $i18n("esp.idf.debug.type");
    }

    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/pioplus/platformio_13.svg", getClass());
    }

    @Override
    public @NotNull @NonNls String getId() {
        return $sys("pio.plus.debug.type.id");
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{
                new Esp32RunConfigFactory(this)
        };
    }

}
