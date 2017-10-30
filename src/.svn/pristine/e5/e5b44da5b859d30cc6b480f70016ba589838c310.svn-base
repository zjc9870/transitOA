package com.expect.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expect.admin.data.dao.LcjdgxbRepository;
import com.expect.admin.data.dao.LcrzbRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.Lcjdgxb;
import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.LcrzbVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.DateUtil;
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
		List<LcrzbVo> lcrzbVoList = new ArrayList<>();
		if(lcrzbList == null || lcrzbList.size() == 0) return lcrzbVoList;
		for (Lcrzb lcrzb : lcrzbList) {
			if(StringUtil.equals(lcrzb.getDyjd(), "T")) continue;
			lcrzbVoList.add(new LcrzbVo(lcrzb));
		}
		return lcrzbVoList;	
	}

	/**
	 * 获取审核完成流程日志
	 * @param id(合同id)
	 * @return
     */
	public LcrzbVo getLastLcrzByClnrid(String id){
		List<Lcrzb> lcrzbList = lcrzbRepository.findByClnridOrderByClsjDesc(id);
		LcrzbVo lcrzbVo = new LcrzbVo();
		if(lcrzbList.size() >0 && lcrzbList !=null){
			Lcrzb lcrzb = lcrzbList.get(0);
			if(lcrzb.getCljg().equals("通过")||lcrzb.getCljg().equals("不通过")){
				lcrzbVo = new LcrzbVo(lcrzb);
			}

		}
		return lcrzbVo;

	}
	
	/**
	 * 转换
	 * @param lcrzbList
	 * @return
	 */
	public static List<LcrzbVo> convert(Set<Lcrzb> lcrzbList) {
		List<LcrzbVo> lcrzbVoList = new ArrayList<LcrzbVo>();
		if(lcrzbList == null || lcrzbList.size() == 0) return lcrzbVoList;
		for (Lcrzb lcrzb : lcrzbList) {
			if(StringUtil.equals(lcrzb.getDyjd(), "T")) continue;
			lcrzbVoList.add(new LcrzbVo(lcrzb));
		}
		Collections.sort(lcrzbVoList, new Comparator<LcrzbVo>() {

			@Override
			public int compare(LcrzbVo c1, LcrzbVo c2) {
				if(StringUtil.isBlank(c1.getClsj())) return -1;
				if(StringUtil.isBlank(c2.getClsj())) return 1;
				Date d1 = DateUtil.parse(c1.getClsj(), DateUtil.fullFormat);
				Date d2 = DateUtil.parse(c2.getClsj(), DateUtil.fullFormat);
				long dif = DateUtil.getDiffSeconds(d1, d2);
				return (dif > 0) ? 1 : 
					((dif < 0) ? -1 : 0);
			}
		});
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
		
//		Contract contract = contractRepository.findOne(id);
//		contract.getLcrzSet().addAll(lcrzbList);
//		contractRepository.save(contract);
	}
	
	/**
	 * 流程日志内容的保存（新增的记录默认是显示的）
	 * @param lcrzbVo 日志基本信息
	 * @param clnrid 日志关联的“东西”的id
	 * @param clnrfl “东西”的分类
	 * @param cunCondition 此条记录关联的流程节点
	 */
	@Transactional
	public String save(LcrzbVo lcrzbVo, String clnrid, String clnrfl, String curCondition) {
		UserVo userVo = userService.getLoginUser();
		User user = userRepository.findOne(userVo.getId());
		
		Lcrzb lcrzb = new Lcrzb(lcrzbVo, user, clnrid, clnrfl);//这一步里面获取了当前的时间clsj ,clnrfl处理内容分类
		lcrzb.setDyjd(curCondition);
		if(StringUtil.equals(lcrzbVo.getCljg(), "不通过") && !StringUtil.equals(curCondition, "3")){
			lcrzb.setSfxs("N");
		}else lcrzb.setSfxs("Y");
		Lcrzb savedLcrz = lcrzbRepository.save(lcrzb);
		return savedLcrz.getId();
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

	/**
	 * @param clnrid
	 * @param clnrfl
     * @return
     */
	public User getUser(String clnrid,String clnrfl){
		List<Lcrzb> lcrzbList = lcrzbRepository.findByClnrflAndClnridOrderByClsjDesc(clnrfl,clnrid);
		if (lcrzbList !=null && lcrzbList.size() >=0){
			return lcrzbList.get(0).getUser();
		}
		return null;
	}
	
}
