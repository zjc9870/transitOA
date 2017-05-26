package com.expect.admin.service;

import com.expect.admin.data.dao.NotifyObjectRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.NotifyObject;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.NotifyObjectVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.utils.Pinyin4jUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;



/**
 * Created by qifeng on 17/5/10.
 */
@Service
public class NotifyObjectService {
    @Autowired
    private NotifyObjectRepository notifyObjectRepository;
    @Autowired
    private  UserService userService;
    @Autowired
    private UserRepository userRepository;


    public List<NotifyObjectVo> getNotifyObjectByTzdxlx(String tzdxfl){
        List<NotifyObjectVo> notifyObjectVos=new ArrayList<>();
        List<NotifyObject> notifyObjectList = notifyObjectRepository.findByTzdxfl(tzdxfl);
        if (notifyObjectList !=null){
            for (NotifyObject notifyObject:notifyObjectList){
                NotifyObjectVo notifyObjectVo=new NotifyObjectVo(notifyObject);
                notifyObjectVos.add(notifyObjectVo);
            }
        }
        return notifyObjectVos;
    }

    public List<NotifyObjectVo> getAllNotifyObject(){
        List<NotifyObjectVo> notifyObjectVos=new ArrayList<>();
        List<NotifyObject> notifyObjectList = notifyObjectRepository.findAll();
        if (notifyObjectList !=null){
            for (NotifyObject notifyObject:notifyObjectList){
                NotifyObjectVo notifyObjectVo=new NotifyObjectVo(notifyObject);
//                String tzdxfl = notifyObjectVo.getTzdxfl();
//                String tzdxPingyingHead= Pinyin4jUtil.converterToFirstSpell(notifyObjectVo.getTzdx());
//                if (tzdxfl.equals("集团高管")|| tzdxfl.equals("集团部门")){
//                    tzdxPingyingHead = "jt"+tzdxPingyingHead;
//                }
//                else if (tzdxfl.equals("公司高管") || tzdxfl.equals("公司部门")){
//                    tzdxPingyingHead = "gs" + tzdxPingyingHead;
//                }
//
//
//                notifyObjectVo.setTzdxjx(tzdxPingyingHead);
                notifyObjectVo.setUserName(notifyObject.getUser().getUsername());
                notifyObjectVos.add(notifyObjectVo);
            }
        }
        return notifyObjectVos;
    }


    public ResultVo save(String tzdx,String tzdxlx){
        ResultVo resultVo = new ResultVo();
        resultVo.setMessage("增加失败");
        if (StringUtils.isEmpty(tzdx)) {
            resultVo.setMessage("对象名不能为空");
            return resultVo;
        }
        resultVo=isRepeated(resultVo,tzdx,tzdxlx);
        if (resultVo.getMessage().equals("对象名重复"))
            return  resultVo;

        resultVo = isUserExist(resultVo,tzdx);
        if (resultVo.getMessage().equals("用户姓名不存在,请先建立此用户"))
            return resultVo;

        NotifyObjectVo notifyObjectVo=new NotifyObjectVo();
        notifyObjectVo.setTzdx(tzdx);
        notifyObjectVo.setTzdxfl(tzdxlx);
        NotifyObject notifyObject=new NotifyObject(notifyObjectVo);

        User user = userRepository.findByFullName(tzdx);
        notifyObject.setUser(user);

        NotifyObject result=notifyObjectRepository.save(notifyObject);
        if (result != null) {
            resultVo.setResult(true);
            resultVo.setMessage("增加成功");
            resultVo.setObj(result);
        }
        return resultVo;
    }

    /**
     * 查询对象名在用户表中是否存在,存在的话(tzdx等于fullname)
     * @param resultVo
     * @param tzdx
     * @return
     */
    public ResultVo isUserExist(ResultVo resultVo,String tzdx){
        User user = userRepository.findByFullName(tzdx);

        if (user ==null){
            resultVo.setMessage("用户姓名不存在,请先建立此用户");
        }
        return resultVo;
    }
    public ResultVo delete(String id){
        ResultVo resultVo = new ResultVo();
        resultVo.setMessage("删除失败");
        NotifyObject notifyObject = notifyObjectRepository.findOne(id);
        if (notifyObject == null) {
            return resultVo;
        }

        notifyObjectRepository.delete(id);
        resultVo.setMessage("删除成功");
        resultVo.setResult(true);
        return resultVo;
    }

    public ResultVo update(String id,String tzdx,String tzdxlx){
        ResultVo resultVo = new ResultVo();
        resultVo.setMessage("修改失败");
        if (StringUtils.isEmpty(tzdx)) {
            resultVo.setMessage("对象名不能为空");
            return resultVo;
        }
        resultVo=isRepeated(resultVo,tzdx,tzdxlx);
        if (resultVo.getMessage().equals("对象名重复"))
            return  resultVo;

        resultVo = isUserExist(resultVo,tzdx);
        if (resultVo.getMessage().equals("用户姓名不存在,请先建立此用户"))
            return resultVo;

        NotifyObject notifyObject = notifyObjectRepository.findOne(id);
        if (notifyObject == null) {
            return resultVo;
        }
        notifyObject.setTzdx(tzdx);
        NotifyObject result=notifyObjectRepository.save(notifyObject);

        if (result != null) {
            resultVo.setResult(true);
            resultVo.setMessage("修改成功");
            resultVo.setObj(result);
        }
        return resultVo;
    }

    public  ResultVo isRepeated(ResultVo resultVo,String tzdx, String tzdxlx){
        List<NotifyObject> notifyObjectList= new ArrayList<>();
        notifyObjectList = notifyObjectRepository.findByTzdxfl(tzdxlx);
        if (notifyObjectList !=null){
            for (NotifyObject notifyObject:notifyObjectList){
                if (notifyObject.getTzdx().equals(tzdx)){
                    resultVo.setMessage("对象名重复");
                    return resultVo;
                }
            }
        }
        return resultVo;
    }




}