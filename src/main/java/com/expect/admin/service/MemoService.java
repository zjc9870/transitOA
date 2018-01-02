package com.expect.admin.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.MemoRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Memo;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.component.html.memo.MemoItemVo;
import com.expect.admin.service.vo.component.html.memo.MemoVo;

/**
 * 备忘录Service
 */
@Service
public class MemoService {

	@Autowired
	private MemoRepository memoRepository;
	@Autowired
	private UserRepository userRepository;

	/**
	 * 获取某月的备忘录
	 * 
	 * @param userId
	 *            用户id
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 备忘录vo
	 */
	public MemoVo getMemosByDate(String userId, String year, String month) {
		List<Memo> memos = memoRepository.findByYearAndMonthAndUserIdOrderByTime(year, month, userId);
		MemoVo memoVo = new MemoVo();
		if (CollectionUtils.isEmpty(memos)) {
			memoVo.setMessage("暂无备忘录记录");
			return memoVo;
		}
		memoVo.setYear(year);
		memoVo.setMonth(month);
		for (Memo memo : memos) {
			memoVo.addMemoItem(memo.getDay(), memo.getId(), memo.getTime(), memo.getDescription());
		}
		memoVo.setResult(true);
		return memoVo;
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
	 * @return 备忘录itemvo
	 */
	public MemoItemVo save(String userId, String year, String month, String day, String time, String description) {
		MemoItemVo memoItemVo = new MemoItemVo();
		memoItemVo.setMessage("增加失败");
		if (StringUtils.isEmpty(userId)) {
			return memoItemVo;
		}
		if (StringUtils.isEmpty(time)) {
			memoItemVo.setMessage("时间不能为空");
			return memoItemVo;
		}
		if (StringUtils.isEmpty(description)) {
			memoItemVo.setMessage("备忘录内容不能为空");
			return memoItemVo;
		}
		User user = userRepository.findOne(userId);
		if (user == null) {
			return memoItemVo;
		}
		Memo memo = new Memo();
		memo.setYear(year);
		memo.setMonth(month);
		memo.setDay(day);
		memo.setTime(time);
		memo.setDescription(description);
		memo.setUser(user);
		Memo result = memoRepository.save(memo);
		if (result != null) {
			memoItemVo.setResult(true);
			memoItemVo.setMessage("增加成功");
			memoItemVo.setId(result.getId());
		}
		return memoItemVo;
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
	 * @return 备忘录itemvo
	 */
	public MemoItemVo update(String id, String time, String description) {
		MemoItemVo memoItemVo = new MemoItemVo();
		memoItemVo.setMessage("修改失败");
		if (StringUtils.isEmpty(id)) {
			return memoItemVo;
		}
		if (StringUtils.isEmpty(time)) {
			memoItemVo.setMessage("时间不能为空");
			return memoItemVo;
		}
		if (StringUtils.isEmpty(description)) {
			memoItemVo.setMessage("备忘录内容不能为空");
			return memoItemVo;
		}
		Memo memo = memoRepository.findOne(id);
		if (memo == null) {
			return memoItemVo;
		}
		memo.setTime(time);
		memo.setDescription(description);
		memoRepository.flush();

		memoItemVo.setResult(true);
		memoItemVo.setMessage("修改成功");
		return memoItemVo;
	}

	/**
	 * 删除备忘录
	 * 
	 * @param id
	 *            备忘录id
	 * @return 备忘录itemvo
	 */
	public MemoItemVo delete(String id) {
		MemoItemVo memoItemVo = new MemoItemVo();
		memoItemVo.setMessage("删除失败");
		if (StringUtils.isEmpty(id)) {
			return memoItemVo;
		}
		Memo memo = memoRepository.findOne(id);
		if (memo == null) {
			return memoItemVo;
		}
		memoRepository.delete(memo);
		memoItemVo.setResult(true);
		memoItemVo.setMessage("删除成功");
		return memoItemVo;
	}
}
