package com.expect.admin.service;

import java.util.List;

import com.expect.admin.service.vo.DraftVO;

public interface DraftService {

	public boolean add(DraftVO draftVO);
	public boolean update(DraftVO draftVO);
	public boolean delete(DraftVO draftVO);
	
	/**
	 * 根据ID获取特定的拟稿
	 * @param draftID
	 * @return
	 */
	public DraftVO findDraft(String draftID);
	/**
	 * 根据 是否集团拟稿  ， 拟稿审核状态 获取特定的拟稿
	 * @param sfjtng
	 * @param ngshzt
	 * @return
	 */
	public List<DraftVO> findDraft(String sfjtng, String ngshzt);
}
