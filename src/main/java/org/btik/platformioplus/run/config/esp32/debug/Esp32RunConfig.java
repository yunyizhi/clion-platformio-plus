package org.btik.platformioplus.run.config.esp32.debug;

import com.intellij.execution.ExecutionTarget;
import com.intellij.execution.Executor;
import com.intellij.execution.configuration.EnvironmentVariablesComponent;
import com.intellij.execution.configuration.EnvironmentVariablesData;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.jetbrains.cidr.cpp.execution.CLionRunConfiguration;
import com.jetbrains.cidr.execution.CidrBuildConfigurationHelper;
import com.jetbrains.cidr.execution.CidrCommandLineState;
import com.jetbrains.cidr.execution.ExecutableData;
import com.jetbrains.cidr.lang.workspace.OCResolveConfiguration;

import org.btik.platformioplus.run.config.esp32.debug.build.Esp32BuildTarget;
import org.btik.platformioplus.run.config.esp32.debug.build.EspIdfBuildConf;
import org.btik.platformioplus.run.config.esp32.debug.build.EspIdfBuildConfHelper;
import org.btik.platformioplus.run.config.esp32.debug.model.DebugConfigModel;
import org.btik.platformioplus.service.SystemMetaService;
import org.btik.platformioplus.util.ClassMetaUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static org.btik.platformioplus.util.SysConf.$sys;


/**
 * @author lustre
 * @since 2024/9/2 21:17
 */
public class Esp32RunConfig extends CLionRunConfiguration<EspIdfBuildConf, Esp32BuildTarget> {

    private ExecutableData executableData;
    private DebugConfigModel configDataModel;
    private int historyConfigModelHash = 0;
    private int thisHistoryHash = 0;

    public Esp32RunConfig(Project project, ConfigurationFactory factory) {
        super(project, factory, $sys("esp32.debug.name"));
    }

    @Override
    public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new Esp32DebugSettingEditor(getProject());
    }

    @Override
    public void readExternal(@NotNull Element element) throws InvalidDataException {
        super.readExternal(element);
        var historyConfigDataModel = new DebugConfigModel();
        SystemMetaService sysConfService = ApplicationManager.getApplication().getService(SystemMetaService.class);
        List<ClassMetaUtils.PropOptMeta> propOptMetas = sysConfService.getEsp32PropOptMetas();
        for (ClassMetaUtils.PropOptMeta propOptMeta : propOptMetas) {
            Class<?> aClass = ClassMetaUtils.propType(propOptMeta);
            if (aClass == String.class) {
                String stringValue = JDOMExternalizerUtil.readField(element, propOptMeta.propName());
                ClassMetaUtils.set(propOptMeta, historyConfigDataModel, stringValue);
                continue;
            }
            if (aClass == EnvironmentVariablesData.class) {
                EnvironmentVariablesData environmentVariablesData = EnvironmentVariablesData.readExternal(element);
                ClassMetaUtils.set(propOptMeta, historyConfigDataModel, environmentVariablesData);
            }

        }
        if (configDataModel == null || Objects.equals(historyConfigDataModel.getTarget(), configDataModel.getTarget())) {
            setConfigDataModel(historyConfigDataModel);
            this.historyConfigModelHash = System.identityHashCode(historyConfigDataModel);
            this.thisHistoryHash = System.identityHashCode(this);
        }
    }

    @Override
    public void writeExternal(@NotNull Element element) {
        super.writeExternal(element);
        if (configDataModel == null) {
            return;
        }
        final DebugConfigModel dataModel = configDataModel;
        SystemMetaService sysConfService = ApplicationManager.getApplication().getService(SystemMetaService.class);
        List<ClassMetaUtils.PropOptMeta> propOptMetas = sysConfService.getEsp32PropOptMetas();
        for (ClassMetaUtils.PropOptMeta propOptMeta : propOptMetas) {
            Class<?> aClass = ClassMetaUtils.propType(propOptMeta);
            if (aClass == String.class) {
                String stringValue = ClassMetaUtils.get(propOptMeta, dataModel);
                JDOMExternalizerUtil.writeField(element, propOptMeta.propName(), stringValue);
                continue;
            }
            if (aClass == EnvironmentVariablesData.class) {
                EnvironmentVariablesData environmentVariablesData = ClassMetaUtils.get(propOptMeta, dataModel);
                if (environmentVariablesData == null) {
                    continue;
                }
                EnvironmentVariablesComponent.writeExternal(element, environmentVariablesData.getEnvs());
            }

        }

    }

    @Override
    public @NotNull CidrBuildConfigurationHelper<EspIdfBuildConf, Esp32BuildTarget> getHelper() {
        return new EspIdfBuildConfHelper(getProject());
    }

    @Override
    public @Nullable OCResolveConfiguration getResolveConfiguration(@NotNull ExecutionTarget executionTarget) {
        return null;
    }

    @Override
    public @Nullable ExecutableData getExecutableData() {
        return executableData;
    }

    @Override
    public void setExecutableData(@Nullable ExecutableData executableData) {
        this.executableData = executableData;
    }

    @Override
    public @Nullable CommandLineState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) {
        Esp32Launcher esp32Launcher = new Esp32Launcher(executionEnvironment, this);
        return new CidrCommandLineState(executionEnvironment, esp32Launcher);
    }


    public DebugConfigModel getConfigDataModel() {
        return configDataModel;
    }

    public void setConfigDataModel(DebugConfigModel configDataModel) {
        this.configDataModel = configDataModel;
    }

    public boolean isFromHistory() {
        if (configDataModel == null) {
            return false;
        }
        if (System.identityHashCode(this) != this.thisHistoryHash) {
            return false;
        }
        return historyConfigModelHash != 0 && historyConfigModelHash == System.identityHashCode(configDataModel);
    }
}
