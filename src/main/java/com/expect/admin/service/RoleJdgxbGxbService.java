package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.RoleJdgxbGxbRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.RoleJdgxbGxb;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.RoleJdgxbGxbVo;
import com.expect.admin.service.vo.UserVo;

@Service
public class RoleJdgxbGxbService {
	
	@Autowired
	private RoleJdgxbGxbRepository roleJdgxbGxbRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
	public RoleJdgxbGxbVo getByFunctionId(String roleId){
		return new RoleJdgxbGxbVo(
				roleJdgxbGxbRepository.findByRoleId(roleId));
	}
	
	/**
	 * 获取用户可以审核的文件的状态
	 * @param bz
	 * @param wjzl
	 * @return
	 */
	public List<RoleJdgxbGxbVo> getWjzt(String bz, String wjzl){
		UserVo userVo = userService.getLoginUser();
		User user = userRepository.findOne(userVo.getId());
//		User user = userRepository.findOne("2c913b71590fcb3201590fd15ada0007");
		if(user.getRoles() == null || user.getRoles().size() == 0) return null;
		List<String> roleIds = new ArrayList<>(user.getRoles().size());
		for (Role role : user.getRoles()) {
			roleIds.add(role.getId());
            System.out.println(role.getName());
        }
		List<RoleJdgxbGxb> roleJdgxbGxbList = roleJdgxbGxbRepository.findByBzAndWjzlAndRoleIdIn(bz, wjzl, roleIds);//有错
        if(roleJdgxbGxbList == null) return null;
		List<RoleJdgxbGxbVo> list = new ArrayList<>();
		for (RoleJdgxbGxb roleJdgxbGxb:roleJdgxbGxbList){
			RoleJdgxbGxbVo roleJdgxbGxbVo = new RoleJdgxbGxbVo(roleJdgxbGxb);
			list.add(roleJdgxbGxbVo);
		}
		return list;
	}

}
