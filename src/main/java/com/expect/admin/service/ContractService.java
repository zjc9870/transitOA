package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		Contract contract = contractRepository.save(new Contract(contractVo));
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
//		LcrzbVo lcrzbVo = new LcrzbVo("修改", "");
//		lcrzbService.save(lcrzbVo, contractVo.getId(), contractVo.getHtfl());
	}
	
	@Transactional
	public void deleteContract(String contractId) {
		contractRepository.delete(contractId);
		lcrzbService.delete(contractId);
	}
	
	public ContractVo getContractById(String contractId) {
		Contract contract = contractRepository.findOne(contractId);
		if(contract == null) throw new BaseAppException("id为 "+contractId+"的合同没有找到");
		ContractVo contractVo = new ContractVo(contract);//合同的基本信息
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
		
		RoleJdgxbGxbVo  roleJdgxbGxbVo = roleJdgxbGxbService.getWjzt("sp", "");
		if(StringUtil.isBlank(roleJdgxbGxbVo.getRoleId()) || 
				StringUtil.isBlank(roleJdgxbGxbVo.getJdId())) return contractVoList;
		Lcjdb lcjd = lcjdbRepository.findOne(roleJdgxbGxbVo.getJdId());
//		if(start == null || end == null)
		if(StringUtil.equals(lx, "wtj")){//未提交
			contractList = contractRepository.findByNhtr_idAndHtshzt(userId, condition);
		}
		if(StringUtil.equals(lx, "dsp"))//待审批 || 待回填
			contractList = contractRepository.findByUserAndCondition(userId, condition);
		if(StringUtil.equals(lx, "dht"))//待回填
			contractList = contractRepository.findByHtshzt("Y");
		if(StringUtil.equals(lx, "yht"))//已回填
			contractList = contractRepository.findYhtContract(userId, start, end);
		if(StringUtil.equals(lx, "yth"))//已退回????
			contractList = contractRepository.findYhtContract(userId, start, end);
		if(StringUtil.equals(lx, "ysp")){ //已审批
			contractList = contractRepository.findYhtContract(userId, start, end);
		}
		
		if(contractList == null) return contractVoList;
		for (Contract contract : contractList) {
			if (!StringUtil.isBlank(lcjd.getShbm()) && 
					!StringUtil.equals(lcjd.getShbm(), contract.getNhtr().getDepartment().getId())) continue;
			else if (!StringUtil.isBlank(lcjd.getShgs())){
				Department parent = contract.getNhtr().getDepartment().getParentDepartment();
				if(parent == null) continue;
				if(!StringUtil.equals(lcjd.getShgs(), parent.getId())) continue;
			}
			contractVoList.add(new ContractVo(contract));
		}
		return contractVoList;
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
		ContractVo contractVo = getContractById(clnrid);
		lcrzbService.save(new LcrzbVo(cljg, message), clnrid, clnrfl, contractVo.getHtshzt());
		if(StringUtil.equals(cljg, "N")){
			nextCondition = lcService.getThCondition(contractVo.getLcbs(), contractVo.getHtshzt());
			lcrzbService.setLcrzSfxs(clnrid, contractVo.getLcbs(), nextCondition);
		}else{
			//修改合同状态
			nextCondition = lcService.getNextCondition(contractVo.getLcbs(), contractVo.getHtshzt());
		}
		
		if(!StringUtil.isBlank(nextCondition)) contractVo.setHtshzt(nextCondition);
		updateContract(contractVo);
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
		contract.setHtbt(contractVo.getHtbt());
		contract.setHtnr(contractVo.getHtnr());
		if(!StringUtil.isBlank(contractVo.getNqdrq()))
		contract.setNqdrq(DateUtil.parse(contractVo.getNqdrq(), DateUtil.webFormat));
		contract.setQx(contractVo.getQx());
	}
	
	/**
	 * 获取用户申请的合同的流程Id
	 * @return
	 */
	public String getLcCategory(){
		UserVo userVo = userService.getLoginUser();
		return "";
	}
	
	/**
	 * 新申请合同时获取合同分类
	 * @return
	 */
	public String getHtfl() {
		UserVo userVo = userService.getLoginUser();
		User user = userRepository.findOne(userVo.getId());
		RoleJdgxbGxbVo roleJdgxbGxbVo= roleJdgxbGxbService.getWjzt("sq", "ht");
		Role role = roleRepository.findOne(roleJdgxbGxbVo.getRoleId());
		if(StringUtil.equals(role.getName(), "集团文员")) return "1";//集团合同
		if(StringUtil.equals(role.getName(), "东交公司文员")) return "3";//东交合同
		return "2";//非集团合同
	}
	

}
