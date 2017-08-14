package com.expect.admin.service.vo.component.html.memo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.expect.admin.service.vo.component.ResultVo;

/**
 * 备忘录vo
 */
public class MemoVo extends ResultVo {

	private String year;
	private String month;
	/**
	 * 存取一个月的数据。 String：天；List：备忘录详情
	 */
	private Map<String, List<MemoItemVo>> memoItemMap;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Map<String, List<MemoItemVo>> getMemoItemMap() {
		return memoItemMap;
	}

	public void setMemoItemMap(Map<String, List<MemoItemVo>> memoItemMap) {
		this.memoItemMap = memoItemMap;
	}

	public void addMemoItem(String day, String id, String time, String desc) {
		MemoItemVo memoItem = new MemoItemVo();
		memoItem.setId(id);
		memoItem.setTime(time);
		memoItem.setDesc(desc);
		if (memoItemMap == null) {
			memoItemMap = new HashMap<>();
		}
		List<MemoItemVo> memoItems = memoItemMap.get(day);
		if (memoItems == null) {
			memoItems = new ArrayList<>();
			memoItemMap.put(day, memoItems);
		}
		memoItems.add(memoItem);
	}

}
