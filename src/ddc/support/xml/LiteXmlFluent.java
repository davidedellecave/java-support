package ddc.support.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LiteXmlFluent {
	private Document doc = null;
	private Element e = null;

	public LiteXmlFluent(String root) throws LiteXmlException {
		this.doc = LiteXmlUtil.createDoc();
		e = doc.createElement(root);
		doc.appendChild(e);
	}

	public LiteXmlFluent(Document doc, Element e) {
		this.doc = doc;
		this.e = e;
	}
	
	public Element getElement() {
		return e;
	}
	
	public Document getDocument() {
		return doc;
	}
	
	public LiteXmlFluent getParent() {
		Node n = this.getElement().getParentNode();		
		if (n instanceof Element) {
			System.err.println("getParent - parent element not found");
			return new LiteXmlFluent(doc, (Element)n);		
		} else {
			return this;
		}	
	}

	public LiteXmlFluent addChild(String name) {
		Element elem = doc.createElement(name);
		e.appendChild(elem);
		return new LiteXmlFluent(doc, elem);
	}
	
	public LiteXmlFluent addChild(String name, String content) {
		Element elem = doc.createElement(name);
		elem.setTextContent(content);
		e.appendChild(elem);
		return new LiteXmlFluent(doc, elem);
	}

	public LiteXmlFluent addSibling(String name) {
		Element elem = doc.createElement(name);
		e.getParentNode().appendChild(elem);
		return new LiteXmlFluent(doc, elem);
	}

	public LiteXmlFluent addSibling(String name, String content) {
		Element elem = doc.createElement(name);
		elem.setTextContent(content);
		e.getParentNode().appendChild(elem);
		return new LiteXmlFluent(doc, elem);
	}


	
	public String toXmlString(String charsetName) throws LiteXmlException {
		return LiteXmlUtil.getXmlString(this.getDocument(), charsetName);
	}
	

	public static void main(String[] args) throws InterruptedException, LiteXmlException {
		LiteXmlFluent f = new LiteXmlFluent("html");
		LiteXmlFluent table = f.addChild("h1", "content").addSibling("table");
		table.addChild("tr").addChild("th", "header1").addSibling("th", "header2");
		table.addChild("tr").addChild("td", "data1").addSibling("td", "data2");
		String html = f.toXmlString("UTF-8");
		System.out.println(html);
	}
}
