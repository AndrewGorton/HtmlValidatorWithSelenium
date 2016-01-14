package uk.andrewgorton.HtmlValidatorWithSelenium;

public class Message {
    private String extract;
    private int firstColumn;
    private int hiliteLength;
    private int hiliteStart;
    private int lastColumm;
    private int lastLine;
    private String message;
    private String subType;
    private String type;

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public int getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(int firstColumn) {
        this.firstColumn = firstColumn;
    }

    public int getHiliteLength() {
        return hiliteLength;
    }

    public void setHiliteLength(int hiliteLength) {
        this.hiliteLength = hiliteLength;
    }

    public int getHiliteStart() {
        return hiliteStart;
    }

    public void setHiliteStart(int hiliteStart) {
        this.hiliteStart = hiliteStart;
    }

    public int getLastColumm() {
        return lastColumm;
    }

    public void setLastColumm(int lastColumm) {
        this.lastColumm = lastColumm;
    }

    public int getLastLine() {
        return lastLine;
    }

    public void setLastLine(int lastLine) {
        this.lastLine = lastLine;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
