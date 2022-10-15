package org.btik.platformioplus.ui.model;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author lustre
 * @since 2022/10/11 23:03
 */
public abstract class TaskTreeNodeFactory {

    public static DefaultMutableTreeNode newNode(String name) {
        return new DefaultMutableTreeNode(new PioTaskTreeNode(name));
    }

    public static DefaultMutableTreeNode newCmdNode(String name, String command) {
        return new DefaultMutableTreeNode(new CommandNode(name, command));
    }

    public static void appendCmd(DefaultMutableTreeNode parent, String name, String command) {
        parent.add(new DefaultMutableTreeNode(new CommandNode(name, command)));
    }

    public static void appendPioHome(DefaultMutableTreeNode parent, String name, String command) {
        parent.add(new DefaultMutableTreeNode(new PioHomeNode(name, command)));
    }


}
