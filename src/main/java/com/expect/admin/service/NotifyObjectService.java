package com.expect.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expect.admin.data.dao.NotifyObjectRepository;
import com.expect.admin.data.dao.UserRepository;
import com.expect.admin.data.dataobject.NotifyObject;
import com.expect.admin.data.dataobject.User;
import com.expect.admin.service.vo.NotifyObjectVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.ResultVo;



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

    public List<NotifyObjectVo> getRelatedNotifyObject(){
        List<NotifyObjectVo> notifyObjectVos = new ArrayList<>();
        UserVo userVo = userService.getLoginUser();
        List<NotifyObject> notifyObjectList = new ArrayList<>();
        if(userVo.getDepartmentName().contains("集团")){
            notifyObjectList.addAll(notifyObjectRepository.findByTzdxfl("集团高管"));
            notifyObjectList.addAll(notifyObjectRepository.findByTzdxfl("集团部门"));
        }else if(userVo.getDepartmentName().contains("东山公交")){
            notifyObjectList.addAll(notifyObjectRepository.findByTzdxfl("东山公交高管"));
            notifyObjectList.addAll(notifyObjectRepository.findByTzdxfl("东山公交部门"));
        }
        notifyObjectList.addAll(notifyObjectRepository.findByTzdxfl("公司办公室"));
        if (notifyObjectList !=null){
            for (NotifyObject notifyObject:notifyObjectList){
                NotifyObjectVo notifyObjectVo=new NotifyObjectVo(notifyObject);
                notifyObjectVo.setUserName(notifyObject.getUser().getUsername());
                notifyObjectVos.add(notifyObjectVo);
            }
        }
        return notifyObjectVos;
    }

    public List<NotifyObjectVo> getAllNotifyObject(){
        List<NotifyObjectVo> notifyObjectVos=new ArrayList<>();
        UserVo userVo = userService.getLoginUser();
//        String roleName = userVo.getRoleName();
        List<NotifyObject> notifyObjectList;
        List<NotifyObject> bgsNotifyObjectList;//公司办公室和集团办公室
        //同一公司办公室通过ssgsid获得同一公司通知对象
//        if(roleName.contains("其他公司办公室")){
        if(!userVo.getDepartmentName().contains("集团部门")){
            String ssgsid = userVo.getSsgsId();
            notifyObjectList = notifyObjectRepository.findBySsgsid(ssgsid);
            //构建集团办公室和公司办公室的对象合集，由于在原来的结构上修改功能，所以不那么好看
            bgsNotifyObjectList = notifyObjectRepository.findByTzdxfl("公司办公室");
            List<NotifyObject> jtbm = notifyObjectRepository.findByTzdxfl("集团部门");
            if(jtbm!=null){
             for(NotifyObject jtbgsNO:jtbm){
                 if(jtbgsNO.getTzdx().contains("集团办公室")) {
                     bgsNotifyObjectList.add(jtbgsNO);
                     System.out.println(jtbgsNO.getTzdx());
                 }
             }
            }
            if(bgsNotifyObjectList!=null){
                notifyObjectList.removeAll(bgsNotifyObjectList);
                notifyObjectList.addAll(bgsNotifyObjectList);
            }
        }
        else{
            notifyObjectList = notifyObjectRepository.findAll();
        }

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
//        for(int i=0;i<notifyObjectVos.size();i++)
//        System.out.println(notifyObjectVos.get(i).getTzdx());
        return notifyObjectVos;
    }


    public ResultVo save(String tzdx,String tzdxlx){
        ResultVo resultVo = new ResultVo();
        resultVo.setMessage("增加失败");
        if (StringUtils.isEmpty(tzdx)) {
            resultVo.setMessage("输入不能为空");
            return resultVo;
        }

        if( tzdx.indexOf("(")==-1 || tzdx.charAt(tzdx.length()-1) !=')'){
            resultVo.setMessage("输入格式有误");
            return  resultVo;
        }
        resultVo=isRepeated(resultVo,tzdx,tzdxlx);
        if (resultVo.getMessage().equals("对象名重复"))
            return  resultVo;

        resultVo = isUserExist(resultVo,tzdx);
        if (resultVo.getMessage().equals("用户名不存在,请先建立此用户") || resultVo.getMessage().equals("姓名错误"))
            return resultVo;


        NotifyObjectVo notifyObjectVo=new NotifyObjectVo();
        notifyObjectVo.setTzdx(tzdx);
        notifyObjectVo.setTzdxfl(tzdxlx);
        NotifyObject notifyObject=new NotifyObject(notifyObjectVo);

        int start=tzdx.indexOf('(');
        String userName = tzdx.substring(0,start);
        User user = userRepository.findByUsername(userName);
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
     * 查询对象名在用户表中是否存在
     * @param resultVo
     * @param tzdx
     * @return
     */
    public ResultVo isUserExist(ResultVo resultVo,String tzdx){
        int start=tzdx.indexOf('(');
        String userName = tzdx.substring(0,start);
        String fullName = tzdx.substring(start+1,tzdx.length()-1);
        User user = userRepository.findByUsername(userName);

        if (user ==null){
            resultVo.setMessage("用户名不存在,请先建立此用户");
            return resultVo;
        }
        if(!user.getFullName().equals(fullName)){
            resultVo.setMessage("姓名错误");
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
        if( tzdx.indexOf("(")==-1 || tzdx.charAt(tzdx.length()-1) !=')'){
            resultVo.setMessage("输入格式有误");
            return  resultVo;
        }
        resultVo=isRepeated(resultVo,tzdx,tzdxlx);
        if (resultVo.getMessage().equals("对象名重复"))
            return  resultVo;

        resultVo = isUserExist(resultVo,tzdx);
        if (resultVo.getMessage().equals("用户名不存在,请先建立此用户") || resultVo.getMessage().equals("姓名错误"))
            return resultVo;

        NotifyObject notifyObject = notifyObjectRepository.findOne(id);
        if (notifyObject == null) {
            return resultVo;
        }
        notifyObject.setTzdx(tzdx);


        int start=tzdx.indexOf('(');
        String userName = tzdx.substring(0,start);
        User user = userRepository.findByUsername(userName);
        notifyObject.setUser(user);

        NotifyObject result=notifyObjectRepository.save(notifyObject);

        if (result != null) {
            resultVo.setResult(true);
            resultVo.setMessage("修改成功");
            resultVo.setObj(result);
        }
        return resultVo;
    }

    public  ResultVo isRepeated(ResultVo resultVo,String tzdx, String tzdxlx){
        List<NotifyObject> notifyObjectList = notifyObjectRepository.findByTzdxfl(tzdxlx);
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
