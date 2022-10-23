package org.btik.platformioplus.service;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.startup.StartupActivity;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * @author lustre
 * @since 2022/10/23 16:50
 */
public class PlatformIoPlusInit implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        boolean enabled =
                Stream.of(ProjectRootManager.getInstance(project).getContentRoots())
                        .anyMatch(root -> root.findChild(PioConf.FILE_NAME) != null);
        PlatformIoPlusService service = project.getService(PlatformIoPlusService.class);
        service.setEnable(enabled);
    }
}
