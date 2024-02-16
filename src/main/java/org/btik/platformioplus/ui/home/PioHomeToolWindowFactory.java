package org.btik.platformioplus.ui.home;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.*;
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

    }
}
