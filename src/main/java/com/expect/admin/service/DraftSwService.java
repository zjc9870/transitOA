package com.expect.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DraftSwRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.DraftSw;
import com.expect.admin.data.dataobject.User;
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
	
	public DraftSwVo getDraftVoById(String id) {
		DraftSwVo draftSwVo = new DraftSwVo();
		return draftSwVo;
	}
}
