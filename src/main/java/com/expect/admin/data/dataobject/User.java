package com.expect.admin.data.dataobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户
 */
@Entity
@Table(name = "c_user")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	@Column(name = "username", length = 32)
	private String username;// 用户名
	@Column(name = "password", length = 100)
	private String password;// 密码
	@Column(name = "full_name", length = 32)
	private String fullName;// 姓名
	@Column(name = "sex", length = 2)
	private String sex;// 性别
	@Column(name = "email", length = 64)
	private String email;// 电子邮件
	@OneToOne
	@JoinColumn(name = "avatar_id")
	private Attachment avatar;// 头像
	//每个人都有3个电话
	@Column(name = "phone", length = 16)
	private String phone;// 手机
	@Column(name = "phoneNumber1", length = 16)
	private String phoneNumber1;
	@Column(name = "phoneNumber2", length = 16)
	private String phoneNumber2;
	/**
	 * joinColumns代表维护端
	 * 
	 * inverseJoinColumns代表被维护段
	 * 
	 * 维护端负责管理多对多关系，被维护端不负责，就代表在被维护端setXXX不会有效果
	 */
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "c_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "c_user_department", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "department_id"))
	private Set<Department> departments;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "ssgs_id")
	private Department ssgs;//所属公司
	
	@Column(name = "zw", length = 32)
	private String zw;//职位

	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "c__user_attachment", joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "attachment_id"))
	private Set<Attachment> attachments;//附件


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return this.password;
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

	public Attachment getAvatar() {
		return avatar;
	}

	public void setAvatar(Attachment avatar) {
		this.avatar = avatar;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public Department getSsgs() {
		return ssgs;
	}

	public void setSsgs(Department ssgs) {
		this.ssgs = ssgs;
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
		Set<Role> roles = this.getRoles();
		for (Role sysRole : roles) {
			auths.add(new SimpleGrantedAuthority(sysRole.getName()));
		}
		return auths;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}
}
