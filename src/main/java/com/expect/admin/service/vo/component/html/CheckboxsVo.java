package com.expect.admin.service.vo.component.html;

import java.util.ArrayList;
import java.util.List;

import com.expect.admin.service.vo.component.BaseVo;

public class CheckboxsVo extends BaseVo{

	private List<CheckboxVo> checkboxVos = new ArrayList<>();
	private StringBuilder htmlSb = new StringBuilder();
	private String name;

	public CheckboxsVo(String name) {
		this.name = name;
	}

	public void addCheckbox(String text, String value, boolean isChecked) {
		checkboxVos.add(new CheckboxVo(value, text, isChecked));
		if (isChecked) {
			htmlSb.append("<div class='md-checkbox checkbox-" + name + "'><input type='checkbox' id='checkbox-" + name
					+ "-" + value + "' value='" + value + "' class='md-check' checked/><label for='checkbox-" + name
					+ "-" + value + "'><span></span><span class='check'></span> <span class='box'></span>" + text
					+ "</label></div>");
		} else {
			htmlSb.append("<div class='md-checkbox checkbox-" + name + "'><input type='checkbox' id='checkbox-" + name
					+ "-" + value + "' value='" + value + "' class='md-check'/><label for='checkbox-" + name + "-"
					+ value + "'><span></span><span class='check'></span> <span class='box'></span>" + text
					+ "</label></div>");
		}
	}

	public void addCheckbox(String text, String value) {
		addCheckbox(text, value, false);
	}

	public List<CheckboxVo> getCheckboxVos() {
		return checkboxVos;
	}

	public String getHtml() {
		return htmlSb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public class CheckboxVo {
		private String value;
		private String text;
		private boolean isChecked;

		public CheckboxVo(String value, String text, boolean isChecked) {
			super();
			this.value = value;
			this.text = text;
			this.isChecked = isChecked;
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

		public boolean isChecked() {
			return isChecked;
		}

		public void setChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}

	}
}
