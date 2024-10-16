package ddc.support.xml;

import org.w3c.dom.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author davidedc, 01/Agosto/2013
 */
public class LiteXml implements LiteXmlDocument {
    private static DateFormat ISODateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Document doc = null;

    public LiteXml(Document doc) {
        this.doc = doc;
    }

    @Override
    public Document getDocument() {
        return doc;
    }

    @Override
    public Node addRoot(String name) {
        Node e = doc.createElement(name);
        doc.appendChild(e);
        return e;
    }

    @Override
    public Element createElement(String name) {
        Element e = doc.createElement(name);
        return e;
    }

    @Override
    public Node getRoot() {
        return doc.getDocumentElement();
    }

    @Override
    public Node addChild(Node parent, Node child) throws LiteXmlException {
        if (parent != null) {
            parent.appendChild(child);
            return child;
        } else {
            throw new LiteXmlException("Parent node in null");
        }
    }

    @Override
    public Node addChild(Node parent, String name) throws LiteXmlException {
        Node e = doc.createElement(name);
        return addChild(parent, e);
    }

    @Override
    public Node addChild(Node parent, String name, String value) throws LiteXmlException {
        Node e = doc.createElement(name);
        e.setTextContent(value);
        return addChild(parent, e);
    }

    @Override
    public Node addChild(Node element, String name, long value) throws LiteXmlException {
        return addChild(element, name, String.valueOf(value));
    }

    @Override
    public Node addChild(Node parent, String name, Date value) throws LiteXmlException {
        return addChild(parent, name, ISODateFormat.format(value));
    }


    @Override
    public Node addSibling(Node parent, Node sibling) throws LiteXmlException {
        addChild(parent.getParentNode(), sibling);
        return sibling;
    }

    @Override
    public Node addSibling(Node parent, String name) throws LiteXmlException {
        Node e = doc.createElement(name);
        return addSibling(parent, e);
    }

    @Override
    public Node addSibling(Node parent, String name, String value) throws LiteXmlException {
        Node e = doc.createElement(name);
        e.setTextContent(value);
        return addSibling(parent, e);
    }

    @Override
    public Node addSibling(Node element, String name, long value) throws LiteXmlException {
        return addSibling(element, name, String.valueOf(value));
    }

    @Override
    public Node addSibling(Node parent, String name, Date value) throws LiteXmlException {
        return addSibling(parent, name, ISODateFormat.format(value));
    }

    @Override
    public Node importAsChild(Node parent, Node node) throws LiteXmlException {
        Node newNode = getDocument().importNode(node, true);
        return addChild(parent, (Element) newNode);
    }

    @Override
    public Node importAsChild(LiteXml parent, Node node) throws LiteXmlException {
        Node newNode = getDocument().importNode(node, true);
        return addChild(parent.getRoot(), (Element) newNode);
    }

    @Override
    public Node importAsChild(LiteXml parent, LiteXml node) throws LiteXmlException {
        Node newNode = getDocument().importNode(node.getRoot(), true);
        return addChild(parent.getRoot(), (Element) newNode);
    }

    @Override
    public Node importAsSibling(Node parent, Node node) throws LiteXmlException {
        Node newNode = getDocument().importNode(node, true);
        return addSibling(parent, (Element) newNode);
    }

    @Override
    public Node importAsSibling(LiteXml parent, Node node) throws LiteXmlException {
        Node newNode = getDocument().importNode(node, true);
        return addSibling(parent.getRoot(), (Element) newNode);
    }

    @Override
    public Node importAsSibling(LiteXml parent, LiteXml node) throws LiteXmlException {
        Node newNode = getDocument().importNode(node.getRoot(), true);
        return addSibling(parent.getRoot(), (Element) newNode);
    }

    @Override
    public Node addChildCData(Node element, String name, String value) throws LiteXmlException {
        Node CDataNode = addChild(element, name);
        CDATASection c = doc.createCDATASection(value);
        CDataNode.appendChild(c);
        return CDataNode;
    }

    @Override
    public CDATASection setCData(Node element, String name, String value) {
        CDATASection c = doc.createCDATASection(value);
        element.appendChild(c);
        return c;
    }

    @Override
    public Attr addAttribute(Element element, String name, String value) {
        Attr a = doc.createAttribute(name);
        a.setNodeValue(value);
        element.setAttributeNode(a);
        return a;
    }


    /**
     * Returns the first (index==0) elmenent of a NodeList of all the Elements
     * in document order with a given tag name and are contained in the
     * document.
     */
    @Override
    public Element getElementByTagName(String name) {
        NodeList list = doc.getElementsByTagName(name);
        if (list.getLength() > 0) {
            Node n = list.item(0);
            if (n instanceof Element) {
                return (Element) n;
            }
        }
        return null;
    }

    /**
     * Returns all the Elements in document with a given tag
     */
    @Override
    public List<Element> getElementsByTagName(String name) {
        NodeList list = doc.getElementsByTagName(name);
        return LiteXmlUtil.toElements(list);
    }

    @Override
    public String getValueByTagName(String name) {
        Node e = getElementByTagName(name);
        return e == null ? null : e.getTextContent();
    }

    @Override
    public List<String> getValuesByTagName(String name) {
        NodeList list = doc.getElementsByTagName(name);
        ArrayList<String> a = new ArrayList<String>();
        if (list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {
                Node n = list.item(i);
                if (n instanceof Element) {
                    a.add(((Element) n).getTextContent());
                }
            }
        }
        return a;
    }

    private void serialize(Document doc, OutputStream out) throws LiteXmlException {
        try {
            TransformerFactory f = TransformerFactory.newInstance();
            Transformer serializer;
            serializer = f.newTransformer();
            serializer.transform(new DOMSource(doc), new StreamResult(out));
        } catch (TransformerException e) {
            throw new LiteXmlException("serialize", e);
        }
    }

    @Override
    public String getXmlString(String charsetName) throws LiteXmlException {
        return LiteXmlUtil.getXmlString(doc, charsetName);
//		try {
//			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//			serialize(doc, bytes);
//			return bytes.toString(charsetName);
//		} catch (UnsupportedEncodingException e) {
//			throw new LiteXmlException(e);
//		}
    }

    public void write(ByteArrayOutputStream bytes) throws LiteXmlException {
        serialize(doc, bytes);
    }

    @Override
    public void write(File file) throws LiteXmlException {
        try {
            FileOutputStream f = new FileOutputStream(file);
            serialize(doc, f);
        } catch (FileNotFoundException e) {
            throw new LiteXmlException("write", e);
        }
    }

    @Override
    public void setValue(Node element, String value) {
        element.setNodeValue(value);
    }

    @Override
    public void setValue(String name, String value) {
        Node e = this.getElementByTagName(name);
        if (e != null)
            e.setTextContent(value);
    }

    @Override
    public Map<String, String> getAttributes(Node elem) {
        Map<String, String> map = new TreeMap<>();
        NamedNodeMap nodeMap = elem.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Node node = nodeMap.item(i);
            map.put(node.getNodeName(), node.getNodeValue());
        }
        return map;
    }

    @Override
    public String getAttribute(Node elem, String attrName) {
        NamedNodeMap nodeMap = elem.getAttributes();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Node node = nodeMap.item(i);
            if (node.getNodeName().equals(attrName))
                return node.getNodeValue();
        }
        return null;
    }

}
