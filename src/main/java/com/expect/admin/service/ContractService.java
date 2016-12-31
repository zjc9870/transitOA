package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expect.admin.data.dao.AttachmentRepository;
import com.expect.admin.data.dao.ContractRepository;
import com.expect.admin.data.dao.LcjdbRepository;
import com.expect.admin.data.dao.RoleRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.data.dataobject.Contract;
import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.Lcjdb;
import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.AttachmentVo;
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
		
		//处理合同和附件的对应关系
		if(attachmentId != null && attachmentId.length > 0) {
			List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
			if(attachmentList != null && attachmentList.size() > 0)
				contract.setAttachments(new HashSet<>(attachmentList));
		}else contract.setAttachments(new HashSet<>());
		
		contract = contractRepository.save(contract);
		return contract.getId();
	}
	
	/**
	 * 新合同的保存
	 * @param contractVo
	 * @param bczl  保存种类（提交：“tj”， 保存 ： “bc”）
	 * @param attachmentId
	 */
	public void newContractSave(ContractVo contractVo, String bczl, String[] attachmentId) {
		String htfl = getHtfl();
		contractVo.setHtfl(htfl);
		String lcbs = htfl;
		String condition = getNewContractCondition(bczl, lcbs);
		contractVo.setHtshzt(condition);//合同审核状态
		contractVo.setLcbs(lcbs);//流程标识
		if(StringUtil.equals(bczl, "tj"))
			contractVo.setSqsj(DateUtil.format(new Date(), DateUtil.fullFormat));
		String id = save(contractVo, attachmentId);
		contractVo.setId(id);
		if(StringUtil.equals(bczl, "tj")) {
			addXzLcrz(id, contractVo.getHtfl(), lcService.getStartCondition(lcbs));//如果是新增就增加一条日志记录 
		}
	}

	private String getNewContractCondition(String bczl, String lcbs) {
		String startCondition = lcService.getStartCondition(lcbs);
		if(!StringUtil.equals(bczl, "tj")) return startCondition;
		else return lcService.getNextCondition(lcbs, startCondition);
	}
	/**
	 * 新增是增加流程日志
	 * @param contract
	 */
	private void addXzLcrz(String id, String htfl, String htshzt) {
		LcrzbVo lcrzbVo = new LcrzbVo("新增", "");
		lcrzbService.save(lcrzbVo, id, htfl, htshzt);
	}
	
	@Transactional
	public void updateContract(ContractVo contractVo, String[] attachmentId) {
		if(contractVo == null || StringUtil.isBlank(contractVo.getId())) 
			throw new BaseAppException("要更新的合同ID为空，无法更新");
		Contract contract = contractRepository.findOne(contractVo.getId());
		if(contract == null) throw new BaseAppException("要更新的合同不存在！");
		update(contract, contractVo);
		//更新合同的附件列表
		if(attachmentId != null && attachmentId.length > 0) {
			List<Attachment> attachmentList = attachmentRepository.findByIdIn(attachmentId);
			if(attachmentList != null && attachmentList.size() > 0)
				contract.setAttachments(new HashSet<>(attachmentList));
		}
		contractRepository.save(contract);
	}
	
	public ContractVo getContractById(String contractId) {
		Contract contract = contractRepository.findOne(contractId);
		if(contract == null) throw new BaseAppException("id为 "+contractId+"的合同没有找到");
		ContractVo contractVo = new ContractVo(contract);//合同的基本信息
		List<LcrzbVo> lcrzbVoList = lcrzbService.convert(contract.getLcrzSet());
		Map<String, String> lcjdbMap = getAllLcjdMapping();
		for (LcrzbVo lcrzbVo : lcrzbVoList) {
			if(!StringUtil.isBlank(lcrzbVo.getLcjd())){
				String lcjdName = lcjdbMap.get(lcrzbVo.getLcjd());
				lcrzbVo.setLcjd(lcjdName);
			}
		}
//		contractVo.setLcrzList(lcrzbService.getKxsLcrzbVoList(contractId));//合同的流程日志信息
		contractVo.setLcrzList(lcrzbVoList);
		List<AttachmentVo> attachmentVoList = getContractAttachment(contract);
		contractVo.setAttachmentList(attachmentVoList);
//		contractVo.setAttachmentList(attachmentService.getAttachmentsByXgid(contractId));//合同的附件信息
		return contractVo;
	}

	/**
	 * 获取一个合同相关的附件信息
	 * @param contract
	 * @return
	 */
	private List<AttachmentVo> getContractAttachment(Contract contract) {
		Set<Attachment> attachmentList = contract.getAttachments();
		List<AttachmentVo> attachmentVoList = new ArrayList<>();
		if(attachmentList != null && !attachmentList.isEmpty())
			for (Attachment attachment : attachmentList) {
				AttachmentVo attachementVo = new AttachmentVo();
				BeanUtils.copyProperties(attachment, attachementVo);
				attachmentVoList.add(attachementVo);
			}
		return attachmentVoList;
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
		List<Contract> contractList = null;
		
		if(StringUtil.isBlank(condition)) return new ArrayList<>();
		if(StringUtil.equals(lx, "wtj")){//未提交
			contractList = getWtjContracts(userId, condition);
		}
		if(StringUtil.equals(lx, "dsp") || StringUtil.equals(lx, "dht"))//待审批 || 待回填
			contractList = contractRepository.findByHtshztOrderBySqsjDesc(condition);
		if(StringUtil.equals(lx, "yht"))//已回填
			contractList = contractRepository.findYhtContract(userId);
		if(StringUtil.equals(lx, "yth"))//已退回
			contractList = contractRepository.findYthContract(userId, condition);
		if(StringUtil.equals(lx, "ysp")){ //已审批（已审批就是根据个人取出的所以不需要再进行过滤）
			return getHtspYspList(userId);
		}
		
		return filter(userId, condition, contractList);
	}

	/**
	 * 对数据进行过滤，并设置合同的状态
	 * 1.过滤掉已经被标记为删除的合同
	 * 2.如果是部门内部审批时过滤掉其他部门的合同
	 * @param userId
	 * @param condition
	 * @param contractList
	 * @return
	 */
	private List<ContractVo> filter(String userId, String condition, List<Contract> contractList) {
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		if(contractList == null) return contractVoList;
		Map<String, String> lcjdbMap = getAllLcjdMapping();
		Lcjdb lcjd = lcjdbRepository.findOne(condition);
		User user = userRepository.findOne(userId);
		for (Contract contract : contractList) {
			if(StringUtil.equals(contract.getSfsc(), "Y")) continue;//过滤掉已删除的合同
			if(StringUtil.equals(lcjd.getShbm(), "Y"))
					if(!sfsybm(contract.getNhtr().getDepartments().iterator().next(), 
							user.getDepartments())) continue;
			ContractVo contractVo = new ContractVo(contract);
			
			convertHtshzt(lcjdbMap, contractVo);
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

	/**
	 * @param userId
	 * @return
	 */
	private List<ContractVo> getHtspYspList(String userId) {
		List<ContractVo> contractVoList = new ArrayList<>();
		List<Contract> contractList = contractRepository.findYspContract(userId);
		Map<String, String> lcjdbMap = getAllLcjdMapping();
		if(contractList != null && !contractList.isEmpty()){
			for (Contract contract : contractList) {
				Set<Lcrzb> lcrzbList = contract.getLcrzSet();
				
				ContractVo contractVo = new ContractVo(contract);
				setSpjg(userId, lcrzbList, contractVo);
				convertHtshzt(lcjdbMap, contractVo);
				contractVoList.add(contractVo);
			}
		}
//		sortContractVoListBySqsjDesc(contractVoList);
		return contractVoList;
	}

	/**
	 * 设置某人已审批合同的审批意见（某人对合同的最后一次的审批结果）
	 * @param userId
	 * @param lcrzbList
	 * @param lcrzbListOfUser
	 * @param contractVo
	 */
	private void setSpjg(String userId, Set<Lcrzb> lcrzbList, ContractVo contractVo) {
		List<Lcrzb> lcrzbListOfUser = new ArrayList<>();
		if(lcrzbList != null && !lcrzbList.isEmpty()){
			for (Lcrzb lcrzb : lcrzbList) {
				if(StringUtil.equals(lcrzb.getUser().getId(), userId))
					lcrzbListOfUser.add(lcrzb);
			}
		}
		if(!lcrzbListOfUser.isEmpty()){
			Collections.sort(lcrzbListOfUser, new Comparator<Lcrzb>() {
				@Override
				public int compare(Lcrzb c1, Lcrzb c2) {
					if(c1.getClsj() == null) return -1;
					if(c2.getClsj() == null) return 1;
					long dif = DateUtil.getDiffSeconds(c1.getClsj(), c2.getClsj());
					return (dif > 0) ? -1 : 
						((dif < 0) ? 1 : 0);
				}
			});
			Lcrzb lastLcrz = lcrzbListOfUser.get(0);
			contractVo.setSpyj(lastLcrz.getCljg());
		}else contractVo.setSpyj("");
	}

	/**
	 * 合同审核状态由代码转换为对应汉字
	 * @param lcjdbMap
	 * @param contractVo
	 */
	private void convertHtshzt(Map<String, String> lcjdbMap, ContractVo contractVo) {
		if(!StringUtil.isBlank(contractVo.getHtshzt())) 
			contractVo.setHtshzt(lcjdbMap.get(contractVo.getHtshzt()));
		else contractVo.setHtshzt("");
	}

	/*private void sortContractVoListBySqsjDesc(List<ContractVo> contractVoList) {
		Collections.sort(contractVoList, new Comparator<ContractVo>() {

			@Override
			public int compare(ContractVo c1, ContractVo c2) {
				if(StringUtil.isBlank(c1.getSqsj())) return -1;
				if(StringUtil.isBlank(c2.getSqsj())) return 1;
				Date d1 = DateUtil.parse(c1.getSqsj(), DateUtil.fullFormat);
				Date d2 = DateUtil.parse(c2.getSqsj(), DateUtil.fullFormat);
				long dif = DateUtil.getDiffSeconds(d1, d2);
				return (dif > 0) ? -1 : 
					((dif < 0) ? 1 : 0);
			}
		});
	}*/
	
	/**
	 * 申请记录界面已审批合同
	 * @param userId
	 * @return
	 */
	public List<ContractVo> getSqjlYspList(String userId){
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		List<Contract> yspList = contractRepository.findByNhtr_idAndHtshztOrderBySqsjDesc(userId, "Y");
		List<Contract> yhtList = contractRepository.findByNhtr_idAndHtshztOrderBySqsjDesc(userId, "T");
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
		List<Contract> contractList = contractRepository.findByNhtr_idAndHtshztOrderBySqsjDesc(userId, condition);
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
		ContractVo contractVo = getContractById(clnrid);
		String nextCondition;//合同的下一个状态，根据是否被退回确定
		String curCondition = contractVo.getHtshzt();
		String sfth = "N";//合同是否被退回
		//添加流程日志
		lcrzbService.save(new LcrzbVo(cljg, message), clnrid, clnrfl, curCondition);
		//处理意见是“不通过”，并且不是法务审核就是退回
		if(StringUtil.equals(cljg, "不通过") && !StringUtil.equals(curCondition, "3")){
			sfth = "Y";
			nextCondition = lcService.getThCondition(contractVo.getLcbs(), curCondition);
			lcrzbService.setLcrzSfxs(clnrid, contractVo.getLcbs(), nextCondition);
		}else{
			//修改合同状态
			nextCondition = lcService.getNextCondition(contractVo.getLcbs(), curCondition);
		}
		
		if(!StringUtil.isBlank(nextCondition)){
			contractVo.setHtshzt(nextCondition);
			contractVo.setSfth(sfth);
			updateContract(contractVo, null);
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
		if(!StringUtil.isBlank(contractVo.getNqdrq()))
			contract.setNqdrq(DateUtil.parse(contractVo.getNqdrq(), DateUtil.zbFormat));
	}
	
	/**
	 * 新申请合同时获取合同分类
	 * @return
	 */
	public String getHtfl() {
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
		List<Lcjdb> lcjdbList2 = lcjdbRepository.findBySslc("2");
		List<Lcjdb> lcjdbList3 = lcjdbRepository.findBySslc("3");
		Map<String, String> resultMap = new HashMap<String, String>();
		for (Lcjdb lcjdb : lcjdbList) {
			resultMap.put(lcjdb.getId(), lcjdb.getName());
		}
		for (Lcjdb lcjdb : lcjdbList2) {
			resultMap.put(lcjdb.getId(), lcjdb.getName());
		}
		for (Lcjdb lcjdb : lcjdbList3) {
			resultMap.put(lcjdb.getId(), lcjdb.getName());
		}
		resultMap.put("T", "已回填");
		resultMap.put("Y", "审核完成");
		return resultMap;
	}
	
	/**
	 * @param htbt 合同标题
	 * @param htbh 合同编号
	 * @param startTime 开始时间（搜索的时间段，以申请时间为准）
	 * @param endTime 结束时间
	 * @param htzt 合同状态（0 ：全部， 1：待审批， 2 ：已审批， 3 ： 已回填）
	 * @param fqr 发起人（合同的申请人）姓名
	 * @return
	 */
	public List<ContractVo> searchContract(final String htbt, final String htbh, final Date startTime,
			final Date endTime, final String htzt, final String fqr) {
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		List<Contract> contractList = contractRepository.findAll(new Specification<Contract>() {
			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				//合同标题
				if(!StringUtil.isBlank(htbt)) list.add(cb.like(root.get("htbt").as(String.class), "%" + htbt + "%"));
				//合同编号
				if(!StringUtil.isBlank(htbh)) list.add(cb.like(root.get("bh").as(String.class), "%" + htbh + "%"));
				
				//合同状态
				if(StringUtil.equals(htzt, "1")){
					list.add(cb.notEqual(root.get("htshzt").as(String.class), "Y"));
					list.add(cb.notEqual(root.get("htshzt").as(String.class), "T"));
				}
				if(StringUtil.equals(htzt, "2"))
					list.add(cb.equal(root.get("htshzt").as(String.class), "Y"));
				if(StringUtil.equals(htzt, "3"))
					list.add(cb.equal(root.get("htshzt").as(String.class), "T"));
				
				//申请时间（时间段内的申请）
				if(startTime != null && endTime != null) list.add(cb.between(root.get("sqsj").as(Date.class), startTime, endTime));
				Join<Contract, User> leftJoin = root.join(root.getModel().getSingularAttribute("nhtr", User.class), JoinType.LEFT);
//				if(!StringUtil.isBlank(fqr)) list.add(cb.equal(leftJoin.get("id").as(String.class), fqr));
				//合同申请人（发起人）
				if(!StringUtil.isBlank(fqr)) list.add(cb.equal(leftJoin.get("fullName").as(String.class), fqr));//发起人
				Predicate[] predicate = new Predicate[list.size()];
				Predicate psssredicate = cb.and(list.toArray(predicate));
				return psssredicate;
			}
		});
		if(contractList == null) return contractVoList;
		for (Contract contract : contractList) {
			ContractVo contractVo = new ContractVo(contract);
			if(StringUtil.equals(contract.getHtshzt(), "Y")) contractVo.setHtshzt("已审批");
			else if(StringUtil.equals(contract.getHtshzt(), "T")) contractVo.setHtshzt("已回填");
			else contractVo.setHtshzt("待审批");
			contractVoList.add(contractVo);
		}
		return contractVoList;
	}
	

}
