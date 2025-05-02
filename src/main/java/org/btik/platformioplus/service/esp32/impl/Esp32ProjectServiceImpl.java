package org.btik.platformioplus.service.esp32.impl;

import com.intellij.openapi.project.Project;
import org.btik.platformioplus.run.config.esp32.debug.build.Esp32BuildTarget;
import org.btik.platformioplus.service.esp32.Esp32ProjectService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author lustre
 * @since 2025/4/26 18:11
 */
public class Esp32ProjectServiceImpl implements Esp32ProjectService {
    private final Esp32BuildTarget esp32BuildTarget;

    private final Project project;

    public Esp32ProjectServiceImpl(Project project) {
        this.project = project;
        this.esp32BuildTarget = new Esp32BuildTarget(project.getName());
    }

    @Override
    public @NotNull List<Esp32BuildTarget> getBuildTargets() {
        return List.of(esp32BuildTarget);
    }
}
