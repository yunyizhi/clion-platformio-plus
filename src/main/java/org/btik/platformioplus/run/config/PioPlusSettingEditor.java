package org.btik.platformioplus.run.config;

import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author lustre
 * @since 2023/5/18 10:51
 */
public class PioPlusSettingEditor extends SettingsEditor<PioPlusRunConfig> {
    private JPanel rootPanel;

    private EnvironmentVariablesComponent envComponent;
    private JTextField arguments;

    public PioPlusSettingEditor() {

    }

    @Override
    protected void resetEditorFrom(@NotNull PioPlusRunConfig config) {
        envComponent.setEnvData(config.getEnvData());
        arguments.setText(config.getArguments());
    }

    @Override
    protected void applyEditorTo(@NotNull PioPlusRunConfig config) {
        config.setEnvData(envComponent.getEnvData());
        config.setArguments(arguments.getText());
    }

    @Override
    protected @NotNull JComponent createEditor() {
        return rootPanel;
    }

    private void createUIComponents() {
        envComponent = new EnvironmentVariablesComponent();
        arguments = new JTextField();

    }
}
