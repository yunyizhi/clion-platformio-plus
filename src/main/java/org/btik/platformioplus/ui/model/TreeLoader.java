package org.btik.platformioplus.ui.model;

import com.intellij.notification.NotificationType;
import org.btik.platformioplus.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.tree.DefaultMutableTreeNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

import static org.btik.platformioplus.ui.model.TreeXmlMeta.*;
import static org.btik.platformioplus.util.DomUtil.eachChildrenElement;
import static org.btik.platformioplus.util.DomUtil.getFirstElementByName;
import static org.btik.platformioplus.util.Note.*;

/**
 * @author lustre
 * @since 2022/10/20 22:41
 */
public class TreeLoader {
    private static final HashMap<String, Function<Element, XmlNode>> factories = new HashMap<>();

    static {
        factories.put(PIO_TASK_TREE_NODE, TreeLoader::newFolder);
        factories.put(COMMAND_NODE, TreeLoader::newCmd);
    }

    public static DefaultMutableTreeNode load() {
        Element documentElement;
        try {
            Document treeConf = DomUtil.parse(TreeLoader.class.getResourceAsStream("/pioplus/defaultTree.xml"));
            documentElement = treeConf.getDocumentElement();

        } catch (Exception e) {
            NOTIFICATION_GROUP.createNotification(getMsg("tree.load.failed"),
                    getMsgF("tree.load.failed", e.getMessage()), NotificationType.ERROR).notify(null);
            return null;
        }
        Element treeRoot = getFirstElementByName(documentElement, TREE_ROOT);
        if (null == treeRoot) {
            NOTIFICATION_GROUP.createNotification(getMsg("notification.group.platformio-plus"),
                    getMsg("tree.load.failed"), NotificationType.ERROR).notify(null);
            return null;
        }

        return build(treeRoot);
    }

    private static DefaultMutableTreeNode build(Element treeRoot) {
        XmlNode rootNode = newFolder(treeRoot);
        LinkedList<XmlNode> treeNodeQueue = new LinkedList<>();
        treeNodeQueue.add(rootNode);
        while (treeNodeQueue.size() > 0) {
            XmlNode xmlNode = treeNodeQueue.removeFirst();
            eachChildrenElement(xmlNode.element, (child) -> {
                String type = child.getAttribute(TYPE);
                XmlNode childXmlNode = factories.get(type).apply(child);
                xmlNode.node.add(childXmlNode.node);
                treeNodeQueue.add(childXmlNode);
            });
        }

        return rootNode.node;
    }

    static class XmlNode {
        Element element;
        DefaultMutableTreeNode node;

        public XmlNode(Element element, DefaultMutableTreeNode parent) {
            this.element = element;
            this.node = parent;
        }
    }

    private static XmlNode newFolder(Element element) {
        String name = element.getAttribute(NAME);
        return new XmlNode(element,
                new DefaultMutableTreeNode(new PioTaskTreeNode(name)));
    }

    private static XmlNode newCmd(Element element) {
        String name = element.getAttribute(NAME);
        String parameters = element.getAttribute(PARAMETERS);
        return new XmlNode(element,
                new DefaultMutableTreeNode(
                        new CommandNode(name, parameters)));
    }

}
