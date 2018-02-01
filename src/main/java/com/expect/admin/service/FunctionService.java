package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.expect.admin.data.dao.ContractRepository;
import com.expect.admin.data.dao.HytzRepository;
import com.expect.admin.data.dao.MeetingRepository;
import com.expect.admin.service.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.FunctionRepository;
import com.expect.admin.data.dataobject.Function;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.convertor.FunctionConvertor;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;

@Service
public class FunctionService {

	@Autowired
	private FunctionRepository functionRepository;
	@Autowired
	private MeetingRepository meetingRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private HytzRepository hytzRepository;
	@Autowired
	private RoleJdgxbGxbService roleJdgxbGxbService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private FwtzService fwtzService;
	@Autowired
	private DraftSwService draftSwService;

	/**
	 * 根据用户封装导航菜单
	 */
	public List<FunctionVo> getFunctionsByUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<Role> roles = user.getRoles();
		Set<Function> functions = new HashSet<>();
		for (Role role : roles) {
			Set<Function> roleFunctions = role.getFunctions();
			if (!CollectionUtils.isEmpty(roleFunctions)) {
				for (Function function : roleFunctions) {
					functions.add(function);
				}
			}
		}

		// 把相应功能归类到同一顶级功能下
		List<FunctionVo> resultFunctions = new ArrayList<>();
		for (Function function : functions) {
			Function parentFunction = function.getParentFunction();
			if (parentFunction != null) {
				Function parentParentFunction = parentFunction.getParentFunction();
				if (parentParentFunction != null) {
					boolean flag = false;
					for (int i = 0; i < resultFunctions.size(); i++) {
						FunctionVo resultFunction = resultFunctions.get(i);
						if ((parentParentFunction.getId()).equals(resultFunction.getId())) {
							List<FunctionVo> resultChildFunctions = resultFunction.getChildFunctionVos();
							boolean flag1 = false;
							for (int j = 0; j < resultChildFunctions.size(); j++) {
								FunctionVo resultChildFunction = resultChildFunctions.get(j);
								if (resultChildFunction.getId().equals(parentFunction.getId())) {
									flag1 = true;
									FunctionVo functionVo = FunctionConvertor.convert(function, true);
									resultChildFunction.addChildFunction(functionVo);
									break;
								}
							}
							if (!flag1) {
								FunctionVo functionVo = FunctionConvertor.convert(function, true);
								FunctionVo parentFunctionVo = FunctionConvertor.convert(parentFunction, true);
								parentFunctionVo.addChildFunction(functionVo);
								for (FunctionVo functionTmpVo : resultFunctions) {
									if (functionTmpVo.getName().equals(parentParentFunction.getName())) {
										functionTmpVo.addChildFunction(parentFunctionVo);
									}
								}
							}
							flag = true;
							break;
						}
					}
					if (!flag) {
						FunctionVo functionVo = FunctionConvertor.convert(function, true);
						FunctionVo parentFunctionVo = FunctionConvertor.convert(parentFunction, true);
						FunctionVo parentParentFunctionVo = FunctionConvertor.convert(parentParentFunction, true);
						parentFunctionVo.addChildFunction(functionVo);
						parentParentFunctionVo.addChildFunction(parentFunctionVo);
						resultFunctions.add(parentParentFunctionVo);
					}
				} else {
					boolean flag = false;
					for (int i = 0; i < resultFunctions.size(); i++) {
						FunctionVo resultFunction = resultFunctions.get(i);
						if ((parentFunction.getId()).equals(resultFunction.getId())) {
							FunctionVo functionVo = FunctionConvertor.convert(function, true);
							resultFunction.addChildFunction(functionVo);
							flag = true;
							break;
						}
					}
					if (!flag) {
						FunctionVo functionVo = FunctionConvertor.convert(function, true);
						FunctionVo parentFunctionVo = FunctionConvertor.convert(parentFunction, true);
						parentFunctionVo.addChildFunction(functionVo);
						resultFunctions.add(parentFunctionVo);
					}
				}
			} else {
				FunctionVo functionVo = FunctionConvertor.convert(function, true);
				resultFunctions.add(functionVo);
			}
		}
		// 排序
		Collections.sort(resultFunctions);
		for (int i = 0; i < resultFunctions.size(); i++) {
			FunctionVo parentParentFunction = resultFunctions.get(i);
			List<FunctionVo> childFunctions = parentParentFunction.getChildFunctionVos();
			Collections.sort(childFunctions);
			if (childFunctions != null) {
				for (int j = 0; j < childFunctions.size(); j++) {
					FunctionVo parentFunction = childFunctions.get(j);
					List<FunctionVo> childChildFunctions = parentFunction.getChildFunctionVos();
					if (childChildFunctions != null) {
						Collections.sort(childChildFunctions);
					}
				}
			}
		}

		//增加待处理条目
		UserVo userVo = userService.getUserById(user.getId());
		String department = userVo.getDepartmentName();
		String userName = userVo.getUsername();
		for(int i = 0; i < resultFunctions.size(); i++){
			FunctionVo parentFunction = resultFunctions.get(i);
			if(parentFunction.getName().equals("会议管理")){
				int countParent = 0;
				List<FunctionVo> childFunctions = parentFunction.getChildFunctionVos();
				if(childFunctions != null){
					for(int j = 0; j < childFunctions.size(); j++){

						FunctionVo childFunction = childFunctions.get(j);
						String functionName = childFunction.getName();
						if(functionName.equals("会议审批")){
							if (department.indexOf("集团") != -1){
								int countChild = meetingRepository.findCountMeeting("2", "1");
								countParent += countChild;
								if(countChild != 0){
									childFunction.setCount("" + countChild);
								}
							}else if (department.indexOf("东山公交") != -1) {
								int countChild = meetingRepository.findCountMeeting("2", "2");
								countParent += countChild;
								if(countChild != 0){
									childFunction.setCount("" + countChild);
								}
							}
						}else if(functionName.equals("会议通知")){
							int countChild = hytzRepository.findCountHytz(userName, "0");
							countParent += countChild;
							if(countChild != 0){
								childFunction.setCount("" + countChild);
							}
						}
					}
				}
				if(countParent != 0){
					parentFunction.setCount("" + countParent);
				}
			}else if (parentFunction.getName().equals("合同审办")){
				int countParent = 0;
				List<FunctionVo> childFunctions = parentFunction.getChildFunctionVos();
				if (childFunctions != null){
					for (int j = 0; j < childFunctions.size(); j++){
						FunctionVo childFunction = childFunctions.get(j);
						String functionName = childFunction.getName();
						if(functionName.equals("合同审批")){
							List<RoleJdgxbGxbVo> condition = roleJdgxbGxbService.getWjzt("sp", "ht");
							List<ContractVo> contractVoList = new ArrayList<>();
							for (RoleJdgxbGxbVo roleJdgxbGxbVo:condition){
								List<ContractVo> contractVos = contractService.getContractByUserIdAndCondition(userVo.getId(), roleJdgxbGxbVo.getJdId(), "dsp");
								contractVoList.addAll(contractVos);
							}
							int countChild = contractVoList.size();
							countParent += countChild;
							if (countChild != 0){
								childFunction.setCount("" + countChild);
							}
						}
					}
				}
				if(countParent != 0){
					parentFunction.setCount("" + countParent);
				}
			}else if (parentFunction.getName().equals("发文管理")){
				int countParent = 0;
				List<FunctionVo> childFunctions = parentFunction.getChildFunctionVos();
				if (childFunctions != null){
					for (int j = 0; j < childFunctions.size(); j++){
						FunctionVo childFunction = childFunctions.get(j);
						String functionName = childFunction.getName();
						if (functionName.equals("发文审批")){
							List<DocumentVo> documentVoList = documentService.getDocumentByUserIdAndCondition(userVo.getId(),"2", "dsp");
							int countChild = documentVoList.size();
							countParent += countChild;
							if (countChild != 0){
								childFunction.setCount("" + countChild);
							}
						}else if (functionName.equals("发文通知")){
							String role = userVo.getRoleName();
							List<FwtzVo> fwtzVoList=fwtzService.getFwtzByUserName(userName);
							List<FwtzVo> wdFwtzVoList=fwtzService.getWdFwtzList(fwtzVoList);
							List<DocumentVo> documentVoList1=fwtzService.getFwDocumentVo(wdFwtzVoList,role);
							List<DocumentVo> documentVoList=fwtzService.getFwDocumentVoMerge(documentVoList1);
							int countChild = documentVoList.size();
							countParent += countChild;
							if (countChild != 0){
								childFunction.setCount("" + countChild);
							}
						}
					}
				}
				if(countParent != 0){
					parentFunction.setCount("" + countParent);
				}
			}else if (parentFunction.getName().equals("收文管理")){
				int countParent = 0;
				List<FunctionVo> childFunctions = parentFunction.getChildFunctionVos();
				if (childFunctions != null){
					for (int j = 0; j < childFunctions.size(); j++){
						FunctionVo childFunction = childFunctions.get(j);
						String functionName = childFunction.getName();
						if (functionName.equals("收文批示")){
							List<DraftSwVo> draftSwVoList = draftSwService.getDraftSwVoList("swps", "dps", userVo.getId());
							int countChild = draftSwVoList.size();
							countParent += countChild;
							if (countChild != 0){
								childFunction.setCount("" + countChild);
							}
						}else if (functionName.equals("收文传阅")){
							List<DraftSwVo> draftSwVoList = draftSwService.getDraftSwVoList("swcy", "dcy", userVo.getId());
							int countChild = draftSwVoList.size();
							countParent += countChild;
							if (countChild != 0){
								childFunction.setCount("" + countChild);
							}
						}else if (functionName.equals("收文办理")){
							List<DraftSwVo> draftSwVoList = draftSwService.getDraftSwVoList("swbl", "wbl", userVo.getId());
							int countChild = draftSwVoList.size();
							countParent += countChild;
							if (countChild != 0){
								childFunction.setCount("" + countChild);
							}
						}
					}
				}
				if(countParent != 0){
					parentFunction.setCount("" + countParent);
				}
			}
		}

		return resultFunctions;
	}

	/**
	 * 将多出的没有子节点的系统管理删掉
	 * @param functionVos
     */
	public void filter(List<FunctionVo> functionVos){
		int k=-1;
		for (FunctionVo resultFunction:functionVos){
			if (resultFunction.getName().equals("系统管理") && resultFunction.getChildFunctionVos().size()==0){
				k=functionVos.indexOf(resultFunction);
			}
		}
		if (k !=-1){
			functionVos.remove(k);
		}
		return;
	}
	
	/**
	 * 获取所有的菜单
	 */
	public List<FunctionVo> getFunctions() {
		List<Function> functions = functionRepository.findAll();
		List<FunctionVo> functionVos = FunctionConvertor.convert(functions);
		return functionVos;
	}

	/**
	 * 根据id获取function
	 */
	public FunctionVo getFunctionById(String id) {
		List<FunctionVo> functions = getFunctions();
		FunctionVo checkedFunction = null;
		if (!StringUtils.isEmpty(id)) {
			for (int i = functions.size() - 1; i >= 0; i--) {
				if (id.equals((functions.get(i).getId()))) {
					checkedFunction = functions.remove(i);
					break;
				}
			}
		}
		SelectOptionVo parentFunctionSov = FunctionConvertor.convertSov(functions, checkedFunction);
		FunctionVo functionVo = null;
		if (!StringUtils.isEmpty(id)) {
			Function function = functionRepository.findOne(id);
			if(function==null){
				functionVo = new FunctionVo();
			}else{
				functionVo = FunctionConvertor.convert(function, false);
			}
		} else {
			functionVo = new FunctionVo();
		}
		functionVo.setParentFunctionSov(parentFunctionSov);
		return functionVo;
	}

	/**
	 * 保存
	 */
	@Transactional
	public DataTableRowVo save(FunctionVo functionVo) {
		DataTableRowVo dtrv = new DataTableRowVo();
		dtrv.setMessage("增加失败");
		Function parentFunction = null;
		if (!StringUtils.isEmpty(functionVo.getParentId())) {
			parentFunction = functionRepository.findOne(functionVo.getParentId());
			if (parentFunction == null) {
				return dtrv;
			}
		}
		Function function = FunctionConvertor.convert(functionVo);
		function.setParentFunction(parentFunction);

		Function result = functionRepository.save(function);
		if (result != null) {
			dtrv.setResult(true);
			dtrv.setMessage("增加成功");
			FunctionConvertor.convertDtrv(dtrv, result, parentFunction);
		}
		return dtrv;
	}

	/**
	 * 更新
	 */
	@Transactional
	public DataTableRowVo update(FunctionVo functionVo) {
		DataTableRowVo dtrv = new DataTableRowVo();
		dtrv.setMessage("修改失败");
		Function parentFunction = null;
		if (!StringUtils.isEmpty(functionVo.getParentId())) {
			parentFunction = functionRepository.findOne(functionVo.getParentId());
			if (parentFunction == null) {
				return dtrv;
			}
		}

		Function checkFunction = functionRepository.findOne(functionVo.getId());
		if (checkFunction == null) {
			return dtrv;
		}
		// 把原来的name记录下来，如果和现在的不一样，并且修改的function是父功能，那就查询到下级所有的子部门
		String name = checkFunction.getName();
		FunctionConvertor.convert(functionVo, checkFunction);
		checkFunction.setParentFunction(parentFunction);

		if (!name.equals(functionVo.getName())) {
			List<Function> childFunctions = functionRepository.findByParentFunctionId(functionVo.getId());
			if (!CollectionUtils.isEmpty(childFunctions)) {
				for (Function function : childFunctions) {
					dtrv.addAddData(function.getId());
				}
			}
		}
		dtrv.setResult(true);
		dtrv.setMessage("修改成功");
		FunctionConvertor.convertDtrv(dtrv, checkFunction, parentFunction);
		return dtrv;
	}

	/**
	 * 删除
	 */
	@Transactional
	public ResultVo delete(String id) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("删除失败");

		Function function = functionRepository.findOne(id);
		if (function == null) {
			return resultVo;
		}
		// 把所有所有的子功能id查询到，传给前台，一起删除
		List<Function> childFunctions = functionRepository.findByParentFunctionId(id);
		List<String> childIds = new ArrayList<>();
		if (!CollectionUtils.isEmpty(childFunctions)) {
			for (Function childFunction : childFunctions) {
				childIds.add(childFunction.getId());
			}
		}
		functionRepository.delete(id);
		resultVo.setResult(true);
		resultVo.setMessage("删除成功");
		resultVo.setObj(childIds);
		return resultVo;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            用,号隔开
	 */
	@Transactional
	public ResultVo deleteBatch(String ids) {
		ResultVo resultVo = new ResultVo();
		resultVo.setMessage("删除失败");
		if (StringUtils.isEmpty(ids)) {
			return resultVo;
		}
		String[] idArr = ids.split(",");
		// 把所有所有的子功能id查询到，传给前台，一起删除
		List<Function> childFunctions = functionRepository.findByParentFunctionIdIn(idArr);
		List<String> childIds = new ArrayList<>();
		if (!CollectionUtils.isEmpty(childFunctions)) {
			for (Function childFunction : childFunctions) {
				childIds.add(childFunction.getId());
			}
		}

		for (String id : idArr) {
			functionRepository.delete(id);
		}
		resultVo.setResult(true);
		resultVo.setMessage("删除成功");
		resultVo.setObj(childIds);
		return resultVo;
	}

}

