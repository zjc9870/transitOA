package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.WxUser;

public interface WxUserRepository extends JpaRepository<WxUser, String> {
	/**
	 * 根据微信号和设备号查询用户
	 * */
	List<WxUser> findByWxIdAndDeviceId(String wxId,String deviceId);

	List<WxUser> findByWxId(String wxid);
}
