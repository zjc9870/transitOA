package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.DraftSwRepository;
import com.expect.admin.data.dao.DraftSwUserLcrzbGxbRepository;
import com.expect.admin.data.dao.RoleRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.DraftSw;
import com.expect.admin.data.dataobject.DraftSwUserLcrzbGxb;
import com.expect.admin.data.dataobject.Lcrzb;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.exception.BaseAppException;
import com.expect.admin.service.vo.DraftSwVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.utils.StringUtil;

@Service
public class DraftSwService {
	
	private final String SWLC = "4";//收文流程id
	@Autowired
	private UserService userService;
	private LcService lcService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DraftSwRepository draftSwRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private DraftSwUserLcrzbGxbRepository draftSwUserLcrzbGxbRepository;
	

	public void save(DraftSwVo draftSwVo) {
		if(draftSwVo == null) return;
		DraftSw draftSw = new DraftSw(draftSwVo);
		UserVo userVo = userService.getLoginUser();
		User user = userRepository.findOne(userVo.getId());
		draftSw.setSwr(user);
		draftSwRepository.save(draftSw);
	}
	
	public void update(DraftSwVo draftSwVo){
		if(draftSwVo == null || StringUtil.isBlank(draftSwVo.getId())) return;
		draftSwRepository.save(new DraftSw(draftSwVo));
	}
	
	public void delete(String id) {
		//1.收文的当前流程标识，判断当前状态是不是开始状态
		//2.当前用户是不是收文的收文人
		//3.删除收文，删除相关用户，删除流程日志
	}
	
	public DraftSwVo getDraftSwVoById(String id) {
		if(StringUtil.isBlank(id)) throw new BaseAppException("根据ID获取收文是ID为空");
		DraftSw draftSw = draftSwRepository.findOne(id);
		if(draftSw == null) throw new BaseAppException("id为 "+id+"的收文没有找到");
		DraftSwVo draftSwVo = new DraftSwVo(draftSw);
		return draftSwVo;
	}
	
	/**
	 * 根据页面和tab标签获取各种收文数据（所有数据都会与当前用户相关的）
	 * 收文记录页面(ym = "swjl") 1.未提交("wtj") 2. 待处理("dcl") 3.已处理(未完成)("ycl") 4.已完成("ywc")
	 * 收文批示页面(ym = "swps") 5.待批示("dps") 6.已批示("yps")
	 * 收文办理页面(ym = "swbl") 7.未办理("wbl") 8.已办理("ybl")
	 * 收文传阅页面(ym = "swcy")9.待传阅("dcy") 10.已传阅("ycy")
	 * @param ym 请求来着的页面
	 * @param tab 请求的tab
	 * @param userId 当前登录的用户的id
	 * @return
	 */
	public List<DraftSwVo> getDraftSwVoList(String ym, String tab, String userId){
		if(StringUtil.isBlank(userId)) throw new BaseAppException("当期用户未登录");
		if(StringUtil.equals(ym, "swjl")) {
			if(StringUtil.equals(tab, "wtj")) return getWtjSw(userId);
			if(StringUtil.equals(tab, "dcl")) return getDclSw(userId);
			if(StringUtil.equals(tab, "ycl")) return getYclSw(userId);
			if(StringUtil.equals(tab, "ywc")) return getYwcSw(userId);
		}
		else if(StringUtil.equals(ym, "swps")) {//待确定
			if(StringUtil.equals(tab, "dps")) return getDps();
			if(StringUtil.equals(tab, "yps")) return null;
		}
		else {
			String ryfl = null;
			boolean sfcl = false;
			if(StringUtil.equals(ym, "swbl")) {
				ryfl = "blr";
				if(StringUtil.equals(tab, "ybl")) sfcl = true;
			}
			else if(StringUtil.equals(ym, "swcy")) {
				ryfl = "cyr";
				if(StringUtil.equals(tab, "ycy")) sfcl = true;
			}
			return getSwblOrSwcy(userId, ryfl, sfcl);
		}
		return getDraftSwVoListFromDraftSwList(null);
	}
	
	/**
	 * 获取收文办理或者收文传阅的收文
	 * @param userId 相关用户id
	 * @param ryfl 人员分类（"blr":办理人  "cyr"：传阅人）
	 * @param sfcl（是否处理 true：已经处理过的（已办理， 已传阅） false：（未办理， 未传阅））
	 * @return
	 */
	private List<DraftSwVo> getSwblOrSwcy(final String userId, final String ryfl, final boolean sfcl) {
		if(StringUtil.isBlank(ryfl)) return getDraftSwVoListFromDraftSwList(null);
		List<DraftSw> wclDraftSwList = draftSwRepository.findAll(new Specification<DraftSw>() {
			@Override
			public Predicate toPredicate(Root<DraftSw> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<DraftSw, DraftSwUserLcrzbGxb> join = root.joinSet("draftSwUserLcrzbGxbs", JoinType.LEFT);
				
				Predicate predicateUser= cb.equal(join.get("user").get("is").as(String.class), userId);
				Predicate predicateRyfl = cb.equal(join.get("ryfl").as(String.class), ryfl);
				
				//未处理的相关联的lcrzb记录为null，已处理的不是null
				Predicate predicateSfcl;
				if(sfcl) predicateSfcl = cb.isNotNull(join.get("lcrz").as(Lcrzb.class));
				else predicateSfcl = cb.isNull(join.get("lcrz").as(Lcrzb.class));
				
				return cb.and(predicateUser, predicateRyfl, predicateSfcl);
			}
		});
		return getDraftSwVoListFromDraftSwList(wclDraftSwList);
	}

	/**
	 * 领导待批示
	 * @return
	 */
	public List<DraftSwVo> getDps(){
		return getDraftSwVoListFromDraftSwList(draftSwRepository.findBySwzt("16"));
	}
	/**
	 * 获取某用户未提交的收文（收文人id是userid 收文分类是‘W’）
	 * @param userId
	 * @return
	 */
	public List<DraftSwVo> getWtjSw(String userId) {
		List<DraftSw> draftSwList = draftSwRepository.findBySwr_idAndSwfl(userId, DraftSw.SWFL_WTJ);
		return getDraftSwVoListFromDraftSwList(draftSwList);
	}
	
	/**
	 * 获取申请人的待处理收文 
	 * @param userId
	 * @return
	 */
	public List<DraftSwVo> getDclSw(final String userId){
		String condition = lcService.getStartCondition(SWLC);
		List<DraftSw> dclDraftSwList = null;
		//第一轮的待处理收文
		List<DraftSw> draftSwList = draftSwRepository.findBySwr_idAndSwztAndSwfl(userId, condition, DraftSw.SWFL_SLYTJ);
		//不是第一轮的待处理收文
		List<DraftSw> draftSwList2 = draftSwRepository.findBySwr_idAndSwztAndSwfl(userId, condition, DraftSw.SWFL_DLYTJ);
		if(draftSwList == null)dclDraftSwList = draftSwList2;
		else if(draftSwList2 == null) dclDraftSwList = draftSwList;
		else{
			draftSwList.addAll(draftSwList2);
			dclDraftSwList = draftSwList;
		}
		return getDraftSwVoListFromDraftSwList(dclDraftSwList);
	}
	
	/**
	 *收文发起人已处理但是还没有完成的收文
	 * @param userId
	 * @return
	 */
	public List<DraftSwVo> getYclSw(final String userId) {
		final String condition = lcService.getStartCondition(SWLC);
		List<DraftSw> draftSwList = draftSwRepository.findAll(new Specification<DraftSw>() {

			@Override
			public Predicate toPredicate(Root<DraftSw> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				
				list.add(cb.equal(root.get("swr").get("id").as(String.class), userId));
				list.add(cb.notEqual(root.get("swzt").as(String.class), condition));
				list.add(cb.notEqual(root.get("swzt").as(String.class), "Y"));
				
				Predicate[] predicate = new Predicate[list.size()];
				Predicate psssredicate = cb.and(list.toArray(predicate));
				return psssredicate;
			}
		});
		return getDraftSwVoListFromDraftSwList(draftSwList);
	}
	
	/**
	 * 发起人发起的已完成的收文（收文状态为Y）
	 * @param userId
	 * @return
	 */
	public List<DraftSwVo> getYwcSw(final String userId) {
		List<DraftSw> draftSwList = draftSwRepository.findBySwr_idAndSwztOrderByTjsjDesc(userId, "Y");
		return getDraftSwVoListFromDraftSwList(draftSwList);
	}

	/**
	 * 将DoList转化为VoList
	 * @param draftSwList
	 * @return
	 */
	private List<DraftSwVo> getDraftSwVoListFromDraftSwList(List<DraftSw> draftSwList) {
		if(draftSwList == null || draftSwList.isEmpty()) return new ArrayList<>(0);
		List<DraftSwVo> draftSwVoList = new ArrayList<>(draftSwList.size());
		for (DraftSw draftSw : draftSwList) {
			draftSwVoList.add(getDraftSwVoFromDraftSw(draftSw));
		}
		return draftSwVoList;
	}

	/**
	 * 将Do转化为Vo
	 * @param draftSw
	 * @return
	 */
	private DraftSwVo getDraftSwVoFromDraftSw(DraftSw draftSw) {
		DraftSwVo draftSwVo = new DraftSwVo();
		BeanUtils.copyProperties(draftSw, draftSwVo);
		return draftSwVo;
	}
	
//	public List<DraftSwVo> getDraftSwVoByUserAndCondition(String userId,String condition,String lx) {
//		List<DraftSwVo> draftSwVoList = new ArrayList<DraftSwVo>();
//		List<DraftSw> draftSwList = new ArrayList<DraftSw>();
//		if(StringUtil.isBlank(condition)) return new ArrayList<>();
//		//未提交
//		if(StringUtil.equals(lx, "wtj")){
//			draftSwList = draftSwRepository.findBySwr_idAndSwztOrderByTjsjDesc(userId, condition);
//		}
//		//待批示
//		if(StringUtil.equals(lx, "dps")) {
////			draftSwList = draftSwRepository.
//		}
//		//待传阅
//		//待办理
//		//已完成
//		if(draftSwList==null){
//			return draftSwVoList;
//		}
//		for(DraftSw sw:draftSwList){
//			draftSwVoList.add(new DraftSwVo(sw));
//		}
//		return draftSwVoList;
//	}
//	public void addPyr(List<String> userIdList, final DraftSwVo swVo){
//		Role role = roleRepository.getOne("roleid");//改成批阅人的roleid
//		List<User> userDoList = new ArrayList<User>();
//
//		if(role!=null){
//			for(String userId:userIdList){
//				User user = userRepository.getOne(userId);
//				if(user!=null){
//					userDoList.add(user);
//					role.getUsers().add(user);
//					user.getRoles().add(role);
//					userRepository.save(user);
//				}
//			}
//		}
//		roleRepository.save(role);
//		DraftSw sw = new DraftSw(swVo);
//		sw.setPyrs(userDoList);
//		draftSwRepository.save(sw);
//		return;
//	}
//	
//	public void addBlr(String userId,DraftSwVo draftSwVo){
//		Role role = roleRepository.findOne("roleid");//改成办理人的id
//		User user = userRepository.findOne("userId");
//		if(role!=null){
//			role.getUsers().add(user);
//		}
//		if(user!=null){
//			user.getRoles().add(role);
//		}
//		DraftSw draftSw = new DraftSw(draftSwVo);
//		draftSw.setBlr(userId);
//		draftSwRepository.save(draftSw);
//	}
//
//	public boolean py(String draftSwId, String pyrId) {
//		User pyr = userRepository.getOne(pyrId);
//		DraftSw draftSw = draftSwRepository.getOne(draftSwId);
//		if(draftSw!=null){
//			draftSw.getPyrs().remove(pyr);
//			draftSwRepository.save(draftSw);
//			return draftSw.getPyrs().size() == 0;
//		}else{
//			return false;
//		}
//	}
}
