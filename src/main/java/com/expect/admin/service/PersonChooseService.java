package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DepartmentRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.component.html.JsTreeVo;

@Service
public class PersonChooseService {
	
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private UserRepository userRepository;
	
	public List<JsTreeVo> getUserTree() {
	    List<User> allUserList = userRepository.findAll();
	    List<Department> allDepartmentList = departmentRepository.findAll();
	    JsTreeVo firJsTree = new JsTreeVo();
	    List<JsTreeVo> resultJsTreeVos = new ArrayList<>();
	    List<JsTreeVo> departmentJsTreeVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allUserList)) {
        	for(Department department: allDepartmentList){
        	    List<JsTreeVo> userJsTreeVos = new ArrayList<>();
        		JsTreeVo departJsTree = new JsTreeVo();
        		setDepartmentTree(department,departJsTree);
        		for (User user : allUserList) {
                    JsTreeVo userJsTree = new JsTreeVo();
                    setUserTree(user, userJsTree);
                    for(Department userDepartment:user.getDepartments()){
                        System.out.println("user's department's id:"+userDepartment.getId()+", departmentid:"+department.getId());
                    	if(userDepartment.getId().equals(department.getId())){
                            userJsTreeVos.add(userJsTree);
                            break;
                    	}
                    }
                }
        		departJsTree.setChildren(userJsTreeVos);
        		departmentJsTreeVos.add(departJsTree);
        	}
        }
        firJsTree.setChildren(departmentJsTreeVos);
        resultJsTreeVos.add(firJsTree);
        
        //一下是增强的从数据库中取数据的代码2c9151b6592fd4c901592fde79690002换成相应最高部门节点的ID
		//取出父部门是东交总公司的
//		List<Department> departmentList = departmentRepository.findByParentDepartmentId("2c9151b6592fd4c901592fde79690002");
//		if(departmentList == null) return new ArrayList<>();
//		List<JsTreeVo> resultJsTreeVos = new ArrayList<>();
//		for (Department department : departmentList) {
//			JsTreeVo firJsTree = new JsTreeVo();
//			setDepartmentTree(department, firJsTree);
//			Set<User> secUser = department.getUsers();
//			List<Department> secDep = department.getChildDepartments();
//			if(!CollectionUtils.isEmpty(secUser) || !CollectionUtils.isEmpty(secDep)){
//				List<JsTreeVo> secJsTreeVos = new ArrayList<>();
//				if(!CollectionUtils.isEmpty(secUser)){ 
//					for (User user : secUser) {
//						JsTreeVo secUserJsTree = new JsTreeVo();
//						setUserTree(user, secUserJsTree);
//						secJsTreeVos.add(secUserJsTree);
//					}
//				}
//				if(!CollectionUtils.isEmpty(secDep)){
//					for (Department department2 : secDep) {
//						JsTreeVo secDepJsTree = new JsTreeVo();
//						setDepartmentTree(department2, secDepJsTree);
//						
//						Set<User> thiUser = department2.getUsers();
//						if(!CollectionUtils.isEmpty(thiUser)){
//							List<JsTreeVo> thiTreeVos = new ArrayList<>();
//							for (User user3 : thiUser) {
//								JsTreeVo thiUserJsTree = new JsTreeVo();
//								setUserTree(user3, thiUserJsTree);
//								thiTreeVos.add(thiUserJsTree);
//							}
//							secDepJsTree.setChildren(thiTreeVos);
//						}
//						secJsTreeVos.add(secDepJsTree);
//					}
//					firJsTree.setChildren(secJsTreeVos);
//				}
//			}
//			resultJsTreeVos.add(firJsTree);
//		}
		
		return resultJsTreeVos;
	}
	
	private void setDepartmentTree(Department department, JsTreeVo jsTreeVo){
		jsTreeVo.setId(department.getId());
		jsTreeVo.setText(department.getName());
	}
	
	private void setUserTree(User user, JsTreeVo jsTreeVo) {
		jsTreeVo.setId(user.getId());
		jsTreeVo.setText(user.getFullName());
	}
	

}
