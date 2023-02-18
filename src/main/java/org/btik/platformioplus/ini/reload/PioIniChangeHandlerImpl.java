package org.btik.platformioplus.ini.reload;

import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent;
import org.btik.platformioplus.ui.task.tree.model.EnvNode;


import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


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

    private final List<String> envs = new ArrayList<>();

    private DefaultMutableTreeNode envNode;

    private Runnable treeUpdateUI;


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
        return envs;
    }

    @Override
    public void fireEnvsChange() {
        if (envNode == null) {
            return;
        }
        if (treeUpdateUI == null) {
            return;
        }
        envNode.removeAllChildren();
        for (String env : envs) {
            envNode.add(new DefaultMutableTreeNode(new EnvNode(env)));
        }
        treeUpdateUI.run();
    }

    @Override
    public void bindEnvNode(DefaultMutableTreeNode envNode, Runnable updateUI) {
        this.envNode = envNode;
        this.treeUpdateUI = updateUI;
    }
}
