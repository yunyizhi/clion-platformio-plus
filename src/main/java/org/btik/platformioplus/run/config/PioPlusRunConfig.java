package org.btik.platformioplus.run.config;

import com.intellij.execution.Executor;
import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.execution.configuration.EnvironmentVariablesData;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.btik.platformioplus.util.Note.$note;

/**
 * @author lustre
 * @since 2023/5/18 1:54
 */
public class PioPlusRunConfig extends LocatableConfigurationBase {

    private EnvironmentVariablesData envData = EnvironmentVariablesData.DEFAULT;

    private static final String PIO_ARGUMENTS = "PIO_ARGUMENTS";
    private String arguments;

    public void setEnvData(EnvironmentVariablesData envData) {
        this.envData = envData;
    }

    public PioPlusRunConfig(@NotNull Project project, @NotNull ConfigurationFactory factory) {
        super(project, factory);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if (arguments == null || arguments.isEmpty()) {
            throw new RuntimeConfigurationException($note("pio.plus.run.config.arguments.required"));
        }
    }

    @Override
    public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new PioPlusSettingEditor();
    }

    @Override
    public @Nullable RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
        return new PioPlusRunTaskProfileState(this, environment);
    }

    @Override
    public void readExternal(@NotNull Element element) throws InvalidDataException {
        super.readExternal(element);
        envData = EnvironmentVariablesData.readExternal(element);
        arguments = JDOMExternalizerUtil.readField(element, PIO_ARGUMENTS);
    }

    @Override
    public void writeExternal(@NotNull Element element) {
        super.writeExternal(element);
        EnvironmentVariablesComponent.writeExternal(element, envData.getEnvs());
        JDOMExternalizerUtil.writeField(element, PIO_ARGUMENTS, arguments);
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getArguments() {
        return arguments;
    }

    public EnvironmentVariablesData getEnvData() {
        return envData;
    }
}
