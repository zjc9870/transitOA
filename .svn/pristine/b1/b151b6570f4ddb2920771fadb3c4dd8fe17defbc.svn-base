package com.expect.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DraftRepository;
import com.expect.admin.data.dataobject.Draft;
import com.expect.admin.service.DraftService;
import com.expect.admin.service.convertor.DraftConvertor;
import com.expect.admin.service.vo.DraftVO;
import com.expect.admin.utils.StringUtil;

@Service
public class DraftServiceImpl implements DraftService {
	
	@Autowired
	DraftRepository draftRepitory;

	@Override
	public boolean add(DraftVO draftVO) {
		if(draftVO == null) throw new IllegalArgumentException();
		Draft draft = DraftConvertor.convetor(draftVO);
		draftRepitory.save(draft);
		return true;
	}

	@Override
	public boolean update(DraftVO draftVO) {
		if(draftVO == null || StringUtil.isBlank(draftVO.getId())) throw new IllegalArgumentException();
		Draft draft = DraftConvertor.convetor(draftVO);
		draftRepitory.save(draft);
		return true;
	}

	@Override
	public boolean delete(DraftVO draftVO) {
		if(draftVO == null || StringUtil.isBlank(draftVO.getId())) throw new IllegalArgumentException();
		Draft draft = DraftConvertor.convetor(draftVO);
		draftRepitory.delete(draft);
		return true;
	}

	@Override
	public DraftVO findDraft(String draftID) {
		if(StringUtil.isBlank(draftID)) throw new IllegalArgumentException();
		Draft draft = draftRepitory.findById(draftID);
		if(draft == null) return null;
		return DraftConvertor.convetor(draft);
	}

	@Override
	public List<DraftVO> findDraft(String sfjtng, String ngshzt) {
		List<DraftVO> draftVOList = new ArrayList<DraftVO>();
		List<Draft> draftList = draftRepitory.findByNgflAndNgshzt(sfjtng, ngshzt);
		if(draftList == null || draftList.size() == 0) return draftVOList;
		for (Draft draft : draftList) {
			DraftVO vo = DraftConvertor.convetor(draft);
			draftVOList.add(vo);
		}
		return draftVOList;
	}

}
