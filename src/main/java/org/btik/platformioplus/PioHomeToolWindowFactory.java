package org.btik.platformioplus;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.*;
import org.btik.platformioplus.service.PlatformIoPlusService;
import org.btik.platformioplus.ui.PioHomeToolWindow;
import org.jetbrains.annotations.NotNull;

/**
 * @author lustre
 * @since 2022/10/15 10:01
 */
public class PioHomeToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        PioHomeToolWindow pioHomeToolWindow = new PioHomeToolWindow();
        ContentFactory contentFactory =  ApplicationManager.getApplication().getService(ContentFactory.class);
        Content content = contentFactory
                .createContent(pioHomeToolWindow.getContent(), "Pio Home", false);
        toolWindow.getContentManager().addContent(content);

        PlatformIoPlusService service = project.getService(PlatformIoPlusService.class);
        toolWindow.setAvailable(service != null && service.isEnable());
        if(service != null){
            service.registerUIComponent(toolWindow::setAvailable);
        }

    }
}
