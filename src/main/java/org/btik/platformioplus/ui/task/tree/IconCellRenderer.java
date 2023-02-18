package org.btik.platformioplus.ui.task.tree;

import com.intellij.openapi.util.IconLoader;
import org.btik.platformioplus.ui.task.tree.model.PioTaskTreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.HashMap;

/**
 * @author lustre
 * @since 2022/10/11 21:24
 */
public class IconCellRenderer extends DefaultTreeCellRenderer {
    private static final HashMap<String, Icon> ijIconMap = new HashMap<>();

    static {
        ijIconMap.put("ij:icons.MavenIcons#ProfilesClosed", null);
    }

    JLabel label = new JLabel();


    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node == null)
            return null;
        if (selected) {
            label.setOpaque(false);
            label.setBackground(getBackgroundSelectionColor());
        } else {
            label.setOpaque(true);
            label.setBackground(getBackgroundNonSelectionColor());
            label.setForeground(getTextNonSelectionColor());
        }
        label.setText(node.toString());

        Object userObject = node.getUserObject();
        if (!(userObject instanceof PioTaskTreeNode taskTreeNode)) {
            return label;
        }
        String icon = taskTreeNode.getIcon();
        // 优先使用配置的图标
        if (icon != null && !icon.isEmpty() && setIconFromConf(icon)) {
            return label;
        }
        // 设置类级别的默认的图标
        label.setIcon(taskTreeNode.getMetaIcon());

        return label;

    }

    private boolean setIconFromConf(String icon) {
        // ij: 命令空间则是 使用内部的图标 ,否则使用的资源目录的图标文件
        if (icon.startsWith("ij:")) {
            Icon ijIcon = ijIconMap.get(icon);
            if (ijIcon == null) {
                return false;
            }
            label.setIcon(ijIcon);
        } else {
            label.setIcon(IconLoader.getIcon(icon, getClass()));
        }
        return true;
    }
}