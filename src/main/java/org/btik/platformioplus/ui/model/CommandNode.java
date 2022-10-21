package org.btik.platformioplus.ui.model;

/**
 * @author lustre
 * @since 2022/10/11 22:58
 */
public class CommandNode extends PioTaskTreeNode {

    private final String command;

    private String warningMessage;

    public CommandNode(String displayName, String command) {
        super(displayName);
        this.command = command;
    }

    public CommandNode(String displayName, String command, String warningMessage) {
        this(displayName, command);
        this.warningMessage = warningMessage;
    }

    public String getCommand() {
        return command;
    }

    public String getWarningMessage() {
        return warningMessage;
    }
}
