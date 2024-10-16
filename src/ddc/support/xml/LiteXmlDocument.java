package ddc.support.xml;

import org.w3c.dom.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author davidedc, 01/Agosto/2013
 */
public interface LiteXmlDocument {
    //	public void createDocument() throws LiteXmlException;
//	public void createDocument(File file) throws LiteXmlException;
//	public void createDocument(String xml, String charset) throws LiteXmlException;	
//	public void createDocument(byte[] bytes) throws LiteXmlException;
    public Document getDocument();

    public Node addRoot(String name);

    public Node getRoot();

    public Element createElement(String name);

    //
    public Node addChild(Node parent, Node child) throws LiteXmlException;

    public Node addChild(Node Node, String name) throws LiteXmlException;

    public Node addChild(Node Node, String name, String value) throws LiteXmlException;

    public Node addChild(Node Node, String name, long value) throws LiteXmlException;

    public Node addChild(Node Node, String name, Date value) throws LiteXmlException;

    //
    public Node addSibling(Node parent, Node child) throws LiteXmlException;

    public Node addSibling(Node Node, String name) throws LiteXmlException;

    public Node addSibling(Node Node, String name, String value) throws LiteXmlException;

    public Node addSibling(Node Node, String name, long value) throws LiteXmlException;

    public Node addSibling(Node Node, String name, Date value) throws LiteXmlException;

    //
    public Node importAsChild(Node parent, Node node) throws LiteXmlException;

    public Node importAsChild(LiteXml parent, Node node) throws LiteXmlException;

    public Node importAsChild(LiteXml parent, LiteXml node) throws LiteXmlException;

    public Node importAsSibling(Node parent, Node node) throws LiteXmlException;

    public Node importAsSibling(LiteXml parent, Node node) throws LiteXmlException;

    public Node importAsSibling(LiteXml parent, LiteXml node) throws LiteXmlException;

    public Attr addAttribute(Element element, String name, String value);

    public Element getElementByTagName(String name);

    //	public Node getNode(XPathExpression expr) throws LiteXmlException;
//	public Node getNodeByXPath(String xpath) throws LiteXmlException;	
//	public List<Node>  getNodesByXPath(String xpath) throws LiteXmlException;
    public List<Element> getElementsByTagName(String name);

    //	public List<Node> getNodes(XPathExpression expr);
    public Map<String, String> getAttributes(Node elem);

    public String getAttribute(Node elem, String attrName);

    public String getValueByTagName(String name);

    //	public String getValue(XPathExpression expr) throws LiteXmlException;
//	public String getValueByXPath(String xpath);
    public List<String> getValuesByTagName(String name);

    public void setValue(Node Node, String value) throws LiteXmlException;

    public void setValue(String name, String value);

    //	public void setValue(XPathExpression expr, String value) throws LiteXmlException;
//	public void setValueByXPath(String xpath, String value);
    public Node addChildCData(Node Node, String name, String value) throws LiteXmlException;

    public CDATASection setCData(Node Node, String name, String value);

    //	public boolean removeByXPath(String xpath);
//	public boolean hasNodeByXPath(String xpath);
    public void write(ByteArrayOutputStream bytes) throws LiteXmlException;

    public void write(File file) throws LiteXmlException;

    public String getXmlString(String charsetName) throws LiteXmlException;
//	public XPathExpression compileXPath(String xpath) throws LiteXmlException;
//	public List<Node> getNodes(XPathExpression expr) throws LiteXmlException;
//	List<Node> getNodesByXPath(String xpath) throws LiteXmlException;
//	public List<Node> filterNode(NodeList nodes);
}
