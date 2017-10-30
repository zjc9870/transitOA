package com.expect.admin.service.vo;

import com.expect.admin.utils.JacksonJsonUtil;

import java.util.List;

public class UserVo {

	private String id;
	private String username;
	private String password;
	private String fullName;
	private String sex;
	private String phone;
	private String email;
	private String roleName;//user.getRole().getName()的合集用逗号连接
	private String departmentName;//department.getName()的合集，用逗号连接
	private String avatarId;
	private String ssgsName;//所属公司
	private String ssgsId;//所属公司id
	private String phoneNumber1;
	private String phoneNumber2;
	private List<AttachmentVo> attachmentVos;//签名图片附件

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(String avatarId) {
		this.avatarId = avatarId;
	}

	public String getSsgsName() {
		return ssgsName;
	}

	public void setSsgsName(String ssgsName) {
		this.ssgsName = ssgsName;
	}

	public String getPhoneNumber1() {
		return phoneNumber1;
	}

	public void setPhoneNumber1(String phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}

	public String getPhoneNumber2() {
		return phoneNumber2;
	}

	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}

	public String getSsgsId() {
		return ssgsId;
	}

	public void setSsgsId(String ssgsId) {
		this.ssgsId = ssgsId;
	}

	@Override
	public String toString() {
		return JacksonJsonUtil.getInstance().write(this);
	}

	public List<AttachmentVo> getAttachmentVos() {
		return attachmentVos;
	}

	public void setAttachmentVos(List<AttachmentVo> attachmentVos) {
		this.attachmentVos = attachmentVos;
	}
}
