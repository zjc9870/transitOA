package com.expect.admin.service.convertor;

import org.springframework.beans.BeanUtils;

import com.expect.admin.data.dataobject.Draft;
import com.expect.admin.service.vo.DraftVO;

public class DraftConvertor {
	
	
	/**
	 * vo to do
	 * @param draftVO
	 * @return
	 */
	public static Draft convetor(DraftVO draftVO) {
		Draft draft = new Draft();
		BeanUtils.copyProperties(draftVO, draft);
		return draft;
	}
	
	public static DraftVO convetor(Draft draft) {
		DraftVO draftVO = new DraftVO();
		BeanUtils.copyProperties(draft, draftVO);
		return draftVO;
	}
}
