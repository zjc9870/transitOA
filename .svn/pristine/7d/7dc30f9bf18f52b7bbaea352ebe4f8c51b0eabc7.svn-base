package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.expect.admin.service.vo.WxUserVo;

/**
 * 关联微信号和用户信息
 */
@Entity
@Table(name = "wxuser")
public class WxUser {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "wx_id",nullable = false,length = 128)
	private String wxId;
	
	@Column(name = "device_id",nullable = false,length = 128)
	private String deviceId;

	public WxUser(){
		
	}
	
	public WxUser(WxUserVo wxUserVo){
		this.id = wxUserVo.getId();
		this.userId = wxUserVo.getUserId();
		this.wxId = wxUserVo.getWxId();
		this.deviceId = wxUserVo.getDeviceId();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	
}
