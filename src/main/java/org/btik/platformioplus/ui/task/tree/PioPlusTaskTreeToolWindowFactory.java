package org.btik.platformioplus.ui.task.tree;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

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
    }
}
