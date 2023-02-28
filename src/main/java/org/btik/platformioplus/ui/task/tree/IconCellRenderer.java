package org.btik.platformioplus.ui.task.tree;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.CheckedTreeNode;
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


    private final JCheckBox checkBox = new JCheckBox();


    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        if (node == null) {
            return null;
        }

        IconTextItem<?> iconTextItem;
        Object userObject = node.getUserObject();
        if (node instanceof CheckedTreeNode checkedTreeNode) {
            checkBox.setSelected(checkedTreeNode.isChecked());
            iconTextItem = new IconTextItem<>(checkBox);
        } else {
            iconTextItem = new IconTextItem<>(label);
        }
        JComponent component = iconTextItem.getComponent();
        if (selected) {

            component.setOpaque(false);
            component.setBackground(getBackgroundSelectionColor());
        } else {
            component.setOpaque(true);
            component.setBackground(getBackgroundNonSelectionColor());
            component.setForeground(getTextNonSelectionColor());
        }

        iconTextItem.setText(node.toString());
        if (!(userObject instanceof PioTaskTreeNode taskTreeNode)) {
            return component;
        }
        component.setToolTipText(null);
        String toolTip = taskTreeNode.getToolTip();
        if (toolTip != null && !toolTip.isEmpty()) {
            component.setToolTipText(toolTip);
        }
        String icon = taskTreeNode.getIcon();
        // 优先使用配置的图标
        if (icon != null && !icon.isEmpty() && setIconFromConf(icon, iconTextItem)) {
            return component;
        }
        // 设置类级别的默认的图标
        iconTextItem.setIcon(taskTreeNode.getMetaIcon());

        return component;

    }

    private boolean setIconFromConf(String icon, IconTextItem<?> iconTextItem) {
        // ij: 命令空间则是 使用内部的图标 ,否则使用的资源目录的图标文件
        if (icon.startsWith("ij:")) {
            Icon ijIcon = ijIconMap.get(icon);
            if (ijIcon == null) {
                return false;
            }
            iconTextItem.setIcon(ijIcon);
        } else {
            iconTextItem.setIcon(IconLoader.getIcon(icon, getClass()));
        }
        return true;
    }
}