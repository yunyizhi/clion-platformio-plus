package org.btik.platformioplus.ui.task.tree;

import com.intellij.ui.treeStructure.Tree;
import org.btik.platformioplus.ui.task.tree.execute.TreeNodeCmdExecutor;
import org.btik.platformioplus.ui.task.tree.model.CommandNode;
import org.btik.platformioplus.ui.task.tree.model.TaskTreeFactory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author lustre
 * @since 2022/10/7 21:44
 */
public class PioPlusTaskTreeToolWindow {

    private JPanel pioToolWinContent;

    private Tree tree;

    public PioPlusTaskTreeToolWindow() {
//
    }


    public JPanel getContent() {
        return pioToolWinContent;
    }


    private void createUIComponents() {
        tree = new Tree(TaskTreeFactory.load());
        tree.setCellRenderer(new IconCellRenderer());
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
                    if (userObject instanceof CommandNode) {
                        TreeNodeCmdExecutor.execute(e.getComponent(), (CommandNode) userObject);
                    }
                }
            }
        });

    }
}
