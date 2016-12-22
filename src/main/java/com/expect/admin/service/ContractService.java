package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.expect.admin.data.dao.AttachmentRepository;
import com.expect.admin.data.dao.ContractRepository;
import com.expect.admin.data.dao.LcjdbRepository;
import com.expect.admin.data.dao.LcjdgxbRepository;
import com.expect.admin.data.dao.RoleRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Attachment;
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
	@Autowired
	private AttachmentRepository attachmentRepository;
	
	@Transactional
	public String save(ContractVo contractVo, String[] attachmentId){
		Contract contract = new Contract(contractVo);
		UserVo userVo = userService.getLoginUser();
		User user = userRepository.findOne(userVo.getId());
		contract.setNhtr(user);
		if(attachmentId != null && attachmentId.length > 0) {
			List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
			if(attachmentList != null && attachmentList.size() > 0)
				contract.setAttachments(attachmentList);
		}
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
	public List<ContractVo> getContractByUserIdAndCondition(String userId, String condition, String lx) {
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		List<Contract> contractList = null;
		
		if(StringUtil.isBlank(condition)) return contractVoList;
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
			contractList = getHtspYspList(userId);
		}
		
		if(contractList == null) return contractVoList;
//		Map<String, String> lcjdbMap = getAllLcjdMapping();
		Lcjdb lcjd = lcjdbRepository.findOne(condition);
		User user = userRepository.findOne(userId);
		for (Contract contract : contractList) {
			if(StringUtil.equals(contract.getSfsc(), "Y")) continue;//过滤掉已删除的合同
			if(StringUtil.equals(lcjd.getShbm(), "Y"))
					if(!sfsybm(contract.getNhtr().getDepartments().iterator().next(), 
							user.getDepartments())) continue;
				
//			if(lcjd != null){
//				if (!StringUtil.isBlank(lcjd.getShbm()) && 
//						!StringUtil.equals(lcjd.getShbm(), contract.getNhtr().getDepartment().getId())) continue;
//				else if (!StringUtil.isBlank(lcjd.getShgs())){
//					Department parent = contract.getNhtr().getDepartment().getParentDepartment();
//					if(parent == null) continue;
//					if(!StringUtil.equals(lcjd.getShgs(), parent.getId())) continue;
//				}
//			}
			ContractVo contractVo = new ContractVo(contract);
//			if(!StringUtil.isBlank(contract.getHtshzt())) 
//				contractVo.setHtshzt(lcjdbMap.get(contract.getHtshzt()));
			contractVoList.add(contractVo);
		}
		return contractVoList;
	}
	
	/**
	 * 判断一个合同是不是属于一个部门
	 * @return true(是同一个部门)<br>
	 * false (不是同一个部门)
	 */
	private boolean sfsybm(Department contractDepartment, Set<Department> userDepartments){
		if(contractDepartment == null || userDepartments == null) return false;
		for (Department department : userDepartments) {
			if(StringUtil.equals(contractDepartment.getId(), department.getId())) return true;
		}
		return false;
	}

	private List<Contract> getHtspYspList(String userId) {
		List<Contract> contractList = contractRepository.findYspContract(userId, "不通过");
		List<Contract> contractList2 = contractRepository.findYspContract(userId, "通过");
		if(contractList == null || contractList.size() == 0) return contractList2;
		if(contractList2 == null || contractList2.size() == 0) return contractList;
		contractList.addAll(contractList2);
		return contractList;
	}
	
	/**
	 * 申请记录界面已审批合同
	 * @param userId
	 * @return
	 */
	public List<ContractVo> getSqjlYspList(String userId){
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		List<Contract> yspList = contractRepository.findByNhtr_idAndHtshzt(userId, "Y");
		List<Contract> yhtList = contractRepository.findByNhtr_idAndHtshzt(userId, "T");
		if(yspList != null){
			for (Contract contract1 : yspList) {
				ContractVo contractVo = new ContractVo(contract1);
				contractVo.setHtshzt("已审批");
				contractVoList.add(contractVo);
			}
		}
		if(yhtList != null){
			for (Contract contract : yhtList) {
				ContractVo contractVo = new ContractVo(contract);
				contractVo.setHtshzt("已回填");
				contractVoList.add(contractVo);
			}
		}
		return contractVoList;
	}
	
	/**
	 * 申请记录界面未审批合同
	 * @param userId
	 * @return
	 */
	public List<ContractVo> getSqjlWspList(String userId, String condition) {
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		List<Contract> wspList = contractRepository.findSqjlWspList(userId, condition);
		if(wspList != null && wspList.size() > 0)
			for (Contract contract : wspList) {
				ContractVo contractVo = new ContractVo(contract);
				contractVo.setHtshzt("待审批");
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
	 * 从数据库中删除合同，用于未提交以保存的合同草稿
	 * @param id
	 */
	public void delete(String id) {
		Contract contract = contractRepository.findById(id);
		if(contract == null) return;
		contractRepository.delete(contract);
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
	
	public List<ContractVo> searchContract(final String htbt, final String htbh, final Date startTime,
			final Date endTime, final String htzt, final String fqr) {
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		List<Contract> contractList = contractRepository.findAll(new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if(StringUtil.isBlank(htbt)) list.add(cb.like(root.get("htbt").as(String.class), "%" + htbt + "%"));
				if(StringUtil.isBlank(htbh)) list.add(cb.like(root.get("htbh").as(String.class), "%" + htbh + "%"));
				if(StringUtil.isBlank(htzt)) list.add(cb.equal(root.get("htzt").as(String.class), htzt));
				if(startTime != null && endTime != null) list.add(cb.between(root.get("sqsj").as(Date.class), startTime, endTime));
				if(StringUtil.isBlank(fqr)) list.add(cb.like(root.get("").as(String.class), "%" + htbt + "%"));//发起人还没有做
				Predicate[] predicate = new Predicate[list.size()];
				return cb.and(list.toArray(predicate));
			}
		});
		if(contractList == null) return contractVoList;
		for (Contract contract : contractList) {
			contractVoList.add(new ContractVo(contract));
		}
		return contractVoList;
	}
	

}
