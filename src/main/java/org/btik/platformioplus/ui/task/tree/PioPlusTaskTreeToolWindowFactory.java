package org.btik.platformioplus.ui.task.tree;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.btik.platformioplus.service.PlatformIoPlusService;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * @author lustre
 * @since 2022/10/7 21:44
 */
public class PioPlusTaskTreeToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        PioPlusTaskTreeToolWindow pioPlusTaskTreeToolWindow = new PioPlusTaskTreeToolWindow(project);
        ContentFactory contentFactory =  ApplicationManager.getApplication().getService(ContentFactory.class);
        Content content = contentFactory.createContent(pioPlusTaskTreeToolWindow.getContent(), "Tasks", false);
        toolWindow.getContentManager().addContent(content);

        PlatformIoPlusService service = project.getService(PlatformIoPlusService.class);
        toolWindow.setAvailable(service != null && service.isEnable());
        if (service != null) {
            service.registerUIComponent(toolWindow::setAvailable);
        }
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return Stream.of(ProjectRootManager.getInstance(project).getContentRoots())
                .anyMatch(root -> root.findChild(PioConf.FILE_NAME) != null);
    }
}
