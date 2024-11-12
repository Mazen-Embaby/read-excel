package trt;

public class XsCell {

	private String value;
	private String comment;

	public XsCell(String value, String comment) {
		this.value = value;
		this.comment = comment;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "XsCell [value=" + value + "]";
	}

}
