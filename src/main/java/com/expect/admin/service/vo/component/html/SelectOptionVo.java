package com.expect.admin.service.vo.component.html;

import java.util.ArrayList;
import java.util.List;

import com.expect.admin.service.vo.component.BaseVo;

public class SelectOptionVo extends BaseVo{

	public List<OptionVo> options = new ArrayList<>();
	public StringBuilder htmlSb = new StringBuilder();

	public void addOption(String value, String text, boolean isSelected) {
		options.add(new OptionVo(value, text, isSelected));
		if (isSelected) {
			htmlSb.append("<option value='" + value + "' selected>" + text + "</option>");
		} else {
			htmlSb.append("<option value='" + value + "'>" + text + "</option>");
		}
	}

	public void addOption(String value, String text) {
		addOption(value, text, false);
	}

	public String getHtml() {
		return htmlSb.toString();
	}

	public class OptionVo {
		private String value;
		private String text;
		private boolean isSelected;

		public OptionVo() {
		}

		public OptionVo(String value, String text, boolean isSelected) {
			this.value = value;
			this.text = text;
			this.isSelected = isSelected;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

	}
}
