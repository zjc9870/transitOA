package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DmbRepository;
import com.expect.admin.data.dataobject.Dmb;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.DmbVo;
import com.expect.admin.utils.StringUtil;

/**
 * 代码表服务类
 * @author zcz
 *
 */
@Service
public class DmbService {

	@Autowired
	private DmbRepository dmbRepository;
	
	/**
	 * 根据类别编号，代码编号获取代码
	 * @param lbbh
	 * @param dmbh
	 * @return
	 */
	public DmbVo getDmbVoByLbbhAndDmbh(String lbbh, String dmbh) {
		if(StringUtil.isBlank(lbbh)) throw new BaseAppException("获取代码表记录是类别编号为空");
		if(StringUtil.isBlank(dmbh)) throw new BaseAppException("获取代码表记录是代码编号为空");
		Dmb dmb = dmbRepository.findByLbbhAndDmbh(lbbh, dmbh);
		return getDmbVoFromDmb(dmb);
	}
	
	/**
	 * 根据代码类别编号获取某类别所有代码
	 * @param lbbh
	 * @return
	 */
	public List<DmbVo> getDmbVoListByLbbh(String lbbh) {
		if(StringUtil.isBlank(lbbh)) throw new BaseAppException("获取代码表记录是类别编号为空");
		List<Dmb> dmbList = dmbRepository.findByLbbh(lbbh);
		return getDmbVoListFromDmbList(dmbList);
	}

	/**
	 *  将代码表List转换为dmbVo List
	 * @param dmbList
	 * @return
	 */
	private List<DmbVo> getDmbVoListFromDmbList(List<Dmb> dmbList) {
		if(dmbList == null || dmbList.isEmpty()) return new ArrayList<>(0);
		List<DmbVo> dmbVoList = new ArrayList<>(dmbList.size());
		for (Dmb dmb : dmbList) {
			dmbVoList.add(getDmbVoFromDmb(dmb));
		}
		return dmbVoList;
	}
	/**
	 * 将代码表转换为代码表Vo
	 * @param dmb
	 * @return
	 */
	private DmbVo getDmbVoFromDmb(Dmb dmb) {
		DmbVo dmbVo = new DmbVo();
		if(dmb == null) return dmbVo;
		BeanUtils.copyProperties(dmb, dmbVo);
		return dmbVo;
	}
}
