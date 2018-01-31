package com.expect.admin.service;

import com.expect.admin.data.dao.LcjdbRepository;
import com.expect.admin.data.dao.LcjdgxbRepository;
import com.expect.admin.data.dao.RoleJdgxbGxbRepository;
import com.expect.admin.data.dataobject.Function;
import com.expect.admin.data.dataobject.Lcjdb;
import com.expect.admin.data.dataobject.Lcjdgxb;
import com.expect.admin.data.dataobject.RoleJdgxbGxb;
import com.expect.admin.service.vo.RoleVo;
import com.expect.admin.service.vo.component.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by qifeng on 17/11/20.
 */
@Service
public class ProcessConfigurationService {

    @Autowired
    LcjdbRepository lcjdbRepository;
    @Autowired
    LcjdgxbRepository lcjdgxbRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    RoleJdgxbGxbRepository roleJdgxbGxbRepository;
    public ResultVo save(String[] allNodeName){
        ResultVo resultVo = new ResultVo();
        String[] id = new String[allNodeName.length];

        String lcid = UUID.randomUUID().toString().substring(0,30);
//        if (lcjdbRepository.findMaxLcId() !=null){
//            lcid = lcjdbRepository.findMaxLcId().getSslc();
//            lcid = String.valueOf(Integer.valueOf(lcid)+1);
//        }
        //创建流程节点
        for (int i=0;i<allNodeName.length;i++){
            Lcjdb  lcjdb = lcjdbRepository.save(new Lcjdb(allNodeName[i],lcid));
            if (lcjdb == null){
                resultVo.setMessage("创建节点失败");
                return resultVo;
            }
            id[i] = lcjdb.getId();

        }
        String startId = "S";

        for (int i=0;i<id.length;i++){
            Lcjdgxb lcjdgxb  = new Lcjdgxb(startId,id[i],null,"Y","N",lcid);
            startId = id[i];
            Lcjdgxb lcjdgxb1 = lcjdgxbRepository.save(lcjdgxb);
            if (lcjdgxb1 == null){
                resultVo.setMessage("创建流程节点失败");
                return resultVo;
            }
        }
        Lcjdgxb lcjdgxb = lcjdgxbRepository.save(new Lcjdgxb(startId,"Y",null,"Y","N",lcid));
        if(lcjdgxb == null){
            resultVo.setMessage("创建流程节点失败");
            return resultVo;
        }
        resultVo.setResult(true);
        resultVo.setMessage("配置审批节点成功");
        resultVo.setObj(id);
        return resultVo;
    }

    public ResultVo bindNodeRole(String[] nodeId,String[] roleId){
        ResultVo resultVo = new ResultVo();
        if (!isApply(roleId[0])){
            resultVo.setMessage("第一个节点没有合同申请功能");
            return resultVo;
        }

        RoleJdgxbGxb roleJdgxbGxbs = roleJdgxbGxbRepository.findByRoleId(roleId[0]);
        if (roleJdgxbGxbs !=null){
            resultVo.setMessage("第一个合同申请角色已经绑定节点,请选择其他角色");
            return resultVo;
        }

        //检查节点是否有资产管理部合同审核员和董事长角色
        boolean isZcglb =false;
        boolean isDsz = false;
        for (String id:roleId){
            RoleVo roleVo = roleService.getRoleById(id);
            if (roleVo.getName().equals("资产管理部合同审核员")){
                isZcglb = true;
            }
            if (roleVo.getName().equals("负责人")){
                isDsz = true;
            }
        }
        if (isZcglb ==false){
            resultVo.setMessage("节点未绑定资产管理部合同审核员");
            return resultVo;
        }
        if (isDsz ==false){
            resultVo.setMessage("节点未绑定负责人");
            return resultVo;
        }

        Set<String> set = new HashSet<>();
        set.add(roleId[0]);
        //检查除第一个节点外,其他节点是否有审批功能
        for (int i=1;i<roleId.length;i++){
//            RoleJdgxbGxb roleJdgxbGxb = roleJdgxbGxbRepository.findByRoleId(roleId[i]);
//            if (roleJdgxbGxb !=null){
//                int index = i+1;
//                resultVo.setMessage("第"+index+"个角色已经绑定节点,请选择其他角色");
//                return resultVo;
//            }
            set.add(roleId[i]);
            boolean isApprove = false;
            Set<Function> functions = roleService.getFunctionsByRoleId(roleId[i]);
            for (Function function:functions){
                if (function.getName() !=null &&function.getName().equals("合同审批")){
                    isApprove = true;
                }
            }
            if(isApprove == false){
                resultVo.setMessage("第"+(i+1)+"个节点没有审批功能");
                return resultVo;
            }
        }

        //判断是否有节点重复
        if (set.size() !=roleId.length) {
            resultVo.setMessage("节点重复");
            return resultVo;
        }

        for(int i=0;i<nodeId.length;i++) {
            String bz;
            if (i==0) {
                bz = "sq";
            }
            else {
                bz = "sp";
            }

            RoleJdgxbGxb roleJdgxbGxb  = new RoleJdgxbGxb(bz,nodeId[i],roleId[i],"ht");
            RoleJdgxbGxb roleJdgxbGxb1 = roleJdgxbGxbRepository.save(roleJdgxbGxb);
            if (roleJdgxbGxb1 == null) {
                resultVo.setMessage("角色节点绑定失败");
            }
        }

        resultVo.setResult(true);
        resultVo.setMessage("绑定成功");

        return resultVo;
    }

    //检查第一个节点是否有申请合同功能
    public boolean isApply(String roleId){
        Set<Function> functions = roleService.getFunctionsByRoleId(roleId);
        for (Function function:functions){
            if (function.getName() !=null &&function.getName().equals("合同申请")){
                return true;
            }
        }
        return false;
    }

}
