package org.btik.platformioplus.run.config;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configuration.EnvironmentVariablesData;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfigurationBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author lustre
 * @since 2023/5/18 1:54
 */
public class PioPlusRunConfig extends LocatableConfigurationBase {

    private EnvironmentVariablesData envData;
    private String arguments;

    public void setEnvData(EnvironmentVariablesData envData) {
        this.envData = envData;
    }

    public PioPlusRunConfig(@NotNull Project project, @NotNull ConfigurationFactory factory) {
        super(project, factory);
    }
    @Override
    public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
         return new PioPlusSettingEditor(getProject());
    }

    @Override
    public @Nullable RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return new PioPlusRunTaskProfile(environment.getProject(), this);
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
