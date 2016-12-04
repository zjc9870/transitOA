package com.expect.admin.service.vo.component.html.datatable;

import java.util.ArrayList;
import java.util.List;

public class DataTableServerVo<T> {

	public static String checkbox = "<label class='mt-checkbox mt-checkbox-single mt-checkbox-outline'> <input type='checkbox' class='checkboxes' /><span></span></label>";

	protected int draw;
	protected int recordsTotal;
	protected int recordsFiltered;
	protected List<T> data=new ArrayList<>();

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
