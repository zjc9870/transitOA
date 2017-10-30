package com.expect.admin.data.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 备忘录
 */
@Entity
@Table(name = "c_memo")
public class Memo {

	private String id;
	private String year;// 年
	private String month;// 月
	private String day;// 日
	private String time;// 时间
	private String description;// 描述
	private User user;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", nullable = false, unique = true, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "year", length = 7)
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Column(name = "month", length = 7)
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Column(name = "day", length = 7)
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	@Column(name = "time", length = 31)
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Column(name = "description", length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
