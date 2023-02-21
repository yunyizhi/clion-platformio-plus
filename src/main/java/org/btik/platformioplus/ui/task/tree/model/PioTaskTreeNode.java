package org.btik.platformioplus.ui.task.tree.model;

import com.intellij.icons.AllIcons;

import javax.swing.*;

/**
 * 任务树用户对象
 *
 * @author lustre
 * @since 2022/10/11 23:00
 */
public class PioTaskTreeNode {

    private final String displayName;

    /**
     * icon的路径
     */
    private String icon;

    private String id;

    private String toolTip;

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     *类级别缺省图标
     */
    public String getIcon() {
        return icon;
    }

    public Icon getMetaIcon() {
        return AllIcons.Nodes.ConfigFolder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public PioTaskTreeNode(String displayName) {
        this.displayName = displayName;
    }
}
