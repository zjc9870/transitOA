package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 流程节点表
 * @author zcz
 *
 */
@Entity
@Table(name = "lcjdb")
public class Lcjdb {
	/**
	 * 起始节点分类标识
	 */
	public static final String START_CATEGORY = "start";
	/**
	 * 中间节点分类标识
	 */
	public static final String MIDDLE_CATEGORY = "middle";
	/**
	 * 结束节点分类标识
	 */
	public static final String END_CATEGORY = "end";
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	
	@Column(name = "name", length = 20)
	private String name;//节点名称
	
	@Column(name = "sslc_id")
	private String sslc;//所属流程
	
//	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
//	@Column(name = "role_id")
//	private Role role;//相关角色
	@Column(name = "categoty", length = 10)
	private String category;//节点类型(起始节点， 中间节点， 结束节点)
	@Column(name = "shbm", length = 32)
	private String shbm;//是否部门内部审核
	@Column(name = "shgs", length = 32)
	private String shgs;//是否公司内部审核


	public Lcjdb(){

	}
	public Lcjdb(String name,String sslc){
		this.name = name;
		this.sslc = sslc;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSslc() {
		return sslc;
	}

	public void setSslc(String sslc) {
		this.sslc = sslc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getShbm() {
		return shbm;
	}

	public void setShbm(String shbm) {
		this.shbm = shbm;
	}

	public String getShgs() {
		return shgs;
	}

	public void setShgs(String shgs) {
		this.shgs = shgs;
	}
}
