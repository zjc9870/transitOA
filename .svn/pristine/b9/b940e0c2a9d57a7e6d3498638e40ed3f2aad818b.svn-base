package com.expect.admin.service.vo.component.html;

import com.expect.admin.service.vo.component.BaseVo;

public class ButtonVo extends BaseVo{

	public final String disabled = "disabled";
	public final String outline = "btn-outline";
	public final String Type_Primary = "btn-primary";
	public final String Type_Success = "btn-success";
	public final String Type_Info = "btn-info";
	public final String Type_Warning = "btn-warning";
	public final String Type_Danger = "btn-danger";
	public final String Type_Link = "btn-link";

	private final static int Size_Small = 1;
	private final static int Size_Normal = 2;
	private final static int Size_Larger = 3;

	private String className = "";
	private String text;
	private String otherAttr;
	private String size = "";
	private boolean isDisabled;
	private boolean isOutline;

	public String getButton() {
		String button = "<button type='button' class='btn " + size + " " + className + "' " + otherAttr + ">" + text
				+ "</button>";
		return button;
	}

	/**
	 * @param isButton false:代表标签是a,true:代表标签是button
	 */
	public String getButton(boolean isButton) {
		if (isButton) {
			return getButton();
		} else {
			String button = "<a class='btn " + size + " " + className + "' " + otherAttr + ">" + text + "</a>";
			return button;
		}
	}

	public String getClassName() {
		return className;
	}

	public String getText() {
		return text;
	}

	public String getOtherAttr() {
		return otherAttr;
	}

	public String getSize() {
		return size;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	public boolean isOutline() {
		return isOutline;
	}

	public static class Builder {

		private ButtonVo buttonVo;

		public Builder() {
			buttonVo = new ButtonVo();
		}

		public Builder setClassName(String className) {
			buttonVo.className += " " + className + " ";
			return this;
		}

		public Builder setText(String text) {
			buttonVo.text = text;
			return this;
		}

		public Builder setOtherAttr(String otherAttr) {
			buttonVo.otherAttr = otherAttr;
			return this;
		}

		public Builder setSize(int size) {
			String sizeClassName = "";
			switch (size) {
			case Size_Small:
				sizeClassName = "btn-xs";
				break;
			case Size_Normal:
				break;
			case Size_Larger:
				sizeClassName = "btn-lg";
				break;
			}
			buttonVo.className += sizeClassName;
			return this;
		}

		public Builder setDisabled(boolean isDisabled) {
			buttonVo.isDisabled = isDisabled;
			if (isDisabled) {
				buttonVo.className += " " + buttonVo.disabled + " ";
			}
			return this;
		}

		public Builder setOutline(boolean isOutline) {
			buttonVo.isOutline = isOutline;
			if (isOutline) {
				buttonVo.className += " " + buttonVo.outline + " ";
			}
			return this;
		}

		public ButtonVo create() {
			return buttonVo;
		}
	}

}
