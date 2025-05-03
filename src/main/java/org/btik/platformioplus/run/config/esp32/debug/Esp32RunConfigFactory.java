package org.btik.platformioplus.run.config.esp32.debug;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.btik.platformioplus.icon.PlatformIoPlusIcon;
import org.btik.platformioplus.ini.PioIniSectionBean;
import org.btik.platformioplus.run.config.PioPlusRunConfigType;
import org.btik.platformioplus.run.config.esp32.debug.model.DebugConfigModel;
import org.btik.platformioplus.run.config.esp32.system.Esp32DebugSysConf;
import org.btik.platformioplus.service.PlatformIoIniStore;
import org.btik.platformioplus.service.SystemMetaService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.nio.file.Path;
import java.util.Objects;

import static org.btik.platformioplus.ini.PioIniMetaConst.MCU;
import static org.btik.platformioplus.run.config.esp32.system.Esp32DebugSysConfMeta.TARGET_DEFAULT;
import static org.btik.platformioplus.util.SysConf.$sys;

/**
 * @author lustre
 * @since 2024/9/2 21:11
 */
public class Esp32RunConfigFactory extends ConfigurationFactory {

    private final static Logger log = Logger.getInstance(Esp32RunConfigFactory.class);

    public Esp32RunConfigFactory(PioPlusRunConfigType esp32RunConfigType) {
        super(esp32RunConfigType);
    }

    @Override
    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new Esp32RunConfig(project, this);
    }

    @Override
    public Icon getIcon() {
        return PlatformIoPlusIcon.ESP32_16;
    }

    @Override
    public @NotNull @NonNls String getId() {
        return $sys("esp32.run.config.type.factory.id");
    }

    @Override
    public @NotNull @Nls String getName() {
        return $sys("esp32.debug.name");
    }

    public static DebugConfigModel getDebugConfigModel(Project project) {
        PlatformIoIniStore service = project.getService(PlatformIoIniStore.class);
        SystemMetaService sysConfService = ApplicationManager.getApplication().getService(SystemMetaService.class);
        PioIniSectionBean currentSection = service.getCurrentSection();
        if (currentSection == null) {
            return null;
        }
        String mcu = currentSection.getProperties().get(MCU);
        if (StringUtil.isEmpty(mcu)) {
            return null;
        }
        Esp32DebugSysConf esp32DebugSysConf = sysConfService.getEsp32DebugSysConf();
        var targetConfig = esp32DebugSysConf.getTargetConfig(mcu);
        if (targetConfig == null) {
            targetConfig = esp32DebugSysConf.getTargetConfig(TARGET_DEFAULT);
        }
        if (targetConfig == null) {
            return null;
        }
        DebugConfigModel debugConfigModel = new DebugConfigModel();
        String openocdCfg = targetConfig.getOpenocdCfg();
        if (Objects.equals(debugConfigModel.getTarget(), TARGET_DEFAULT)) {
            openocdCfg = String.format(openocdCfg, mcu);
        }
        debugConfigModel.setOpenOcdArguments(openocdCfg);
        debugConfigModel.setOpenOcdPath(esp32DebugSysConf.getOpenocdBinPath());
        debugConfigModel.setGdbExe(targetConfig.getGdbPath());
        String basePath = project.getBasePath();
        if (basePath == null) {
            return debugConfigModel;
        }
        Path baseDir = Path.of(basePath);
        debugConfigModel.setAppElf(baseDir.resolve($sys("platformio.out.dir")).resolve($sys("platformio.out.build.dir"))
                .resolve(currentSection.getEnvName()).resolve($sys("platformio.out.elf")).toString());
        return debugConfigModel;
    }
}
