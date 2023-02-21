package org.btik.platformioplus.ui.task.tree.model;

/**
 * @author lustre
 * @since 2022/10/21 1:03
 */
public interface TreeXmlMeta {
    // tags
    String XML_TAG = "pio-task-tree";

    String TREE_ROOT = "root";

    String FOLDER_TAG = "folder";

    String EXECUTABLE_TAG = "executable";
    // attrs
    String ID = "id";
    String NAME = "name";

    String TYPE = "type";

    String ICON = "icon";

    String TOOL_TIP = "toolTip";

    String ENVIRONMENT = "environment";

    String PARAMETERS = "parameters";

    String LOCK = "lock";

    // type
    String PIO_TASK_TREE_NODE = "PioTaskTreeNode";

    String COMMAND_NODE = "CommandNode";

    String LOCK_COMMAND_NODE = "LockCommandNode";

}
