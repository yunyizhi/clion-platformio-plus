package org.btik.platformioplus.run.config.esp32.debug;

import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import org.btik.platformioplus.ini.PioIniSectionBean;
import org.btik.platformioplus.run.config.esp32.components.TextFieldFileChooser;
import org.btik.platformioplus.run.config.esp32.debug.model.DebugConfigModel;
import org.btik.platformioplus.service.PlatformIoIniStore;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.btik.platformioplus.util.Note.$i18n;
import static org.btik.platformioplus.util.UIUtils.createConstraints;
import static org.btik.platformioplus.util.UIUtils.i18nLabel;

/**
 * @author lustre
 * @since 2024/9/2 22:26
 */
public class Esp32DebugSettingEditor extends SettingsEditor<Esp32RunConfig> {

    private final JPanel rootPanel;

    private final EnvironmentVariablesComponent envComponent;

    private final TextFieldFileChooser openocdPath;
    private final JTextField openOcdArguments = new JTextField();
    private final TextFieldFileChooser appElf;
    private final TextFieldFileChooser gdb;
    private final JButton setDefault = new JButton();
    private final Project project;

    public Esp32DebugSettingEditor(@NotNull Project project) {
        this.project = project;
        rootPanel = new JPanel(new VerticalFlowLayout(0, 2));
        envComponent = new EnvironmentVariablesComponent();
        envComponent.getLabel().setVisible(false);
        JPanel wrapper = new JPanel(new GridLayoutManager(7, 2, JBUI.insetsTop(16), -1, -1));

        int rowIndex = 0;
        wrapper.add(i18nLabel("esp32.debug.openocd.path"), createConstraints(rowIndex, 0));
        GridConstraints openocdPathConstraints = createConstraints(rowIndex, 1);
        openocdPathConstraints.setFill(GridConstraints.FILL_HORIZONTAL);
        openocdPathConstraints.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        openocdPath = new TextFieldFileChooser();
        openocdPath.addActionListener(project, FileChooserDescriptorFactory.singleFile(), $i18n("select.openocd.path"), $i18n("select.openocd.path.for.esp32"));
        wrapper.add(openocdPath, openocdPathConstraints);
        rowIndex++;

        wrapper.add(i18nLabel("esp32.debug.openocd.arguments"), createConstraints(rowIndex, 0));
        GridConstraints openocdArgConstraints = createConstraints(rowIndex, 1);
        openocdArgConstraints.setFill(GridConstraints.FILL_HORIZONTAL);
        openocdArgConstraints.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        wrapper.add(openOcdArguments, openocdArgConstraints);
        rowIndex++;

        wrapper.add(i18nLabel("esp32.debug.app_elf"), createConstraints(rowIndex, 0));
        GridConstraints appElfConstraints = createConstraints(rowIndex, 1);
        appElfConstraints.setFill(GridConstraints.FILL_HORIZONTAL);
        appElfConstraints.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        appElf = new TextFieldFileChooser();
        appElf.addActionListener(project, newElfFileChooser(), $i18n("select.elf.path"), $i18n("select.elf.path.for.esp32"));
        wrapper.add(appElf, appElfConstraints);
        rowIndex++;

        wrapper.add(i18nLabel("esp32.debug.gdb"), createConstraints(rowIndex, 0));
        GridConstraints gdbArgConstraints = createConstraints(rowIndex, 1);
        gdbArgConstraints.setFill(GridConstraints.FILL_HORIZONTAL);
        gdbArgConstraints.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        gdb = new TextFieldFileChooser();
        FileChooserDescriptor gdbChooser = new FileChooserDescriptor(true, false, false, false, false, false);
        gdb.addActionListener(project, gdbChooser, $i18n("select.esp.gdb.path"), $i18n("select.esp.gdb.path"));
        wrapper.add(gdb, gdbArgConstraints);
        rowIndex++;

        wrapper.add(i18nLabel("esp32.debug.environment.variables"), createConstraints(rowIndex, 0));
        GridConstraints envConstraints = createConstraints(rowIndex, 1);
        envConstraints.setFill(GridConstraints.FILL_HORIZONTAL);
        envConstraints.setHSizePolicy(GridConstraints.SIZEPOLICY_WANT_GROW);
        wrapper.add(envComponent, envConstraints);
        rowIndex++;

        wrapper.add(new JLabel(""), createConstraints(rowIndex, 0));
        wrapper.add(setDefault, createConstraints(rowIndex, 1));
        setDefault.setText($i18n("esp32.debug.set.default"));

        rootPanel.add(wrapper, BorderLayout.CENTER);

        bindAction();
    }


    private void bindAction() {
        setDefault.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    setDefault.setEnabled(false);
                    initValue();
                } finally {
                    setDefault.setEnabled(true);
                }
            }
        });
    }

    private void initValue() {
        DebugConfigModel debugConfigModel = Esp32RunConfigFactory.getDebugConfigModel(project);
        if (debugConfigModel == null) {
            return;
        }
        setDefault(debugConfigModel);
    }

    private void setDefault(@NotNull DebugConfigModel debugConfigModel) {
        openocdPath.setText(debugConfigModel.getOpenOcdPath());
        appElf.setText(debugConfigModel.getAppElf());
        gdb.setText(debugConfigModel.getGdbExe());
        openOcdArguments.setText(debugConfigModel.getOpenOcdArguments());
        String target = debugConfigModel.getTarget();
    }

    @Override
    protected void resetEditorFrom(@NotNull Esp32RunConfig esp32RunConfig) {
        DebugConfigModel configDataModel = esp32RunConfig.getConfigDataModel();
        if (configDataModel == null) {
            initValue();
            return;
        }
        envComponent.setEnvData(configDataModel.getEnvData());
        openOcdArguments.setText(configDataModel.getOpenOcdArguments());
        appElf.setText(configDataModel.getAppElf());
        gdb.setText(configDataModel.getGdbExe());
        openocdPath.setText(configDataModel.getOpenOcdPath());
    }

    @Override
    protected void applyEditorTo(@NotNull Esp32RunConfig esp32RunConfig) {
        DebugConfigModel configDataModel = esp32RunConfig.getConfigDataModel();
        DebugConfigModel debugConfigModel = configDataModel == null ? new DebugConfigModel() : configDataModel;
        esp32RunConfig.setConfigDataModel(debugConfigModel);
        debugConfigModel.setAppElf(appElf.getText());
        debugConfigModel.setOpenOcdArguments(openOcdArguments.getText());
        debugConfigModel.setGdbExe(gdb.getText());
        debugConfigModel.setEnvData(envComponent.getEnvData());
        debugConfigModel.setOpenOcdPath(openocdPath.getText());
    }

    @Override
    protected @NotNull JComponent createEditor() {
        return rootPanel;
    }

    private FileChooserDescriptor newElfFileChooser() {
        return FileChooserDescriptorFactory.createSingleFileDescriptor("elf");
    }

}
