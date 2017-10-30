package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DepartmentRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.html.JsTreeVo;
import com.expect.admin.utils.StringUtil;

@Service
public class PersonChooseService {
	
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	
	
	public List<JsTreeVo> getUserTree() {
	    List<User> allUserList = userRepository.findAll();
	    List<Department> allDepartmentList = departmentRepository.findAll();
	    JsTreeVo firJsTree = new JsTreeVo();
	    List<JsTreeVo> resultJsTreeVos = new ArrayList<>();
	    List<JsTreeVo> departmentJsTreeVos = new ArrayList<>();
	    //关于集团与分公司权限的问题，可能要修改数据库结构
	    
	    //将所有的department变为节点
	    List<JsTreeVo> allDepartmentTreeVoList = new ArrayList<JsTreeVo>();
	    //第一层为用户所在的公司节点
	    UserVo currentUser = userService.getLoginUser();
	    if(currentUser.getSsgsId().equals("super")){
			for(Department department:allDepartmentList){
				JsTreeVo gsDepartJsTree = new JsTreeVo();
				if(department.getParentDepartment()==null&&!StringUtil.isBlank(department.getName())){
					setDepartmentTree(department,gsDepartJsTree);
					allDepartmentTreeVoList.add(gsDepartJsTree);
		    		departmentJsTreeVos.add(gsDepartJsTree);
				}
			}
	    }else{
	    	for(Department department:allDepartmentList){
				JsTreeVo gsDepartJsTree = new JsTreeVo();
				if(department.getId().equals(currentUser.getSsgsId())){
					setDepartmentTree(department,gsDepartJsTree);
					allDepartmentTreeVoList.add(gsDepartJsTree);
		    		departmentJsTreeVos.add(gsDepartJsTree);
				}
			}
	    }
		//对每个公司部门节点进行递归 将部门节点树结构完成
		for(JsTreeVo parentTreeVos:departmentJsTreeVos){
			addDepartmentsToParent(allDepartmentList,allDepartmentTreeVoList,parentTreeVos);
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
		
		return departmentJsTreeVos;
	}


	private void addDepartmentsToParent(List<Department> allDepartmentList, List<JsTreeVo> allDepartmentTreeVoList, JsTreeVo parentTreeVo) {
		for(Department department:allDepartmentList){
				//找到父节点 将节点加入总节点集合和父节点下
			if(department.getParentDepartment()!=null&&!StringUtil.isBlank(department.getName())){

				if((department.getParentDepartment().getId()).equals(parentTreeVo.getId())){
					JsTreeVo departJsTree = new JsTreeVo();
					setDepartmentTree(department,departJsTree);
					parentTreeVo.addChildren(departJsTree);
					allDepartmentTreeVoList.add(departJsTree);
					addDepartmentsToParent(allDepartmentList, allDepartmentTreeVoList,departJsTree);
				}
			}
		}
	}

	private void setDepartmentTree(Department department, JsTreeVo jsTreeVo){

		jsTreeVo.setId(department.getId());
		jsTreeVo.setText(department.getName());
		jsTreeVo.setIcon("../images/folder_user.png");
	    List<User> allUserList = userRepository.findAll();
	    for(User user : allUserList){
        	if(department!=null){
        		for(Department userDepartment:user.getDepartments()){
        			if(userDepartment.getId().equals(department.getId())){

                        JsTreeVo userJsTree = new JsTreeVo();	
                        setUserTree(user, userJsTree);
                        jsTreeVo.addChildren(userJsTree);
        			}
        		}
        	}
        }
	}

	private void setUserTree(User user, JsTreeVo jsTreeVo) {
		jsTreeVo.setText(user.getUsername()+"("+user.getFullName()+")");
		jsTreeVo.setId(user.getId()+","+jsTreeVo.getText());
		jsTreeVo.setIcon("../images/user_suit.png");
	}


}
