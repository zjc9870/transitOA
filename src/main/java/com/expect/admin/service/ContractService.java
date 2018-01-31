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

import com.expect.admin.data.dao.*;
import com.expect.admin.data.dataobject.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

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
    
    /**
     * 撤销状态
     */
    private static final String REVOCATION_CONDITION = "revocation";
	
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
	@Autowired
	private LcrzbRepository lcrzbRepository;
	@Autowired
	private RoleJdgxbGxbRepository roleJdgxbGxbRepository;
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
		}else contract.setAttachments(new HashSet<Attachment>());
		
		contract = contractRepository.save(contract);
		return contract.getId();
	}
	
	/**
	 * 新合同的保存
	 * @param contractVo
	 * @param bczl  保存种类（提交：“tj”， 保存 ： “bc”）
	 * @param attachmentId
	 */
	@Transactional
	public void  newContractSave(ContractVo contractVo, String bczl, String[] attachmentId) {
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
		    addXzLcrz(id, "ht", lcService.getStartCondition(lcbs));//如果是新增就增加一条日志记录
		}
	}

	private String getNewContractCondition(String bczl, String lcbs) {
		String startCondition = lcService.getStartCondition(lcbs);
		if(!StringUtil.equals(bczl, "tj")) return startCondition;
		else return lcService.getNextCondition(lcbs, startCondition);
	}
	/**
	 * 新增时增加流程日志
	 * @param contract
	 */
	@Transactional
	public void addXzLcrz(String id, String htfl, String htshzt) {
		LcrzbVo lcrzbVo = new LcrzbVo("新增", "");
		String lcrzId = lcrzbService.save(lcrzbVo, id, htfl, htshzt);
		bindContractWithLcrz(id, lcrzId);
	}

	/**
	 * 流程日志和合同的绑定
	 * @param id
	 * @param lcrzId
	 */
	public void bindContractWithLcrz(String ContractId, String lcrzId) {
		Contract contract = contractRepository.findOne(ContractId);
		Lcrzb lcrz = lcrzbRepository.findOne(lcrzId);
		contract.getLcrzSet().add(lcrz);
		contractRepository.save(contract);
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
	
	/**
	 * 1.根据id找到相关合同，并将数据写入到对用Vo中
	 * 2.获取相关的流程日志
	 * 3.设置个流程日志相关联的合同状态的名称
	 * 4.如果合同的状态不是已撤销（REVOCATION_CONDITION）就加载合同的附件信息，否则就不加载
	 * @param contractId
	 * @return
	 */
	public ContractVo getContractById(String contractId) {
		Contract contract = contractRepository.findOne(contractId);
		if(contract == null) throw new BaseAppException("id为 "+contractId+"的合同没有找到");
		ContractVo contractVo = new ContractVo(contract);//合同的基本信息
		List<LcrzbVo> lcrzbVoList = LcrzbService.convert(contract.getLcrzSet());
		Map<String, String> lcjdbMap = getAllLcjdMapping();
		for (LcrzbVo lcrzbVo : lcrzbVoList) {
			if(!StringUtil.isBlank(lcrzbVo.getLcjd())){
				String lcjdName = lcjdbMap.get(lcrzbVo.getLcjd());
				lcrzbVo.setLcjd(lcjdName);
			}
		}
//		contractVo.setLcrzList(lcrzbService.getKxsLcrzbVoList(contractId));//合同的流程日志信息
		contractVo.setLcrzList(lcrzbVoList);
		
		//如果合同的状态不是已撤销就显示合同的附件信息，否则就不显示附件信息
		if(!StringUtil.equals(contract.getHtshzt(), REVOCATION_CONDITION)){
		    List<AttachmentVo> attachmentVoList = getContractAttachment(contract);
		    contractVo.setAttachmentList(attachmentVoList);
		}
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
	 * @param userId
	 * @param condition
	 * @param start
	 * @param end
	 * @param lx
	 * @return
	 */
	@Cacheable(cacheName = "CONTRACT_CACHE")
	public List<ContractVo> getContractByUserIdAndCondition(String userId, String condition, String lx) {
		List<Contract> contractList = null;

		if(StringUtil.isBlank(condition)) return new ArrayList<>();
		if(StringUtil.equals(lx, "wtj")){//未提交
			contractList = getWtjContracts(userId, condition);
		}
		if(StringUtil.equals(lx, "dsp") ){
			//待审批 || 待回填
			contractList = contractRepository.findByHtshztOrderBySqsjDesc(condition);

//			UserVo userVo = userService.getLoginUser();
//			if (userVo.getRoleName().equals("资产管理部合同审核员")){
//				List<Contract> contractList1 = contractRepository.findByHtshztOrderBySqsjDesc("3");
//				List<Contract> contractList2 = contractRepository.findByHtshztOrderBySqsjDesc("b1");
//				if (contractList1 !=null&&contractList1.size() >0){
//					for (Contract contract:contractList1){
//						contractList.add(contract);
//					}
//				}
//				if (contractList2 !=null&&contractList2.size() >0){
//					for (Contract contract:contractList2){
//						contractList.add(contract);
//					}
//				}
//			}
//			if (userVo.getRoleName().equals("法务")){
//				List<Contract> contractList1 = contractRepository.findByHtshztOrderBySqsjDesc("b2");
//				if (contractList1 !=null&&contractList1.size() >0){
//					for (Contract contract:contractList1){
//						contractList.add(contract);
//					}
//				}
//
//			}

		}
		if (StringUtil.equals(lx, "dht")){
			contractList = contractRepository.findDhtContract(userId);
		}

		if(StringUtil.equals(lx, "yht")) {
			//已回填
			contractList = contractRepository.findYhtContract(userId);
		}

		if(StringUtil.equals(lx, "yth"))//已撤销的合同(“yth”是历史原因已退回)
		    //所有已经审批过该合同的人可以看到这条合同的撤销记录
			contractList = getYthContractList(userId);
		if(StringUtil.equals(lx, "ysp")){ //已审批（已审批就是根据个人取出的所以不需要再进行过滤）
			return getHtspYspList(userId, condition);
		}
		
		return filter(userId, condition, contractList);
	}
	

    /**
     * 获取已退回的合同列表
     * @param loginUserId
     * @return
     */
    private List<Contract> getYthContractList(String loginUserId) {
        if(isFzr(loginUserId)){
            return fzrYthContractList();
        }else {
            return contractRepository.findByUserAndCondition(loginUserId, REVOCATION_CONDITION);
        }
    }

    /**
     * 判断当前登录用户是不是有 “负责人”角色
     * @param loginUserId
     * @return
     */
    private boolean isFzr(String loginUserId) {
        User user = userRepository.getOne(loginUserId);
        Set<Role> roleSet = user.getRoles();
        for (Role role : roleSet) {
            if(StringUtil.equals(role.getName(), "负责人")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 负责人只能看到资产管理部已经审核过的已撤销合同
     * @return
     */
    private List<Contract> fzrYthContractList() {
         List<Contract> allYthContract = contractRepository.findByHtshztOrderBySqsjDesc(REVOCATION_CONDITION);
         List<Contract> resultList = new ArrayList<Contract>();
         final String zcglbJd = "4"; //资产管理部审核对应的节点
        for (Contract contract : allYthContract) {
            Set<Lcrzb> Lcrz = contract.getLcrzSet();
            for (Lcrzb lcrzb : Lcrz) {
                if(StringUtil.equals(lcrzb.getDyjd(), zcglbJd)){
                    resultList.add(contract);
                    break;//跳出内层循环
                }
            }
        }
        return resultList;
    }

	/**
	 * 对数据进行过滤，并设置合同的状态
	 * 1.过滤掉已经被标记为删除的合同
	 * 2.如果是部门内部审批时过滤掉其他部门的合同
	 * 3.如果是公司内部审批时过滤掉其他公司的合同
	 * @param userId
	 * @param condition
	 * @param contractList
	 * @return
	 */
	private List<ContractVo> filter(String userId, String condition, List<Contract> contractList) {
		List<ContractVo> contractVoList = new ArrayList<>();
		if(contractList == null) return contractVoList;
		Map<String, String> lcjdbMap = getAllLcjdMapping();
		Lcjdb lcjd = lcjdbRepository.findOne(condition);
		User user = userRepository.findOne(userId);
		for (Contract contract : contractList) {
			if(StringUtil.equals(contract.getSfsc(), "Y")) continue;//过滤掉已删除的合同
			//需要部门内部审核的合同，只有申请人的部门内部人员有权限审核
			if(StringUtil.equals(lcjd.getShbm(), "Y")){
					if(!sfsybm(contract.getNhtr().getDepartments().iterator().next(), 
							user.getDepartments())) continue;
			}
			
			//需要公司内部审核的合同，只有同一个公司的人员才有权限审核
			if(StringUtil.equals(lcjd.getShgs(), "Y")){
			    String companyIdOfContract = contract.getNhtr().getSsgs().getId();
			    if(!StringUtil.equals(companyIdOfContract, user.getSsgs().getId())) continue;
			}
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
	 * 对于一些退回的合同，可能会存在在待审批，已审批中出现同一条记录的情况，现在通过condition
	 * （合同审核状态=condition的说明是当前用户待审批的合同，将这种合同从已审批的合同列表中去除）
	 *  去除这种情况
	 * @param userId
	 * @return
	 */
	private List<ContractVo> getHtspYspList(String userId, String condition) {
		List<ContractVo> contractVoList = new ArrayList<>();
		List<Contract> contractList = contractRepository.findYspContract(userId, condition);
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
		if(!StringUtil.isBlank(contractVo.getHtshzt())) {
			if (lcjdbMap.get(contractVo.getHtshzt()).equals("审核完成")) {
				String id = contractVo.getId();
				LcrzbVo lcrzbVo= lcrzbService.getLastLcrzByClnrid(id);
				if(lcrzbVo !=null){
					if(lcrzbVo.getCljg().equals("通过")){
						contractVo.setHtshzt("通过");
					}
					else if(lcrzbVo.getCljg().equals("不通过")){
						contractVo.setHtshzt("终止");
					}
				}
			}
			else {
				contractVo.setHtshzt(lcjdbMap.get(contractVo.getHtshzt()));
			}
		}
		else contractVo.setHtshzt("");
	}

	/**
	 * 申请记录界面已审批合同
	 * @param userId
	 * @return
	 */
	@Cacheable(cacheName = "CONTRACT_CACHE")
	public List<ContractVo> getSqjlYspList(String userId){
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		List<Contract> yspList = contractRepository.findByNhtr_idAndHtshztOrderBySqsjDesc(userId, "Y");
		List<Contract> yhtList = contractRepository.findByNhtr_idAndHtshztOrderBySqsjDesc(userId, "T");
		if(yspList != null){
			for (Contract contract1 : yspList) {
				String id = contract1.getId();
				ContractVo contractVo = new ContractVo(contract1);
				List<LcrzbVo> lcrzbVoList = LcrzbService.convert(contract1.getLcrzSet());
				String cljg = lcrzbVoList.get(lcrzbVoList.size()-1).getCljg();
				if(cljg.equals("不通过")){
					contractVo.setHtshzt("终止");
				}
				else{
					contractVo.setHtshzt("通过");
				}
//				Map<String, String> lcjdbMap = getAllLcjdMapping();
//				String lcjdName = lcjdbMap.get(lcrzbVoList.get(lcrzbVoList.size()-1).getLcjd())
//				for (LcrzbVo lcrzbVo : lcrzbVoList) {
//					if(!StringUtil.isBlank(lcrzbVo.getLcjd())){
//						String lcjdName = lcjdbMap.get(lcrzbVo.getLcjd());
//						lcrzbVo.setLcjd(lcjdName);
//					}
//				}

//
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
	 * 1.没有完成 状态不是Y
	 * 2.没有回填 状态不是T
	 * 3.没有撤销 状态不是revocation
	 * @param userId
	 * @return
	 */
	@Cacheable(cacheName = "CONTRACT_CACHE")
	public List<ContractVo> getSqjlWspList(String userId, String condition) {
		List<ContractVo> contractVoList = new ArrayList<>();
		List<Contract> wspList = contractRepository.findSqjlWspList(userId, condition);//过滤掉合同状态是Y，T的合同
		if(wspList != null && wspList.size() > 0)
			for (Contract contract : wspList) {
			    if(StringUtil.equals(REVOCATION_CONDITION, contract.getHtshzt())) continue;//过滤掉已撤销的合同
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
		List<Contract> result = new ArrayList<>();
		if(ythContractList == null || ythContractList.size() == 0) return contractList;
		Map<String, Contract> ythContractMap = new HashMap<>();
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
	 * 没有退回合同的说法了，现在合同只能往下审批，申请人有撤销合同的权限。其他人不能退合同
	 * @param cljg
	 * @param message
	 * @param clnrid
	 * @param clnrfl
	 */
	@Transactional
	@TriggersRemove(cacheName = { "CONTRACT_CACHE" }, removeAll = true)
	public void saveContractLcrz(String cljg, String message, String clnrid, String clnrfl) {
		UserVo userVo = userService.getLoginUser();
		String roleName = userVo.getRoleName();
		ContractVo contractVo = getContractById(clnrid);
		String nextCondition;//合同的下一个状态，根据是否被退回确定
		String curCondition = contractVo.getHtshzt();
//		String sfth = "N";//合同是否被退回
		//添加流程日志

		//处理意见是“不通过”，并且不是法务审核就是退回
//		if(StringUtil.equals(cljg, "不通过") && !StringUtil.equals(curCondition, "3")){
//			nextCondition = lcService.getThCondition(contractVo.getLcbs(), curCondition);
//			lcrzbService.setLcrzSfxs(clnrid, contractVo.getLcbs(), nextCondition);
//		}
//		else{
	    //如果是负责人审核通过就生成电子序号
	    if (StringUtil.equals(cljg, "通过") && StringUtil.equals(curCondition, "5")) {
            contractVo.setSequenceNumber(generateContractSequenceNumber());
        }
        String lcrzId="";
		//修改合同状态
		if(StringUtil.equals(cljg, "不通过")){
			nextCondition = "Y";
		    lcrzId = lcrzbService.save(new LcrzbVo(cljg, message), clnrid, clnrfl, curCondition);

		}
		else{
			nextCondition = lcService.getNextCondition(contractVo.getLcbs(), curCondition);
			lcrzId = lcrzbService.save(new LcrzbVo(cljg, message), clnrid, clnrfl, curCondition);
		}

		bindContractWithLcrz(clnrid, lcrzId);
//		}
		
		if(!StringUtil.isBlank(nextCondition)){
			contractVo.setHtshzt(nextCondition);
//			contractVo.setSfth(sfth);
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
	
	/**
	 * 判断当前要使用的合同编号是不是唯一的
	 * @param htbh
	 * @return true 合同编号是唯一的<br>
	 * @return false 合同编号已经存在（不唯一）
	 */
	public boolean isHtbhUnique(String htbh) {
		if(StringUtil.isBlank(htbh)) throw new BaseAppException("要验证的合同编号为空！");
		if(contractRepository.findByBh(htbh) == null) return true;
		return false;
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
		List<RoleJdgxbGxbVo> roleJdgxbGxbVo= roleJdgxbGxbService.getWjzt("sq", "ht");
		//取第一个合同申请角色
		Role role = roleRepository.findOne(roleJdgxbGxbVo.get(0).getRoleId());
		if(StringUtil.equals(role.getName(), "集团文员")) return "1";//集团合同
		if(StringUtil.equals(role.getName(), "东交公司文员")) return "3";//东交合同
		if (StringUtil.equals(role.getName(),"其他公司发起人")) return "2";//非集团合同

		RoleJdgxbGxb roleJdgxbGxb  = roleJdgxbGxbRepository.findByRoleId(role.getId());
		if (roleJdgxbGxb !=null && roleJdgxbGxb.getJdId() !=null ){
			Lcjdb lcjdb = lcjdbRepository.getOne(roleJdgxbGxb.getJdId());
			if (lcjdb !=null){
				return lcjdb.getSslc();
			}
		}

		return "-1";

	}

	/**
	 * 删除合同（软删除）
	 * @param id
	 */
	@Transactional
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
	@Transactional
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
		Map<String, String> resultMap = new HashMap<String, String>();
//		List<Lcjdb> lcjdbList = lcjdbRepository.findBySslc("1");
//		List<Lcjdb> lcjdbList2 = lcjdbRepository.findBySslc("2");
//		List<Lcjdb> lcjdbList3 = lcjdbRepository.findBySslc("3");
//
//		for (Lcjdb lcjdb : lcjdbList) {
//			resultMap.put(lcjdb.getId(), lcjdb.getName());
//		}
//		for (Lcjdb lcjdb : lcjdbList2) {
//			resultMap.put(lcjdb.getId(), lcjdb.getName());
//		}
//		for (Lcjdb lcjdb : lcjdbList3) {
//			resultMap.put(lcjdb.getId(), lcjdb.getName());
//		}
		List<Lcjdb> lcjdbs = lcjdbRepository.findAll();
		for (Lcjdb lcjdb:lcjdbs){
			resultMap.put(lcjdb.getId(),lcjdb.getName());
		}
		resultMap.put("T", "已回填");
		resultMap.put("Y", "审核完成");
		resultMap.put("b1","集团资产管理部审批");
		resultMap.put("b2","法务审批");
		resultMap.put(REVOCATION_CONDITION, "已撤销");
		return resultMap;
	}
	
	/**
	 * @param htbt 合同标题
	 * @param htbh 合同编号
	 * @param startTime 开始时间（搜索的时间段，以申请时间为准）
	 * @param endTime 结束时间
	 * @param htzt 合同状态（0 ：全部， 1：待审批， 2 ：已审批， 3 ： 已回填）
	 * @param fqr 发起人（合同的申请人）姓名
	 * @param xgryId 相关的人员的ID（用于查询个人相关的合同）
	 * @return
	 */
	@Cacheable(cacheName = "CONTRACT_CACHE")
	public List<ContractVo> searchContract(final String htbt, final String htbh, final Date startTime,
			final Date endTime, final String htzt, final String fqr, final String xgryId) {
		List<ContractVo> contractVoList = new ArrayList<>();
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
				
//				if(!StringUtil.isBlank(fqr)) list.add(cb.equal(leftJoin.get("id").as(String.class), fqr));
				//合同申请人（发起人）
				if(!StringUtil.isBlank(fqr)){
					Join<Contract, User> contractUserLeftJoin = root.join(root.getModel().getSingularAttribute("nhtr", User.class), JoinType.LEFT);
					list.add(cb.equal(contractUserLeftJoin.get("fullName").as(String.class), fqr));//发起人
				}
				
				//相关的人员的ID（用于查询个人相关的合同）
				if(!StringUtil.isBlank(xgryId)){
					Join<Contract, Lcrzb> contractLcrzbLeftJoin = root.joinSet("lcrzSet", JoinType.LEFT);
					list.add(cb.equal(contractLcrzbLeftJoin.get("user").get("id").as(String.class), xgryId));
				}
				
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

    /**
     * 撤销合同
     * 合同申请人有权利撤销合同 撤销后的合同只能查看基本属性，不能查看和下载已上传的附件
     * 1.在数据库中查找相应id的合同（未找到抛出异常，指示合同没有找到）
     * 2.在流程日志表中插入一条合同撤销的记录
     * 3.修改合同的状态为已撤销，并保存
     * @param contractId 要撤销的合同的id
     * @param reason （1.提交人发现有误  2.资产部建议修改  3.其他（理由自填））
     */
	@Transactional
	@TriggersRemove(cacheName = { "CONTRACT_CACHE" }, removeAll = true)
    public void revocationContract(String contractId, String reason) {
        if(StringUtil.isBlank(contractId)) throw new BaseAppException("要撤销的合同id为空");
        Contract contract = contractRepository.getOne(contractId);
        if(contract == null) throw new BaseAppException("没有找到要撤销的合同！");
        
        //插入撤销合同的记录
        String lcrzId = lcrzbService.save(new LcrzbVo("撤销", reason), contractId, "", REVOCATION_CONDITION);
        bindContractWithLcrz(contractId, lcrzId);
        //记录与合同绑定
        contract.setHtshzt(REVOCATION_CONDITION);
        //保存合同
        contractRepository.save(contract);
    }
	
	/**
	 * 同步方法的访问保证生成的序号唯一
	 * 生成合同的电子序号：序号格式是 日期+四位的序号 如201701020001
	 * 1.取得当前日期并用适当的格式转化为字符串(dateSequence)
	 * 2.从数据库中取出最大的序号（电子序号后四位）并加一(numSequence)
	 * 3.二者合并生成当前审批合同的电子序号，保存到数据库中
	 * @return 可以使用的电子序号
	 */
	private synchronized String generateContractSequenceNumber(){
	    Date today = DateUtil.today();
	    String dateSequence = DateUtil.format(today, DateUtil.shortFormat);
	    String matchString = dateSequence.substring(0, 4) + "%";
	    int countOfYear = contractRepository.countBySequenceNumberLike(matchString);
	    String numSequence = String.format("%04d", countOfYear + 1);//四位的序号，不足四位的在前面补零
	    return dateSequence + numSequence;
	}
	
	/**
	 * 判断用户是不是可以下载某个合同附件的word版本
	 * @param contractId
	 * @return
	 */
	public boolean attachmentDownloadAuthorityJudgement(String contractId, String loginUserId) {
    	final Contract contract = contractRepository.findById(contractId);
    	if(contract == null) return false;
    	boolean isAuditFinaish = (StringUtil.equals(contract.getHtshzt(), "Y") 
    	        || StringUtil.equals(contract.getHtshzt(), "T"));//合同审核是否完成
    	User user = userRepository.getOne(loginUserId);
    	Set<Role> roleSet = user.getRoles();
    	boolean isZichanguanlibu = false;
    	for (Role role : roleSet) {
            if(StringUtil.equals(role.getName(), "资产管理部合同审核员")) {
                isZichanguanlibu = true;
                break;
            }
        }
	    return (isAuditFinaish && isZichanguanlibu);
	}
	public List<ContractVo> deleteRepeatedContract(List<ContractVo> contractVos){
		if (contractVos.size() == 0 ) return new ArrayList<>();
		List<ContractVo> contractVoList = new ArrayList<>();
		for (ContractVo contractVo:contractVos){
			if (!contractVoList.contains(contractVo)){
				contractVoList.add(contractVo);
			}
		}
		return contractVoList;
	}

}
