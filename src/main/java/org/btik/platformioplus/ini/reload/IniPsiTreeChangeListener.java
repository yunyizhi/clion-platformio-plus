package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiTreeChangeListener;
import ini4idea.lang.psi.IniSection;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.btik.platformioplus.ini.reload.ChangeHandler.RE_INIT_DO;

/**
 * @author lustre
 * @since 2022/12/18 16:07
 */
public class IniPsiTreeChangeListener implements PsiTreeChangeListener {

    private ChangeHandler changeHandler;



    @Override
    public void beforeChildAddition(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void beforeChildRemoval(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void beforeChildReplacement(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void beforeChildMovement(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void beforeChildrenChange(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void beforePropertyChange(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void childAdded(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void childRemoved(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void childReplaced(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void childrenChanged(@NotNull PsiTreeChangeEvent event) {
        PsiFile file = event.getFile();
        if (file == null) {
            return;
        }
        Project project = changeHandler.getProject();
        if (project == null) {
            return;
        }
        if (!Objects.equals(file.getName(), PioConf.FILE_NAME)) {
            return;
        }
        Project fileProject = file.getProject();
        if (fileProject != project) {
            return;
        }
        PsiElement parent = event.getParent();
        if (parent instanceof IniSection) {
            parent = parent.getParent();
        }
        if (parent == null) {
            return;
        }
        changeHandler.update(parent.getText());


    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent event) {

    }

    public IniPsiTreeChangeListener() {
        AnAction action = ActionManager.getInstance().getAction(RE_INIT_DO);
        if (action instanceof ChangeHandler) {
            this.changeHandler = (ChangeHandler) action;
        }
    }
}
