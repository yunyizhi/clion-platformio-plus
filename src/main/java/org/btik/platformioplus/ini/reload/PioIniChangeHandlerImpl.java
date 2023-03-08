package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;
import com.intellij.psi.PsiElement;
import com.intellij.ui.CheckedTreeNode;
import ini4idea.lang.psi.IniSection;
import org.btik.platformioplus.ui.task.tree.model.EnvNode;
import org.jetbrains.annotations.NotNull;


import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;


/**
 * @author lustre
 * @since 2022/12/18 13:40
 */
public class PioIniChangeHandlerImpl implements PioIniChangeHandler {

    private final HashSet<FloatingToolbarComponent> floatingToolbarComponents = new HashSet<>();

    private boolean isVisible = false;


    private CharSequence lastSaveCharSequence = "";

    private CharSequence lastUnSaveCharSequence = "";


    private volatile boolean fileChange;

    private final List<CheckedTreeNode> envs = new ArrayList<>();

    private final HashMap<String, CheckedTreeNode> envMap = new HashMap<>();

    private DefaultMutableTreeNode envNode;

    private Runnable treeUpdateUI;

    private final byte[] envLock = new byte[0];


    private void setVisible(boolean visible) {
        if (visible == isVisible) {
            return;
        }
        isVisible = visible;
        if (visible) {
            for (FloatingToolbarComponent floatingToolbarComponent : floatingToolbarComponents) {
                floatingToolbarComponent.scheduleShow();
            }
            return;
        }

        for (FloatingToolbarComponent floatingToolbarComponent : floatingToolbarComponents) {
            floatingToolbarComponent.scheduleHide();
        }
    }

    @Override
    public void register(FloatingToolbarComponent component) {
        floatingToolbarComponents.add(component);
        component.scheduleHide();
    }

    @Override
    public void update(CharSequence charSequence) {
        lastUnSaveCharSequence = charSequence;

        // 文件回退则隐藏，否则依然存在差异则显示
        fileChange = !Objects.equals(lastSaveCharSequence, charSequence);
        setVisible(fileChange);
    }

    @Override
    public void saveChangeAndHide() {
        lastSaveCharSequence = lastUnSaveCharSequence;
        fileChange = false;
        setVisible(false);
    }

    @Override
    public List<String> getEnvs() {
        synchronized (envLock) {
            return envs.stream().
                    filter(CheckedTreeNode::isChecked)
                    .map(DefaultMutableTreeNode::toString)
                    .toList();
        }

    }

    @Override
    public void bindEnvNode(DefaultMutableTreeNode envNode, Runnable updateUI) {
        this.envNode = envNode;
        this.treeUpdateUI = updateUI;
    }

    @Override
    public void loadEnvInFile(@NotNull PsiElement[] children) {
        synchronized (envLock) {
            envs.clear();
            for (PsiElement child : children) {
                if (child instanceof IniSection section) {
                    String name = section.getNameText();
                    if (name != null && name.startsWith("[env:")) {
                        String envName = name.substring(5, name.length() - 1);
                        CheckedTreeNode checkedTreeNode = envMap.get(envName);
                        if (checkedTreeNode == null) {
                            checkedTreeNode = new CheckedTreeNode(new EnvNode(envName));
                            checkedTreeNode.setChecked(false);
                        }
                        envs.add(checkedTreeNode);
                    }
                }
            }
            if (envNode != null) {
                envNode.removeAllChildren();
            }

            envMap.clear();
            for (CheckedTreeNode env : envs) {
                if (envNode != null) {
                    envNode.add(env);
                }
                envMap.put(env.toString(), env);
            }
        }
        treeUpdateUI.run();
    }
}
