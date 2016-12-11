package com.expect.admin.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.LcbRepository;
import com.expect.admin.data.dao.LcjdbRepository;
import com.expect.admin.data.dao.LcjdgxbRepository;
import com.expect.admin.data.dataobject.Lcb;
import com.expect.admin.data.dataobject.Lcjdb;
import com.expect.admin.data.dataobject.Lcjdgxb;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.LcVo;
import com.expect.admin.utils.StringUtil;

@Service
public class lcService {

	@Autowired
	private LcbRepository lcbRepository;
	@Autowired
	private LcjdbRepository lcjdbRepository;
	@Autowired
	private LcjdgxbRepository lcjdgxbRepository;
	
	/**
	 * 集团合同流程代码
	 */
	public static final String JTHT_LC = "1";
	/**
	 * 非集团合同流程代码
	 */
	public static final String FJTHT_LC = "2";
	/**
	 * 东交合同流程代码
	 */
	public static final String DJHT_LC = "3";
	
	public LcVo getDefaultLc(String category) {
		Lcb lcb = lcbRepository.findOne(category);
		return new LcVo();
	}
	
	/**
	 * 获取某人流程的起始状态
	 * @param lcCategory
	 * @return
	 */
	public String getStartCondition(String lcCategory) {
		return getJd(lcCategory, Lcjdb.START_CATEGORY);
	}
	
	/**
	 * 获取当前状态在流程上的下一个状态
	 * 如果下一个状态唯一就直接返回
	 * 如果下一个状态不唯一就将后面多个同时进行的状态组合成“middle_状态1_状态2”来表示一个中间态
	 * @param lcCategory
	 * @param currentCondition
	 * @return
	 * @throws BaseAppException 
	 */
	public String getNextCondition(String lcCategory, String currentCondition) {
		if(StringUtil.isEmpty(currentCondition) || StringUtil.isEmpty(lcCategory)) throw new BaseAppException();
		Lcb lcb = lcbRepository.findOne(lcCategory);
		Set<Lcjdgxb> curLcjdSet = new HashSet<Lcjdgxb>();
		if(currentCondition.startsWith("middle")){
			String[] curConditons = currentCondition.split("_");
			Set<String> curConditionSet = new HashSet<String>();
			for (int i = 1; i < curConditons.length; i++) {
				curConditionSet.add(curConditons[i]);
			}
			if(curConditionSet.size() == 1) {
				currentCondition = curConditionSet.iterator().next();
			}else {
				for (String curCon : curConditionSet) {
					curLcjdSet.add(getCurLcjd(curCon, lcb));
				}
			}
		}else {
			curLcjdSet.add(getCurLcjd(currentCondition, lcb));
		}
		return constructNextCondition(curLcjdSet);
	}

	private String constructNextCondition(Set<Lcjdgxb> curLcjdSet) {
		if(curLcjdSet.size() > 1) {
			StringBuilder sb = new StringBuilder("middle_");
			for (Lcjdgxb curLcjd : curLcjdSet) {
				if(StringUtil.equals(curLcjd.getSftb(), "Y")) continue;
				sb.append(curLcjd.getJsjd()).append("_");
			}
			return sb.substring(0, sb.length() - 1).toString();
		}else return curLcjdSet.iterator().next().getJsjd();
	}

	private Lcjdgxb getCurLcjd(String currentCondition, Lcb lcb) {
		Lcjdgxb curlcjd = lcjdgxbRepository.findByLcbsAndKsjd(lcb.getId(), currentCondition);
		if(curlcjd ==  null) throw new BaseAppException("未找到当前的状态节点");
		return curlcjd;
		
		
//		return lcjdgxList.get(0).getJsjd();
	}
	
	public String getJd(String lcCategory, String jdCategory) {
		Lcb lcb = lcbRepository.findOne(lcCategory);
		if(lcb == null) return null;
		Lcjdb lcjdb = lcjdbRepository.findBySslcAndCategory(lcb.getId(), jdCategory);
		if(lcjdb == null) return null;
		return lcjdb.getId();
	}
	
}
