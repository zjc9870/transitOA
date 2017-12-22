package com.expect.admin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.expect.admin.service.MemoService;
import com.expect.admin.service.vo.component.html.memo.MemoItemVo;
import com.expect.admin.service.vo.component.html.memo.MemoVo;

/**
 * 备忘录controller
 */
@Controller
@RequestMapping("/admin/memo")
public class MemoController {

	@Autowired
	private MemoService memoService;

	/**
	 * 根据用户id/年/月，获取当月的备忘录列表
	 *
	 * @param userId
	 *            用户id
	 * @param year
	 *            年
	 * @param month
	 *            月
	 */
	@RequestMapping(value = "/getMemoItems")
	@ResponseBody
	public MemoVo getMemoItems(String userId, String year, String month) {
		return memoService.getMemosByDate(userId, year, month);
	}

	/**
	 * 保存备忘录
	 * 
	 * @param userId
	 *            用户id
	 * @param time
	 *            时间
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param desc
	 *            描述
	 */
	@RequestMapping(value = "/addMemoItem")
	@ResponseBody
	public MemoItemVo addMemoItem(String userId, String year, String month, String day, String time, String desc) {
		return memoService.save(userId, year, month, day, time, desc);
	}

	/**
	 * 修改备忘录
	 * 
	 * @param id
	 *            备忘录id
	 * @param time
	 *            时间
	 * @param desc
	 *            描述
	 */
	@RequestMapping(value = "/updateMemoItem")
	@ResponseBody
	public MemoItemVo updateMemoItem(String id, String time, String desc) {
		return memoService.update(id, time, desc);
	}

	/**
	 * 修改备忘录
	 * 
	 * @param id
	 *            备忘录id
	 */
	@RequestMapping(value = "/deleteMemoItem")
	@ResponseBody
	public MemoItemVo deleteMemoItem(String id) {
		return memoService.delete(id);
	}
}
