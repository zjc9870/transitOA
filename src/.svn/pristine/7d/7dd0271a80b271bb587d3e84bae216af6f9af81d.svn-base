package com.expect.admin.data.dataobject;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 新闻
 * @author zcz
 *
 */
@Entity
@Table(name = "c_news")
public class News {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	private String id;
	
	@Column(name = "tittle", length = 50)
	private String tittle;//标题
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "content", nullable = true)
	private String content;//内容
	
	@Column(name = "category", length = 10)
	private String category;//分类
	
	@Column(name = "sqsj")
//	@Temporal(TemporalType.DATE)
	private Date sqsj;//申请时间
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;//申请人

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getSqsj() {
		return sqsj;
	}

	public void setSqsj(Date sqsj) {
		this.sqsj = sqsj;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
