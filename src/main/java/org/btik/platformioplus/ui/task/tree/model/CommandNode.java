package org.btik.platformioplus.ui.task.tree.model;

import javax.swing.*;

/**
 * @author lustre
 * @since 2022/10/11 22:58
 */
public class CommandNode extends PioTaskTreeNode {

    private final String command;

    private String envParamKey;


    public String getEnvParamKey() {
        return envParamKey;
    }

    public void setEnvParamKey(String envParamKey) {
        this.envParamKey = envParamKey;
    }

    @Override
    public Icon getMetaIcon() {
        return icons.ExternalSystemIcons.Task;
    }

    public CommandNode(String displayName, String command) {
        super(displayName);
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
