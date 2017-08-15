package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 关联 收文 人员 流程日志表
 * 一个收文对应多条记录
 * 每条记录关联一个收文 一个人 一条流程日志表记录
 * 
 * ryfl（人员分类）字段用来区分传阅人("cyr") 办理人("blr"), 领导（“ld”）
 * @author zcz
 *
 */
@Entity
@Table(name = "s_draftSw_user_lcrzb")
public class DraftSwUserLcrzbGxb {
	/**
	 * 人员分类 领导
	 */
	public static final String RYFL_LD = "ld";
	/**
	 * 人员分类 传阅人
	 */
	public static final String RYFL_CYR = "cyr";
	/**
	 * 人员分类 办理人
	 */
	public static final String RYFL_BLR = "blr";
	/**
	 * 人员分类 发起人
	 */
	public static final String RYFL_SWR = "swr";
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;//人员（办理人 传阅人）
	
	@OneToOne
	@JoinColumn(name = "lcrzb_id")
	private Lcrzb lcrz;//流程日志
	
	@ManyToOne
	@JoinColumn(name = "draftSw_id")
	private DraftSw draftSw;//收文
	
	@Column(name = "ryfl", length = 10)
	private String ryfl;//人员分类
	
	public DraftSwUserLcrzbGxb(){}
	
	public DraftSwUserLcrzbGxb(User user, DraftSw draftSw, String ryfl) {
		super();
		this.user = user;
		this.draftSw = draftSw;
		this.ryfl = ryfl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Lcrzb getLcrz() {
		return lcrz;
	}

	public void setLcrz(Lcrzb lcrz) {
		this.lcrz = lcrz;
	}

	public DraftSw getDraftSw() {
		return draftSw;
	}

	public void setDraftSw(DraftSw draftSw) {
		this.draftSw = draftSw;
	}

	public String getRyfl() {
		return ryfl;
	}

	public void setRyfl(String ryfl) {
		this.ryfl = ryfl;
	}

}