package com.expect.admin.service.vo;

import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.utils.DateUtil;


public class LcrzbVo {
	private  String id;
	private String userName;
	private String userDepartment;
	private String clsj;//处理时间
	private String message;//处理意见
	private String cljg;//处理结果
	private String lcjd;//流程节点
	private User user;
//	private String clnrid;//处理内容的id（所处理的东西的id）
	private String clnrfl;
	
	public LcrzbVo(){
		
	}


	public LcrzbVo(String cljg){
		this.cljg = cljg;
	}
	
	public LcrzbVo(String cljg, String message){
		this.message = message;
		this.cljg = cljg;
	}
	
	public LcrzbVo(Lcrzb lcrzb) {
		this.id = lcrzb.getId();
	    if(lcrzb.getUser() != null){
	        this.userName = lcrzb.getUser().getUsername();
			this.user = lcrzb.getUser();
	        if(lcrzb.getUser().getDepartments() != null && lcrzb.getUser().getDepartments().size() > 0)
	            this.userDepartment = lcrzb.getUser().getDepartments().iterator().next().getName();
	    }
		this.cljg = lcrzb.getCljg();
		this.clsj = lcrzb.getClsj() == null? "" : 
			DateUtil.format(lcrzb.getClsj(), DateUtil.fullFormat);
		this.message = lcrzb.getMessage();//处理意见
		this.lcjd = lcrzb.getDyjd();
		this.clnrfl = lcrzb.getClnrfl();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserDepartmnet() {
		return userDepartment;
	}
	public void setUserDepartmnet(String userDepartmnet) {
		this.userDepartment = userDepartmnet;
	}
	public String getClsj() {
		return clsj;
	}
	public void setClsj(String clsj) {
		this.clsj = clsj;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCljg() {
		return cljg;
	}
	public void setCljg(String cljg) {
		this.cljg = cljg;
	}

	public String getLcjd() {
		return lcjd;
	}

	public void setLcjd(String lcjd) {
		this.lcjd = lcjd;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getClnrfl() {
		return clnrfl;
	}

	public void setClnrfl(String clnrfl) {
		this.clnrfl = clnrfl;
	}
}
