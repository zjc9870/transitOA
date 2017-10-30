package com.expect.admin.service.convertor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.expect.admin.data.dataobject.Attachment;
import com.expect.admin.service.vo.AttachmentVo;
import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.expect.admin.data.dataobject.Department;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.html.SelectOptionVo;
import com.expect.admin.service.vo.component.html.datatable.DataTableButtonFactory;
import com.expect.admin.service.vo.component.html.datatable.DataTableRowVo;
import com.expect.admin.utils.StringUtil;

public class UserConvertor {

	/**
	 * do To vo
	 */
	public static UserVo convert(User user) {
		UserVo userVo = new UserVo();
		if (user == null) {
			return userVo;
		}
		BeanUtils.copyProperties(user, userVo);
		// 设置角色
		Set<Role> roles = user.getRoles();
		StringBuilder roleSb = new StringBuilder();
		if (!CollectionUtils.isEmpty(roles)) {
			int index = 0;
			for (Role role : roles) {
				if (index != roles.size() - 1) {
					roleSb.append(role.getName() + ",");
				} else {
					roleSb.append(role.getName());
				}
				index++;
			}
		}
		userVo.setRoleName(roleSb.toString());
		// 设置部门
		Set<Department> departments = user.getDepartments();
		StringBuilder departmentSb = new StringBuilder();
		if (!CollectionUtils.isEmpty(departments)) {
			int index = 0;
			for (Department department : departments) {
				if (index != departments.size() - 1) {
					departmentSb.append(department.getName() + ",");
				} else {
					departmentSb.append(department.getName());
				}
				index++;
			}
		}
		userVo.setDepartmentName(departmentSb.toString());
		
		// 设置头像
		if (user.getAvatar() != null) {
			userVo.setAvatarId(user.getAvatar().getId());
		}
		//设置所属公司
		if(user.getSsgs() != null){
			userVo.setSsgsName(user.getSsgs().getName());
			userVo.setSsgsId(user.getSsgs().getId());
		}
		//设置签名附件
		List<AttachmentVo> attachmentVos= getUserAttachment(user);
		if (attachmentVos !=null && attachmentVos.size() >0){
			userVo.setAttachmentVos(attachmentVos);
		}

		return userVo;

	}
	//
	public static List<AttachmentVo> getUserAttachment(User user) {
		Set<Attachment> attachmentList = user.getAttachments();
		List<AttachmentVo> attachmentVoList = new ArrayList<>();
		if(attachmentList != null && !attachmentList.isEmpty()) {
            for (Attachment attachment : attachmentList) {
                AttachmentVo attachementVo = new AttachmentVo(attachment);
                attachmentVoList.add(attachementVo);
            }
        }
		return attachmentVoList;
	}

	/**
	 * dos To vos
	 */
	public static List<UserVo> convert(Collection<User> users) {
		List<UserVo> userVos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(users)) {
			for (User user : users) {
				UserVo userVo = convert(user);
				userVos.add(userVo);
			}
		}
		return userVos;
	}

	/**
	 * vo to do
	 */
	public static User convert(UserVo userVo) {
		User user = new User();
		BeanUtils.copyProperties(userVo, user);
		return user;
	}

	/**
	 * vo to do
	 */
	public static void convert(User user, UserVo userVo) {
		user.setUsername(userVo.getUsername());
		user.setPassword(userVo.getPassword());
		user.setFullName(userVo.getFullName());
		user.setEmail(userVo.getEmail());
		user.setPhone(userVo.getPhone());
		user.setPhoneNumber1(userVo.getPhoneNumber1());
		user.setPhoneNumber2(userVo.getPhoneNumber2());
		user.setSex(userVo.getSex());
	}

	/**
	 * vos To dtrvs
	 */
	public static List<DataTableRowVo> convertDtrvs(List<UserVo> userVos) {
		List<DataTableRowVo> dtrvs = new ArrayList<>();
		if (!CollectionUtils.isEmpty(userVos)) {
			for (UserVo userVo : userVos) {
				DataTableRowVo dtrv = new DataTableRowVo();
				convertDtrv(dtrv, userVo);
				dtrvs.add(dtrv);
			}
		}
		return dtrvs;
	}

	/**
	 * vo to dtrv
	 */
	public static void convertDtrv(DataTableRowVo dtrv, UserVo userVo) {
		dtrv.setObj(userVo);
		dtrv.setCheckbox(true);
		dtrv.addData(userVo.getUsername());
//		dtrv.addData(userVo.getPassword());
		dtrv.addData(userVo.getFullName());
		dtrv.addData(userVo.getSex());
		dtrv.addData(userVo.getPhone());
//		dtrv.addData(userVo.getEmail());
		String img = "<img id="+userVo.getId()+" src=\"attachment/qmxsById?Id="+userVo.getId()+"\" height=50 width=100 />";
		dtrv.addData(img);
		dtrv.addData(userVo.getRoleName());
		dtrv.addData(userVo.getDepartmentName());
		// 设置操作的button
		StringBuilder sb = new StringBuilder();
		sb.append(DataTableButtonFactory.getYellowButton("头像", "data-id='" + userVo.getId() + "'"));
		sb.append(DataTableButtonFactory.getPurpleButton("角色", "data-id='" + userVo.getId() + "'"));
		sb.append(DataTableButtonFactory.getGreenButton("部门", "data-id='" + userVo.getId() + "'"));
		sb.append(DataTableButtonFactory.getUpdateButton("data-id='" + userVo.getId() + "'"));
		sb.append(DataTableButtonFactory.getDeleteButton("data-id='" + userVo.getId() + "'"));
		dtrv.addData(sb.toString());
	}

	/**
	 * do to dtrv
	 */
	public static void convertDtrv(DataTableRowVo dtrv, User user) {
		UserVo userVo = convert(user);
		dtrv.setObj(userVo);
		dtrv.setCheckbox(true);
		dtrv.addData(user.getUsername());
//		dtrv.addData(user.getPassword());
		dtrv.addData(user.getFullName());
		dtrv.addData(user.getSex());
		dtrv.addData(user.getPhone());
		dtrv.addData(user.getEmail());
		// 设置角色
		Set<Role> roles = user.getRoles();
		StringBuilder roleSb = new StringBuilder();
		if (!CollectionUtils.isEmpty(roles)) {
			int index = 0;
			for (Role role : roles) {
				if (index != roles.size() - 1) {
					roleSb.append(role.getName() + ",");
				} else {
					roleSb.append(role.getName());
				}
				index++;
			}
		}
		dtrv.addData(roleSb.toString());
		// 设置部门
//		Department department = user.getDepartment();
		Set<Department> departments = user.getDepartments();
		StringBuilder departmentSb = new StringBuilder();
		if (!CollectionUtils.isEmpty(departments)) {
			int index = 0;
			for (Department department : departments) {
				if (index != departments.size() - 1) {
					departmentSb.append(department.getName() + ",");
				} else {
					departmentSb.append(department.getName());
				}
				index++;
			}
		}
//		dtrv.addData((department == null) ? "" : department.getName());
		dtrv.addData(departmentSb.toString());
		// 设置操作的button
		StringBuilder buttonSb = new StringBuilder();
		buttonSb.append(DataTableButtonFactory.getYellowButton("头像", "data-id='" + user.getId() + "'"));
		buttonSb.append(DataTableButtonFactory.getPurpleButton("角色", "data-id='" + user.getId() + "'"));
		buttonSb.append(DataTableButtonFactory.getGreenButton("部门", "data-id='" + user.getId() + "'"));
		buttonSb.append(DataTableButtonFactory.getUpdateButton("data-id='" + user.getId() + "'"));
		buttonSb.append(DataTableButtonFactory.getDeleteButton("data-id='" + user.getId() + "'"));
		dtrv.addData(buttonSb.toString());
	}

	/**
	 * vos to sov
	 */
	public static SelectOptionVo convertSov(List<UserVo> userVos, String username) {
		SelectOptionVo sov = new SelectOptionVo();
		if (!CollectionUtils.isEmpty(userVos)) {
			sov.addOption("", "无");
			for (UserVo userVo : userVos) {
				if (StringUtil.isEmpty(username)) {
					sov.addOption(userVo.getId(), userVo.getUsername());
					continue;
				}
				if (username.equals(userVo.getUsername())) {
					sov.addOption(userVo.getId(), userVo.getUsername(), true);
				} else {
					sov.addOption(userVo.getId(), userVo.getUsername());
				}
			}
		}
		return sov;
	}
}
