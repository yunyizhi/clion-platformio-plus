package org.btik.platformioplus.run.config.esp32.debug;

import com.google.gson.Gson;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.jetbrains.cidr.cpp.cmake.CMakeSettings;
import com.jetbrains.cidr.cpp.cmake.workspace.CMakeWorkspace;
import org.btik.platformioplus.icon.PlatformIoPlusIcon;
import org.btik.platformioplus.run.config.PioPlusRunConfigType;
import org.btik.platformioplus.run.config.esp32.debug.model.DebugConfigModel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

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

    private static DebugConfigModel parseDesc(File descFile) {
        Gson gson = new Gson();
        String json;
        try {
            json = Files.readString(descFile.toPath());
        } catch (IOException e) {
            log.error(e);
            return null;
        }
        return gson.fromJson(json, DebugConfigModel.class);
    }

    public static File getFileInCmakeBuildDir(Project project, final String fileName) {
        CMakeWorkspace instance = CMakeWorkspace.getInstance(project);
        CMakeSettings settings = instance.getSettings();
        List<CMakeSettings.Profile> profiles = settings.getProfiles();
        String basePath = project.getBasePath();
        if (basePath == null) {
            return null;
        }
        Path baseDir = Path.of(basePath);

        if (profiles.isEmpty()) {
            return checkDescFile(baseDir.resolve($sys("esp32.build.project.build.dir")), fileName);
        }
        File resolve;
        for (CMakeSettings.Profile profile : profiles) {
            File generationDir = profile.getGenerationDir();
            if (generationDir != null && (resolve = checkDescFile(baseDir.resolve(generationDir.getName()), fileName)) != null) {
                return resolve;
            }
        }
        return null;
    }

    private static File checkDescFile(Path buildDir, final String fileName) {
        if (!Files.exists(buildDir)) {
            return null;
        }
        if (Objects.equals("/", fileName)) {
            return buildDir.toFile();
        }
        File projectDesc = buildDir.resolve(fileName).toFile();
        return projectDesc.exists() && projectDesc.canRead() ? projectDesc : null;

    }

    public static DebugConfigModel syncProjectDesc(Project project) {
        String projectDescFileName = $sys("esp32.build.project.description");
        File projectDescFile = getFileInCmakeBuildDir(project, projectDescFileName);
        if (projectDescFile == null) {
            return null;
        }
        return parseDesc(projectDescFile);
    }
}
