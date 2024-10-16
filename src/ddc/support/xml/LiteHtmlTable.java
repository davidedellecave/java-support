package ddc.support.xml;

import org.w3c.dom.Node;

public class LiteHtmlTable extends LiteXml {
    private static final long serialVersionUID = -2591624652391624345L;
    private Node table;
    private Node lastRowCol;
    private Node lastRowHeader;
    private Node lastHeader;
    private Node lastCol;

    public LiteHtmlTable() throws LiteXmlException {
        super(LiteXmlUtil.createDoc());
        table = addRoot("table");
    }

    public Node getTable() {
        return table;
    }

    public void setTable(Node table) {
        this.table = table;
    }

    public Node getLastRowCol() {
        return lastRowCol;
    }

    public void setLastRowCol(Node lastRowCol) {
        this.lastRowCol = lastRowCol;
    }

    public Node getLastRowHeader() {
        return lastRowHeader;
    }

    public void setLastRowHeader(Node lastRowHeader) {
        this.lastRowHeader = lastRowHeader;
    }

    public Node getLastHeader() {
        return lastHeader;
    }

    public void setLastHeader(Node lastHeader) {
        this.lastHeader = lastHeader;
    }

    public Node getLastCol() {
        return lastCol;
    }

    public void setLastCol(Node lastCol) {
        this.lastCol = lastCol;
    }

    public Node getRoot() {
        return super.getRoot();
    }

    public LiteHtmlTable addRowColumn() throws LiteXmlException {
        lastRowCol = this.addChild(table, "tr");
        return this;
    }

    public LiteHtmlTable addRowHeader() throws LiteXmlException {
        lastRowHeader = this.addChild(table, "tr");
        return this;
    }

    public LiteHtmlTable addHeader(String text) throws LiteXmlException {
        lastHeader = this.addChild(lastRowHeader, "th", text);
        return this;
    }

    public LiteHtmlTable addHeader(String... text) throws LiteXmlException {
        for (int i = 0; i < text.length; i++) {
            addHeader(text[i]);
        }
        return this;
    }

    public LiteHtmlTable addHeader() throws LiteXmlException {
        lastCol = this.addChild(lastRowHeader, "th");
        return this;
    }

    public LiteHtmlTable addColumn() throws LiteXmlException {
        lastCol = this.addChild(lastRowCol, "td");
        return this;
    }


    public LiteHtmlTable addColumn(String text) throws LiteXmlException {
        lastCol = this.addChild(lastRowCol, "td", text);
        return this;
    }

    public LiteHtmlTable addColumn(Object text) throws LiteXmlException {
        lastCol = this.addChild(lastRowCol, "td", String.valueOf(text));
        return this;
    }

    public LiteHtmlTable addColumn(Node element) throws LiteXmlException {
        lastCol = this.addChild(lastRowCol, "td");
        lastCol.appendChild(element);
        return this;
    }
}