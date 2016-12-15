package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expect.admin.data.dao.ContractRepository;
import com.expect.admin.data.dao.LcjdgxbRepository;
import com.expect.admin.data.dataobject.Contract;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.ContractVo;
import com.expect.admin.service.vo.LcrzbVo;
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
	
	@Transactional
	public void save(ContractVo contractVo){
		Contract contract = contractRepository.save(new Contract(contractVo));
		LcrzbVo lcrzbVo = new LcrzbVo("新增", "");
		lcrzbService.save(lcrzbVo, contract.getId(), contract.getHtfl(), contractVo.getHtshzt());
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
		ContractVo controctVo = new ContractVo(contract);
		controctVo.setLcrzList(lcrzbService.getKxsLcrzbVoList(contractId));
		return controctVo;
	}
	
	public List<ContractVo> getContractByUserIdAndCondition(final String userId, final String condition, 
			final Date start, final Date end, String lx) {
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		List<Contract> contractList = null;
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
	

}
