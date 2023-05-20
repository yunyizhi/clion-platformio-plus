package org.btik.platformioplus.run.config;

import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.TextFieldWithStoredHistory;
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

    private TextFieldWithStoredHistory textFiled;

    public PioPlusSettingEditor(Project project) {

    }

    @Override
    protected void resetEditorFrom(@NotNull PioPlusRunConfig config) {
        envComponent.setEnvData(config.getEnvData());
        arguments.setText(config.getArguments());
    }

    @Override
    protected void applyEditorTo(@NotNull PioPlusRunConfig config) throws ConfigurationException {
        config.setEnvData(envComponent.getEnvData());
        config.setArguments(arguments.getText());
    }

    @Override
    protected @NotNull JComponent createEditor() {
        return rootPanel;
    }
}
