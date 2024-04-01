package org.btik.platformioplus.ui.home;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.*;
import org.jetbrains.annotations.NotNull;

import static org.btik.platformioplus.service.PlatformIoPlusConst.PIO_HOME_CONTENT_ID;
import static org.btik.platformioplus.service.PlatformIoPlusConst.PIO_HOME_OPT_CONTENT_ID;


/**
 * @author lustre
 * @since 2022/10/15 10:01
 */
public class PioHomeToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory =  ApplicationManager.getApplication().getService(ContentFactory.class);
        Content content = contentFactory
                .createContent(new PioHomeToolWindow(), PIO_HOME_CONTENT_ID, false);
        toolWindow.getContentManager().addContent(content);
        Content logContent = contentFactory
                .createContent(new PioHomeOptionPanel(), PIO_HOME_OPT_CONTENT_ID, false);
        toolWindow.getContentManager().addContent(logContent);
    }
}
