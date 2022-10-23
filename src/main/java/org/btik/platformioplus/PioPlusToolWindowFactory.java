package org.btik.platformioplus;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.btik.platformioplus.service.PlatformIoPlusService;
import org.btik.platformioplus.ui.PioPlusToolWindow;
import org.jetbrains.annotations.NotNull;

/**
 * @author lustre
 * @since 2022/10/7 21:44
 */
public class PioPlusToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        PioPlusToolWindow pioPlusToolWindow = new PioPlusToolWindow();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(pioPlusToolWindow.getContent(), "Tasks", false);
        toolWindow.getContentManager().addContent(content);
        PlatformIoPlusService service = project.getService(PlatformIoPlusService.class);
        toolWindow.setAvailable(service != null && service.isEnable());
        if (service != null) {
            service.registerUIComponent(toolWindow::setAvailable);
        }
    }
}
