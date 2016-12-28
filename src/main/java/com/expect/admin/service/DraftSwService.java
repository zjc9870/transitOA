package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DraftSwRepository;
import com.expect.admin.data.dao.RoleRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.DraftSw;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.DraftSwVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.StringUtil;

@Service
public class DraftSwService {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DraftSwRepository draftSwRepository;
	@Autowired
	private RoleRepository roleRepository;

	public void save(DraftSwVo draftSwVo) {
		if(draftSwVo == null) return;
		DraftSw draftSw = new DraftSw(draftSwVo);
		UserVo userVo = userService.getLoginUser();
		User user = userRepository.findOne(userVo.getId());
		draftSw.setSwr(user);
		draftSwRepository.save(draftSw);
	}
	
	public void update(DraftSwVo draftSwVo){
		if(draftSwVo == null || StringUtil.isBlank(draftSwVo.getId())) return;
		draftSwRepository.save(new DraftSw(draftSwVo));
	}
	
	public void delete(String id) {
		//1.收文的当前流程标识，判断当前状态是不是开始状态
		//2.当前用户是不是收文的收文人
		//3.删除收文，删除相关用户，删除流程日志
	}
	
	public DraftSwVo getDraftSwVoById(String id) {
//		DraftSwVo draftSwVo = new DraftSwVo();
		DraftSw draftSw = draftSwRepository.findOne(id);
		if(draftSw == null) throw new BaseAppException("id为 "+id+"的收文没有找到");
		DraftSwVo draftSwVo = new DraftSwVo(draftSw);
		return draftSwVo;
	}
	
	public List<DraftSwVo> getDraftSwVoByUserAndCondition(final String userId,final String condition) {
		List<DraftSwVo> draftSwVoList = new ArrayList<DraftSwVo>();
		List<DraftSw> draftSwList = new ArrayList<DraftSw>();
		//待批示
		//已批示
		//待传阅
		//待办理
		//待选择传阅人
		//待选择办理人
		if(draftSwList==null){
			return draftSwVoList;
		}
		for(DraftSw sw:draftSwList){
			draftSwVoList.add(new DraftSwVo(sw));
		}
		return draftSwVoList;
	}
	
	public void addPyr(List<String> userIdList, final DraftSwVo swVo){
		Role role = roleRepository.getOne("roleid");//改成批阅人的roleid
		List<User> userDoList = new ArrayList<User>();

		if(role!=null){
			for(String userId:userIdList){
				User user = userRepository.getOne(userId);
				if(user!=null){
					userDoList.add(user);
					role.getUsers().add(user);
					user.getRoles().add(role);
					userRepository.save(user);
				}
			}
		}
		roleRepository.save(role);
		DraftSw sw = new DraftSw(swVo);
		sw.setPyrs(userDoList);
		draftSwRepository.save(sw);
		return;
	}
	
	public void addBlr(String userId,DraftSwVo draftSwVo){
		Role role = roleRepository.findOne("roleid");//改成办理人的id
		User user = userRepository.findOne("userId");
		if(role!=null){
			role.getUsers().add(user);
		}
		if(user!=null){
			user.getRoles().add(role);
		}
		DraftSw draftSw = new DraftSw(draftSwVo);
		draftSw.setBlr(userId);
		draftSwRepository.save(draftSw);
	}

	public boolean py(String draftSwId, String pyrId) {
		User pyr = userRepository.getOne(pyrId);
		DraftSw draftSw = draftSwRepository.getOne(draftSwId);
		if(draftSw!=null){
			draftSw.getPyrs().remove(pyr);
			draftSwRepository.save(draftSw);
			return draftSw.getPyrs().size() == 0;
		}else{
			return false;
		}
	}
}
