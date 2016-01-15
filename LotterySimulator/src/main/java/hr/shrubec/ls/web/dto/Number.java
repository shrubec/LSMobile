package hr.shrubec.ls.web.dto;

public class Number {

	private int value;
	private boolean rendered;
	private boolean selected;

	public Number() {
		
	}
	
	public Number(int value, boolean rendered, boolean selected) {
		this.value = value;
		this.rendered = rendered;
		this.selected = selected;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

}
