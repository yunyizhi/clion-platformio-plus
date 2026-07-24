package org.btik.platformioplus.ui.task.tree;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.treeStructure.Tree;
import org.btik.platformioplus.ini.reload.PioIniChangeHandler;
import org.btik.platformioplus.service.PlatformIoIniStore;
import org.btik.platformioplus.ui.task.tree.execute.TreeNodeCmdExecutor;
import org.btik.platformioplus.ui.task.tree.model.CommandNode;
import org.btik.platformioplus.ui.task.tree.model.PioTaskTreeNode;
import org.btik.platformioplus.ui.task.tree.model.TaskTreeFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Supplier;

import static javax.swing.tree.TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION;

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
        IconCellRenderer cellRenderer = new IconCellRenderer();
        tree.setCellRenderer(cellRenderer);
        tree.getSelectionModel().setSelectionMode(DISCONTIGUOUS_TREE_SELECTION);
        Supplier<List<String>> getEnvsFunction = project.getService(PioIniChangeHandler.class)::getEnvs;
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path == null) {
                    return;
                }
                // 设置复选框选中
                DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (lastPathComponent instanceof CheckedTreeNode checkedTreeNode) {
                    checkedTreeNode.setChecked(!checkedTreeNode.isChecked());
                    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

                    model.nodeChanged(checkedTreeNode);
                    tree.updateUI();
                }
                // 执行命令节点
                if (e.getClickCount() == 2) {
                    Object userObject = lastPathComponent.getUserObject();
                    if (userObject instanceof CommandNode) {
                        TreeNodeCmdExecutor.execute(project, (CommandNode) userObject, getEnvsFunction);
                    }
                }
            }
        });
        bindEnvNode(root);
        loadEnv();

    }

    /**
     * 加载已存在的环境
     */
    private void loadEnv() {
        PioIniChangeHandler pioIniChangeHandler = project.getService(PioIniChangeHandler.class);
        if (pioIniChangeHandler == null) {
            return;
        }
        PlatformIoIniStore service = project.getService(PlatformIoIniStore.class);
        PsiFile platformIoIni = service.getPlatformIoIni();
        if (platformIoIni == null) {
            return;
        }
        pioIniChangeHandler.loadEnvInFile(platformIoIni.getChildren());
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
