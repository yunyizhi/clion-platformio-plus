package org.btik.platformioplus.ui.task.tree;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import org.btik.platformioplus.ui.task.tree.model.CommandNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author lustre
 * @since 2022/10/11 21:24
 */
public class IconCellRenderer extends DefaultTreeCellRenderer {
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
        if (node.isRoot()) {
            label.setIcon(IconLoader.getIcon("/pioplus/platformio_13.svg", getClass()));
            label.setText(node.toString());
            return label;
        }
        Object userObject = node.getUserObject();
        if (userObject instanceof CommandNode) {
            label.setIcon(icons.ExternalSystemIcons.Task);
            label.setText(node.toString());
            return label;
        }
        label.setIcon(AllIcons.Nodes.ConfigFolder);
        label.setText(node.toString());
        return label;

    }
}