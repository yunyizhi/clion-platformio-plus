package org.btik.platformioplus.service;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.AsyncFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.PathUtilRt;
import org.btik.platformioplus.ini.completion.PlatformioIniMetaFactory;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author lustre
 * @since 2022/10/23 16:50
 */
public class PlatformIoPlusInit implements StartupActivity, AsyncFileListener {

    @Override
    public void runActivity(@NotNull Project project) {
        PlatformioIniMetaFactory.INSTANCE.load();
        setEnable(project);
    }

    private static void setEnable(@NotNull Project project) {
        boolean enabled =
                Stream.of(ProjectRootManager.getInstance(project).getContentRoots())
                        .anyMatch(root -> root.findChild(PioConf.FILE_NAME) != null);
        PlatformIoPlusService service = project.getService(PlatformIoPlusService.class);
        service.setEnable(enabled);
    }

    @Override
    public @Nullable ChangeApplier prepareChange(@NotNull List<? extends @NotNull VFileEvent> events) {
        for (VFileEvent event : events) {
            String path = event.getPath();
            if (PioConf.FILE_NAME.equals(PathUtilRt.getFileName(path))) {
                return new ChangeApplier() {
                    @Override
                    public void afterVfsChange() {
                        Arrays.stream(ProjectManager.getInstance().getOpenProjects())
                                .forEach(PlatformIoPlusInit::setEnable);
                    }
                };
            }
        }

        return null;
    }
}
