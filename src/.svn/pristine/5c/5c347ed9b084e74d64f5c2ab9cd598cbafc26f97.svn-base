package com.expect.admin.service.vo.component.html.datatable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataTableServerArrayVo extends DataTableServerVo<Map<String, String>> {

	private String checkbox = "<label class='mt-checkbox mt-checkbox-single mt-checkbox-outline'> <input type='checkbox' class='checkboxes' /><span></span></label>";

	private boolean isCheckbox;
	private int index = 0;

	public boolean isCheckbox() {
		return isCheckbox;
	}

	public void setCheckbox(boolean isCheckbox) {
		this.isCheckbox = isCheckbox;
	}

	public void addData(int index, Map<String, String> values) {
		if (isCheckbox) {
			values.put("0", checkbox);
		}
		data.add(index, values);
	}

	public void addData(Map<String, String> values) {
		if (isCheckbox) {
			values.put("0", checkbox);
		}
		data.add(values);
	}

	/**
	 * 重置map
	 */
	public void reset() {
		List<Map<String, String>> data = getData();
		Map<String, String> map = new HashMap<>();
		index = 0;
		if (isCheckbox) {
			map.put(index + "", checkbox);
			index++;
		}
		data.add(map);
	}

	/**
	 * 非第一个数据使用的方法
	 */
	public void addData(String value) {
		Map<String, String> map = data.get(data.size() - 1);
		map.put(index + "", value);
		index++;
	}

	/**
	 * 设置id
	 */
	public void setId(String id) {
		Map<String, String> map = data.get(data.size() - 1);
		map.put("DT_RowId", id);
	}
}
