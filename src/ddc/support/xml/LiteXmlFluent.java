package ddc.support.xml;

import org.w3c.dom.*;
import org.w3c.dom.Node;

public class LiteXmlFluent {
    private LiteXml xml = null;
    private Node lastNode = null;
    private Node root = null;
    private Attr lastAttr = null;

    public LiteXmlFluent(String rootName) throws LiteXmlException {
        var doc = LiteXmlUtil.createDoc();
        xml = new LiteXml(doc);
        root = xml.addRoot(rootName);
        lastNode = root;
    }

    public Document getDocument() {
        return xml.getDocument();
    }


    public Node detach() {
        return (Node) xml.getDocument().removeChild(root);
    }

    public LiteXmlFluent addChild(String name) throws LiteXmlException {
        lastNode = xml.addChild(lastNode, name);
        return this;
    }

    public LiteXmlFluent addChild(String name, String text) throws LiteXmlException {
        lastNode = xml.addChild(lastNode, name, text);
        return this;
    }

    public LiteXmlFluent addChild(Node Node) {
        lastNode.appendChild(Node);
        lastNode = Node;
        return this;
    }

    public LiteXmlFluent addAttribute(String name, String value) {
        lastAttr = xml.addAttribute((Element) lastNode, name, value);
        return this;
    }

    public LiteXmlFluent addSibling(Node Node) {
        lastNode.getParentNode().appendChild(Node);
        lastNode = Node;
        return this;
    }

    public LiteXmlFluent addSibling(String name) {
        Node elem = xml.getDocument().createElement(name);
        return  addSibling(elem);
    }

    public LiteXmlFluent addSibling(String name, String content) {
        addSibling(name).getLastNode().setTextContent(content);
        return this;
    }

    public LiteXmlFluent importAsChild(LiteXmlFluent importXml) {
        return importAsChild(importXml.getRoot());
    }

    public LiteXmlFluent importAsSibling(LiteXmlFluent importXml) {
        return importAsSibling(importXml.getRoot());
    }


    public LiteXmlFluent importAsChild(Node node) {
        Node newNode = xml.getDocument().importNode(node, true);
        return addChild((Node) newNode);
    }

    public LiteXmlFluent importAsSibling(Node node) {
        Node newNode = xml.getDocument().importNode(node, true);
        return addSibling((Node) newNode);
    }

    public Node getLastNode() {
        return lastNode;
    }

    public Node getRoot() {
        return root;
    }

    public LiteXml getXml() {
        return xml;
    }


    public void setLastNode(Node newLastNode) {
        lastNode = newLastNode;
    }

    public LiteXmlFluent setLastNodeAsParent() {
        lastNode = (Node) lastNode.getParentNode();
        return this;
    }
}
