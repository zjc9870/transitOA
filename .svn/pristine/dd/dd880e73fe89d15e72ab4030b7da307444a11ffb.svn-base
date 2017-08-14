package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 文件相关人员
 * 发文  的 主送 抄送  抄报人
 * 收文的办理人  传阅人
 * @author zcz
 *
 */
@Entity
@Table(name = "wjxgry")
public class Wjxgry {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	
	@Column(name = "draft_id", length = 32)
	private String draftId;
	
	@Column(name = "user_id", length = 32)
	private String userId;
	
	@Column(name = "category", length = 10)
	private String category;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDraftId() {
		return draftId;
	}

	public void setDraftId(String draftId) {
		this.draftId = draftId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
