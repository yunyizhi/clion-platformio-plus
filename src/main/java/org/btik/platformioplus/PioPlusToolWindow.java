package org.btik.platformioplus;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.treeStructure.Tree;
import org.btik.platformioplus.ui.IconCellRenderer;
import org.btik.platformioplus.ui.model.PioHomeNode;
import org.btik.platformioplus.ui.model.TaskTreeNodeFactory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author lustre
 * @since 2022/10/7 21:44
 */
public class PioPlusToolWindow {

    private JPanel pioToolWinContent;

    private Tree tree;
    private JPanel head;
    private JLabel setting;


    public PioPlusToolWindow(ToolWindow toolWindow) {
        setting.setIcon(AllIcons.General.Settings);
        setting.setToolTipText("settings");
        setting.setText("");
    }


    public JPanel getContent() {
        return pioToolWinContent;
    }


    private void createUIComponents() {
        DefaultMutableTreeNode root = TaskTreeNodeFactory.newNode(" Tasks");
        tree = new Tree(root);
        tree.setCellRenderer(new IconCellRenderer());
        DefaultMutableTreeNode defaultTasks = TaskTreeNodeFactory.newNode("Default");
        root.add(defaultTasks);
        TaskTreeNodeFactory.appendCmd(defaultTasks, "Re init", " init --ide clion");
        TaskTreeNodeFactory.appendCmd(defaultTasks, "Build", " run");
        TaskTreeNodeFactory.appendCmd(defaultTasks, "Upload", " run --target upload");
        TaskTreeNodeFactory.appendCmd(defaultTasks, "Monitor", " device monitor");
        TaskTreeNodeFactory.appendCmd(defaultTasks, "Upload and Monitor", " run --target upload --target monitor");
        TaskTreeNodeFactory.appendCmd(defaultTasks, "Devices", " device list");
        TaskTreeNodeFactory.appendCmd(defaultTasks, "Clean", " run --target clean");
        TaskTreeNodeFactory.appendCmd(defaultTasks, "Clean All", " run --target cleanall");
        DefaultMutableTreeNode pioHome = TaskTreeNodeFactory.newNode(" Home");
        root.add(pioHome);
        TaskTreeNodeFactory.appendPioHome(pioHome, "Open", " home --no-open");
        TaskTreeNodeFactory.appendCmd(pioHome, "Upgrade", " upgrade");

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    if (path == null) {
                        return;
                    }
                    DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) path.getLastPathComponent();
                    Object userObject = lastPathComponent.getUserObject();
                    if (userObject instanceof PioHomeNode) {
                        ((PioHomeNode) userObject).run();
                    }
                    System.out.println(e.getComponent());
                    System.out.println(e.getComponent().getClass());
                }
            }
        });
    }
}
