package org.btik.platformioplus.ui.model;

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
    String NAME = "name";

    String TYPE = "type";

    String PARAMETERS = "parameters";

    // type
    String PIO_TASK_TREE_NODE = "PioTaskTreeNode";

    String COMMAND_NODE = "CommandNode";

}
