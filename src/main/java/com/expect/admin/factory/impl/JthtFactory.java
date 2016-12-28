package com.expect.admin.factory.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.factory.WordXmlFactory;
import com.expect.admin.service.ContractService;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.utils.StringUtil;
import com.expect.admin.utils.WordXmlUtil;

import freemarker.template.TemplateException;

@Service("jthtFactory")
public class JthtFactory implements WordXmlFactory {

	private final String TEMPLATE_NAME = "jtht.ftl";
	@Autowired
	private ContractService contractService;
	
	@Override
	public byte[] create(String wjid) throws IOException, TemplateException {
		ContractVo contractVo = contractService.getContractById(wjid);
		if(contractVo == null) return null;
		List<LcrzbVo> lcrzbVoList = contractVo.getLcrzList(); 
		Map<String,Object> dataMap = new HashMap<String,Object>();
		
		setJbxx(contractVo, dataMap);//设置基本信息
		setLcxx(lcrzbVoList, dataMap);//设置流程信息
		
		byte[] content =  WordXmlUtil.create(dataMap, TEMPLATE_NAME) ;
		return content;
	}

	/**
	 * 设置显示流程信息
	 * @param lcrzbVoList
	 * @param dataMap
	 */
	private void setLcxx(List<LcrzbVo> lcrzbVoList, Map<String, Object> dataMap) {
		if(lcrzbVoList == null || lcrzbVoList.isEmpty()) return;
		for (LcrzbVo lcrzbVo : lcrzbVoList) {
			if(StringUtil.equals(lcrzbVo.getLcjd(), "部门审核")){
				dataMap.put("bmshjg", lcrzbVo.getCljg());
				dataMap.put("bmshr", lcrzbVo.getUserName());
				dataMap.put("bmshsj", lcrzbVo.getClsj());
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "部门负责人审核")) {
				dataMap.put("bmfzrshjg", lcrzbVo.getCljg());
				dataMap.put("bmfzrshr", lcrzbVo.getUserName());
				dataMap.put("bmfzrshsj", lcrzbVo.getClsj());
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "法务意见")){//法务意见
				dataMap.put("fwshjg", lcrzbVo.getCljg());
				dataMap.put("fwshr", lcrzbVo.getUserName());
				dataMap.put("fwshsj", lcrzbVo.getClsj());
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "资产管理部意见")){//资产管理部意见
				dataMap.put("zcglbshjg", lcrzbVo.getCljg());
				dataMap.put("zcglbshr", lcrzbVo.getUserName());
				dataMap.put("zcglbshsj", lcrzbVo.getClsj());
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "负责人意见")){//负责人意见
				dataMap.put("fzrshjg", lcrzbVo.getCljg());
				dataMap.put("fzrshr", lcrzbVo.getUserName());
				dataMap.put("fzrshsj", lcrzbVo.getClsj());
				continue;
			}
			if(StringUtil.equals(lcrzbVo.getLcjd(), "分管负责人意见")){//分管负责人意见
				dataMap.put("fgfzrshjg", lcrzbVo.getCljg());
				dataMap.put("fgfzrshr", lcrzbVo.getUserName());
				dataMap.put("fgfzrshsj", lcrzbVo.getClsj());
				continue;
			}
		}
	}

	/**
	 * 设置基本信息
	 * @param contractVo
	 * @param dataMap
	 */
	private void setJbxx(ContractVo contractVo, Map<String, Object> dataMap) {
		dataMap.put("sbd", contractVo.getSbd());
		dataMap.put("htbt", contractVo.getHtbt());
		dataMap.put("bh", contractVo.getBh());
		dataMap.put("htnr", contractVo.getHtnr());
		dataMap.put("nhtr", contractVo.getUserName());
		dataMap.put("nqdrq", contractVo.getNqdrq());
		dataMap.put("qx", contractVo.getQx());
	}

	@Override
	public String getFileName(String wjid) {
		ContractVo contractVo = contractService.getContractById(wjid);
		return contractVo.getHtbt() + contractVo.getBh()+"号.doc";
	}

}
