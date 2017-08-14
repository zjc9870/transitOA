package com.expect.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.WxUserRepository;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.data.dataobject.WxUser;
import com.expect.admin.service.vo.WxUserVo;

@Service
public class WxUserService {
	@Autowired
	private WxUserRepository wxUserRepository;
	
	public boolean isUserExisit(String wxid){
		if(wxUserRepository.findByWxId(wxid).size()>0){
			return true;
		}
		return false;
	}
	
	public WxUser findUserByWxidAndDeviceId(String wxid,String deviceid){
		return wxUserRepository.findByWxIdAndDeviceId(wxid,deviceid).get(0);
	}
	
	public WxUser findUserByWxid(String wxid){
		return wxUserRepository.findByWxId(wxid).get(0);
	}
	
}
