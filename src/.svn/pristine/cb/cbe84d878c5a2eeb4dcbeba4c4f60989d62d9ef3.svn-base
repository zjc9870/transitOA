package com.expect.admin.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expect.admin.data.dataobject.Attachment;

/**
 * 附件JPA
 */
public interface AttachmentRepository extends JpaRepository<Attachment, String> {

	/**
	 * 根据ids获取多个附件
	 */
	public List<Attachment> findByIdIn(String[] ids);
	
	/**
	 * 根据相关id获取多有的附件记录
	 * @param xgid
	 * @return
	 */
	public List<Attachment> findByXgId(String xgid);



}
