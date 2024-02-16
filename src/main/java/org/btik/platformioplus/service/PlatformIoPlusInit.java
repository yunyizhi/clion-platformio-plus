package org.btik.platformioplus.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.openapi.vfs.AsyncFileListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.openapi.vfs.newvfs.events.VFilePropertyChangeEvent;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.util.PathUtilRt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.btik.platformioplus.ini.completion.PlatformioIniMetaFactory;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.btik.platformioplus.service.PlatformIoPlusService.WINDOW_ARRAY;

/**
 * @author lustre
 * @since 2022/10/23 16:50
 */
public class PlatformIoPlusInit implements ProjectActivity, AsyncFileListener {

    private static void setEnable(@NotNull Project project) {
        boolean enabled =
                Stream.of(ProjectRootManager.getInstance(project).getContentRoots())
                        .anyMatch(root -> root.findChild(PioConf.FILE_NAME) != null);
        for (String id : WINDOW_ARRAY) {
            ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(id);
            if(toolWindow != null) {
                toolWindow.setAvailable(enabled);
            }
        }
    }

    @Override
    public @Nullable ChangeApplier prepareChange(@NotNull List<? extends @NotNull VFileEvent> events) {
        for (VFileEvent event : events) {
            String path = event.getPath();
            if (PioConf.FILE_NAME.equals(PathUtilRt.getFileName(path)) || isRenameToPioIni(event)) {
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

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        PlatformioIniMetaFactory.INSTANCE.load();
        ApplicationManager.getApplication().invokeLater(() -> setEnable(project));
        return null;
    }

    private static boolean isRenameToPioIni(VFileEvent event) {
        if (!(event instanceof VFilePropertyChangeEvent filePropertyChangeEvent )){
            return false;
        }
        return  PioConf.FILE_NAME.equals(PathUtilRt.getFileName(filePropertyChangeEvent.getNewPath()));
    }
}
