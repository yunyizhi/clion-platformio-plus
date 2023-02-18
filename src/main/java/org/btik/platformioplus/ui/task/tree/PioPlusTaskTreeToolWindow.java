package org.btik.platformioplus.ui.task.tree;

import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import org.btik.platformioplus.ini.reload.PioIniChangeHandler;
import org.btik.platformioplus.ui.task.tree.execute.TreeNodeCmdExecutor;
import org.btik.platformioplus.ui.task.tree.model.CommandNode;
import org.btik.platformioplus.ui.task.tree.model.PioTaskTreeNode;
import org.btik.platformioplus.ui.task.tree.model.TaskTreeFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
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

    private static final String ENVS_ID = "envs";

    private final Project project;

    public PioPlusTaskTreeToolWindow(@NotNull Project project) {
        this.project = project;
    }


    public JPanel getContent() {
        return pioToolWinContent;
    }


    private void createUIComponents() {
        DefaultMutableTreeNode root = TaskTreeFactory.load();
        if (root == null) {
            return;
        }
        tree = new Tree(root);
        tree.setCellRenderer(new IconCellRenderer());
        bindEnvNode(root);
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

    /**
     * 绑定环境节点，环境节点为根节点下的一级节点
     */
    private void bindEnvNode(DefaultMutableTreeNode root) {
        PioIniChangeHandler pioIniChangeHandler = project.getService(PioIniChangeHandler.class);
        if (pioIniChangeHandler == null) {
            return;
        }
        int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TreeNode child = root.getChildAt(i);
            if (!(child instanceof DefaultMutableTreeNode defaultMutableTreeNode)) {
                continue;
            }
            Object userObject = defaultMutableTreeNode.getUserObject();
            if (!(userObject instanceof PioTaskTreeNode pioTaskTreeNode)) {
                continue;
            }
            if (ENVS_ID.equals(pioTaskTreeNode.getId())) {
                pioIniChangeHandler.bindEnvNode(defaultMutableTreeNode, tree::updateUI);
                break;
            }
        }
    }
}
