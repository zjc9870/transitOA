package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expect.admin.data.dao.ContractRepository;
import com.expect.admin.data.dao.LcjdbRepository;
import com.expect.admin.data.dao.LcjdgxbRepository;
import com.expect.admin.data.dao.RoleRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Contract;
import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.Lcjdb;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.service.vo.RoleJdgxbGxbVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.DateUtil;
import com.expect.admin.utils.StringUtil;

@Service
public class ContractService {
	
	@Autowired
	private ContractRepository contractRepository;
	@Autowired
	private LcrzbService lcrzbService;
	@Autowired
	private UserService userService;
	@Autowired
	private LcService lcService;
	@Autowired
	private LcjdgxbRepository lcjdgxbRepository;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private RoleJdgxbGxbService roleJdgxbGxbService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private LcjdbRepository lcjdbRepository;
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public String save(ContractVo contractVo){
		Contract contract = new Contract(contractVo);
		UserVo userVo = userService.getLoginUser();
		User user = userRepository.findOne(userVo.getId());
		contract.setNhtr(user);
		contract = contractRepository.save(contract);
		LcrzbVo lcrzbVo = new LcrzbVo("新增", "");
		lcrzbService.save(lcrzbVo, contract.getId(), contract.getHtfl(), contractVo.getHtshzt());
		return contract.getId();
	}
	
	@Transactional
	public void updateContract(ContractVo contractVo) {
		if(contractVo == null || StringUtil.isBlank(contractVo.getId())) 
			throw new BaseAppException("要更新的合同ID为空，无法更新");
		Contract contract = contractRepository.findOne(contractVo.getId());
		if(contract == null) throw new BaseAppException("要更新的合同不存在！");
		update(contract, contractVo);
		contractRepository.save(contract);
	}
	
	public ContractVo getContractById(String contractId) {
		Contract contract = contractRepository.findOne(contractId);
		if(contract == null) throw new BaseAppException("id为 "+contractId+"的合同没有找到");
		ContractVo contractVo = new ContractVo(contract);//合同的基本信息
		List<LcrzbVo> lcrzbVoList = lcrzbService.getKxsLcrzbVoList(contractId);
		Map<String, String> lcjdbMap = getAllLcjdMapping();
		for (LcrzbVo lcrzbVo : lcrzbVoList) {
			if(!StringUtil.isBlank(lcrzbVo.getLcjd())) lcrzbVo.setLcjd(lcjdbMap.get(lcrzbVo.getLcjd()));
		}
		contractVo.setLcrzList(lcrzbService.getKxsLcrzbVoList(contractId));//合同的流程日志信息
		contractVo.setAttachmentList(attachmentService.getAttachmentsByXgid(contractId));//合同的附件信息
		return contractVo;
	}
	
	/**
	 * 还要修改
	 * @param userId
	 * @param condition
	 * @param start
	 * @param end
	 * @param lx
	 * @return
	 */
	public List<ContractVo> getContractByUserIdAndCondition(final String userId, final String condition, 
			final Date start, final Date end, String lx) {
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		List<Contract> contractList = null;
		
		if(StringUtil.isBlank(condition)) return contractVoList;
		Lcjdb lcjd = lcjdbRepository.findOne(condition);
		if(StringUtil.equals(lx, "wtj")){//未提交
			contractList = getWtjContracts(userId, condition);
		}
		if(StringUtil.equals(lx, "dsp") || StringUtil.equals(lx, "dht"))//待审批 || 待回填
			contractList = contractRepository.findByHtshzt(condition);
		if(StringUtil.equals(lx, "yht"))//已回填
			contractList = contractRepository.findYhtContract(userId);
		if(StringUtil.equals(lx, "yth"))//已退回
			contractList = contractRepository.findYthContract(userId, condition);
		if(StringUtil.equals(lx, "ysp")){ //已审批
			contractList = contractRepository.findYspContract(userId);
		}
		
		if(contractList == null) return contractVoList;
		Map<String, String> lcjdbMap = getAllLcjdMapping();
		for (Contract contract : contractList) {
			if(StringUtil.equals(contract.getSfsc(), "Y")) continue;//过滤掉已删除的合同
			if (!StringUtil.isBlank(lcjd.getShbm()) && 
					!StringUtil.equals(lcjd.getShbm(), contract.getNhtr().getDepartment().getId())) continue;
			else if (!StringUtil.isBlank(lcjd.getShgs())){
				Department parent = contract.getNhtr().getDepartment().getParentDepartment();
				if(parent == null) continue;
				if(!StringUtil.equals(lcjd.getShgs(), parent.getId())) continue;
			}
			ContractVo contractVo = new ContractVo(contract);
			if(!StringUtil.isBlank(contract.getHtshzt())) 
				contractVo.setHtshzt(lcjdbMap.get(contract.getHtshzt()));
			contractVoList.add(contractVo);
		}
		return contractVoList;
	}
	
	/**
	 * 获取未提交的合同列表
	 * @param userId
	 * @param condition
	 * @return
	 */
	private List<Contract> getWtjContracts(String userId, String condition){
		List<Contract> ythContractList = contractRepository.findYthContract(userId, condition);//已退回合同
		List<Contract> contractList = contractRepository.findByNhtr_idAndHtshzt(userId, condition);
		List<Contract> result = new ArrayList<Contract>();
		if(ythContractList == null || ythContractList.size() == 0) return contractList;
		Map<String, Contract> ythContractMap = new HashMap<String, Contract>();
		for (Contract contract2 : ythContractList) {
			ythContractMap.put(contract2.getId(), contract2);
		}
		for (Contract contract : contractList) {
			if(ythContractMap.get(contract.getId()) == null)
				result.add(contract);
		}
		return result;
	}
	
	/**
	 * 合同审批
	 * @param cljg
	 * @param message
	 * @param clnrid
	 * @param clnrfl
	 */
	@Transactional
	public void saveContractLcrz(String cljg, String message, String clnrid, String clnrfl) {
		String nextCondition;
		String sfth = "N";//合同是否被退回
		ContractVo contractVo = getContractById(clnrid);
		lcrzbService.save(new LcrzbVo(cljg, message), clnrid, clnrfl, contractVo.getHtshzt());
		if(StringUtil.equals(cljg, "不通过")){
			sfth = "Y";
			nextCondition = lcService.getThCondition(contractVo.getLcbs(), contractVo.getHtshzt());
			lcrzbService.setLcrzSfxs(clnrid, contractVo.getLcbs(), nextCondition);
		}else{
			//修改合同状态
			nextCondition = lcService.getNextCondition(contractVo.getLcbs(), contractVo.getHtshzt());
		}
		
		if(!StringUtil.isBlank(nextCondition)){
			contractVo.setHtshzt(nextCondition);
			contractVo.setSfth(sfth);
			updateContract(contractVo);
		}
	}
	
	/**
	 * 更新合同编号
	 * @param id 合同id
	 * @param htbh 合同编号
	 */
	public void updateHtbh(String id, String htbh) {
		contractRepository.updateHtbh(id, htbh);
	}
	
	private void update(Contract contract, ContractVo contractVo) {
		BeanUtils.copyProperties(contractVo, contract);
//		contract.setHtbt(contractVo.getHtbt());
//		contract.setHtnr(contractVo.getHtnr());
		if(!StringUtil.isBlank(contractVo.getNqdrq()))
			contract.setNqdrq(DateUtil.parse(contractVo.getNqdrq(), DateUtil.zbFormat));
//		contract.setQx(contractVo.getQx());
//		contract.setHtshzt(contractVo.getHtshzt());
//		contract.setSfth(contractVo.getSfth());
//		contract.setBh(contractVo.getBh());
	}
	
	/**
	 * 新申请合同时获取合同分类
	 * @return
	 */
	public String getHtfl() {
//		UserVo userVo = userService.getLoginUser();
//		User user = userRepository.findOne(userVo.getId());
		RoleJdgxbGxbVo roleJdgxbGxbVo= roleJdgxbGxbService.getWjzt("sq", "ht");
		Role role = roleRepository.findOne(roleJdgxbGxbVo.getRoleId());
		if(StringUtil.equals(role.getName(), "集团文员")) return "1";//集团合同
		if(StringUtil.equals(role.getName(), "东交公司文员")) return "3";//东交合同
		return "2";//非集团合同
	}
	
	/**
	 * 删除合同（软删除）
	 * @param id
	 */
	public void deleteContract(String id) {
		Contract contract = contractRepository.findById(id);
		if(contract == null) throw new BaseAppException("未找到要删除的合同 合同id = "+ id);
		contract.setSfsc("Y");
		contractRepository.save(contract);
	}
	
	/**
	 * 获取所有的合同的节点id和名字的map
	 * @return
	 */
	public Map<String, String> getAllLcjdMapping() {
		List<Lcjdb> lcjdbList = lcjdbRepository.findBySslc("1");
		Map<String, String> resultMap = new HashMap<String, String>();
		for (Lcjdb lcjdb : lcjdbList) {
			resultMap.put(lcjdb.getId(), lcjdb.getName());
		}
		return resultMap;
	}
	

}
