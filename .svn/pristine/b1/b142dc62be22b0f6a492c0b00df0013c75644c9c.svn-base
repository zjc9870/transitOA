package com.expect.admin.service.vo.component.html.datatable;

import java.util.ArrayList;
import java.util.List;

import com.expect.admin.service.vo.component.ResultVo;

public class DataTableRowVo extends ResultVo {

	private String checkbox = "<label class='mt-checkbox mt-checkbox-single mt-checkbox-outline'> <input type='checkbox' class='checkboxes' /><span></span></label>";

	private boolean isAddCheckbox;
	private boolean isCheckbox;
	// 表-行数据
	private List<String> data = new ArrayList<>();
	// 附加数据
	private List<String> addData = new ArrayList<>();

	public boolean isCheckbox() {
		return isCheckbox;
	}

	public void setCheckbox(boolean isCheckbox) {
		if (isCheckbox && !isAddCheckbox) {
			data.add(0, checkbox);
			isAddCheckbox = true;
		}
		if (!isCheckbox && isAddCheckbox) {
			data.remove(0);
		}
		this.isCheckbox = isCheckbox;
	}

	public List<String> getData() {
		return data;
	}

	public List<String> getAddData() {
		return addData;
	}

	public void addData(String value) {
		this.data.add(value);
	}

	public void addData(int index, String value) {
		this.data.add(index, value);
	}

	public void addAddData(String value) {
		this.addData.add(value);
	}

	@Override
	public String toString() {
		return "DataTableRowVo [checkbox=" + checkbox + ", isAddCheckbox=" + isAddCheckbox + ", isCheckbox="
				+ isCheckbox + ", data=" + data + "]";
	}

}
