package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiTreeChangeListener;
import ini4idea.lang.psi.IniSection;
import org.btik.platformioplus.setting.PioConf;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


/**
 * @author lustre
 * @since 2022/12/18 16:07
 */
public class IniPsiTreeChangeListener implements PsiTreeChangeListener {


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
        if (!Objects.equals(file.getName(), PioConf.FILE_NAME)) {
            return;
        }

        PsiElement parent = event.getParent();
        if (parent instanceof IniSection) {
            parent = parent.getParent();
        }
        if (parent == null) {
            return;
        }
        Project fileProject = file.getProject();
        PioIniChangeHandler pioIniChangeHandler = fileProject.getService(PioIniChangeHandler.class);
        if (pioIniChangeHandler == null) {
            return;
        }
        List<String> envs = pioIniChangeHandler.getEnvs();
        envs.clear();
        @NotNull PsiElement[] children = parent.getChildren();
        for (PsiElement child : children) {
            if (child instanceof IniSection section) {
                String name = section.getNameText();
                if (name != null && name.startsWith("[env:")) {
                    envs.add(name.substring(5, name.length() - 1));
                }
            }
        }
        pioIniChangeHandler.fireEnvsChange();
        pioIniChangeHandler.update(parent.getText());


    }

    @Override
    public void childMoved(@NotNull PsiTreeChangeEvent event) {

    }

    @Override
    public void propertyChanged(@NotNull PsiTreeChangeEvent event) {

    }
}
