package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.LcjdgxbRepository;
import com.expect.admin.data.dao.LcrzbRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Lcjdgxb;
import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.StringUtil;

/**
 * 流程日志表服务
 * @author zcz
 *
 */
@Service
public class LcrzbService {
	
	@Autowired
	private LcrzbRepository lcrzbRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private LcjdgxbRepository lcjdgxbRepository;
	
	/**
	 * 获取某个文件的所有可显示的审批记录（sfxs）
	 * @param id(文件id 如合同id)
	 * @return
	 */
	public List<LcrzbVo> getKxsLcrzbVoList(String id){
		List<Lcrzb> lcrzbList = lcrzbRepository.findByClnridAndSfxsOrderByClsjAsc(id, "Y");
		List<LcrzbVo> lcrzbVoList = new ArrayList<LcrzbVo>();
		if(lcrzbList == null || lcrzbList.size() == 0) return lcrzbVoList;
		for (Lcrzb lcrzb : lcrzbList) {
			if(StringUtil.equals(lcrzb.getDyjd(), "T")) continue;
			lcrzbVoList.add(new LcrzbVo(lcrzb));
		}
		return lcrzbVoList;	
	}
	
	/**
	 * 获取某人文件的所有审批记录（包括可显示的和不显示的），共管理员查看
	 * @param id
	 * @return
	 */
	public List<LcrzbVo> getLcrzbVoList(String id){
		List<Lcrzb> lcrzbList = lcrzbRepository.findByClnridOrderByClsjAsc(id);
		List<LcrzbVo> lcrzbVoList = new ArrayList<LcrzbVo>();
		if(lcrzbList == null || lcrzbList.size() == 0) return lcrzbVoList;
		for (Lcrzb lcrzb : lcrzbList) {
			lcrzbVoList.add(new LcrzbVo(lcrzb));
		}
		return lcrzbVoList;	
	}
	
	/**
	 *  退回时 退回到某个节点 该节点之后的所有记录都不显示，只有管理员可以查看
	 * @param id（文件的id）
	 * @param lcbs文件的流程标识
	 * @param curCondition文件现在的状态（退回后的）
	 * 先把所有的记录都设置为不显示
	 */
	public void setLcrzSfxs(String id, String lcbs, String curCondition) {
		List<Lcrzb> lcrzbList = lcrzbRepository.findByClnridOrderByClsjAsc(id);
		Map<String, Lcrzb> lcrzbMap = new HashMap<String, Lcrzb>();
		for (Lcrzb lcrzb : lcrzbList) {
			lcrzb.setSfxs("N");
			lcrzbMap.put(lcrzb.getDyjd(), lcrzb);
		}
		
		List<Lcjdgxb> lcjdgxbList = lcjdgxbRepository.findByLcbsOrderByXssx(lcbs);
		for (Lcjdgxb lcjdgxb : lcjdgxbList) {
			String con = lcjdgxb.getJsjd();
			if(StringUtil.equals(curCondition, con) || StringUtil.equals(con, "Y")) break;
			Lcrzb lcrzb = lcrzbMap.get(con);
			if(lcrzb == null) continue;
			lcrzb.setSfxs("Y");
		}
		
		lcrzbRepository.save(lcrzbList);
	}
	
	/**
	 * 流程日志内容的保存（新增的记录默认是显示的）
	 * @param lcrzbVo 日志基本信息
	 * @param clnrid 日志关联的“东西”的id
	 * @param clnrfl “东西”的分类
	 * @param cunCondition 此条记录关联的流程节点
	 */
	public void save(LcrzbVo lcrzbVo, String clnrid, String clnrfl, String cunCondition) {
		UserVo userVo = userService.getLoginUser();
		User user = userRepository.findOne(userVo.getId());
		
		Lcrzb lcrzb = new Lcrzb(lcrzbVo, user, clnrid, clnrfl);
		lcrzb.setDyjd(cunCondition);
		if(StringUtil.equals(lcrzbVo.getCljg(), "不通过")){
			lcrzb.setSfxs("N");
		}else lcrzb.setSfxs("Y");
		lcrzb = lcrzbRepository.save(lcrzb);
	}
	
	
	/**
	 * 获取某人用户某段时间内的所有流程操作日志
	 * @param userId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<LcrzbVo> getLcrzbVoListByUserIdAndDate(String userId, Date start, Date end) {
		List<LcrzbVo> lcrzbVoList = new ArrayList<LcrzbVo>();
		User user = userRepository.findOne(userId);
		List<Lcrzb> lcrzbList = lcrzbRepository.findByUserAndClsjBetweenOrderByClsjDesc(user, start, end);
		if(lcrzbList == null || lcrzbList.size() == 0) return lcrzbVoList;
		for (Lcrzb lcrzb : lcrzbList) {
			lcrzbVoList.add(new LcrzbVo(lcrzb));
		}
		return lcrzbVoList;
	}
	
	public void delete(String wjId) {
		lcrzbRepository.delete(
				lcrzbRepository.findByClnridOrderByClsjAsc(wjId));
	}
	
}
