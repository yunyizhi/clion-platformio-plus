package org.btik.platformioplus.ui.task.tree.model;

import com.intellij.icons.AllIcons;

import javax.swing.*;

/**
 * @author lustre
 * @since 2023/2/19 1:45
 */
public class EnvNode extends PioTaskTreeNode{
    public EnvNode(String displayName) {
        super(displayName);
    }

    @Override
    public Icon getMetaIcon() {
        return AllIcons.General.InlineVariablesHover;
    }
}
