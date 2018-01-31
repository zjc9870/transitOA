package com.expect.admin.service;

import com.expect.admin.data.dao.LcjdbRepository;
import com.expect.admin.data.dao.LcjdgxbRepository;
import com.expect.admin.data.dao.RoleJdgxbGxbRepository;
import com.expect.admin.data.dao.RoleRepository;
import com.expect.admin.data.dataobject.Lcjdgxb;
import com.expect.admin.data.dataobject.Role;
import com.expect.admin.data.dataobject.RoleJdgxbGxb;
import com.expect.admin.service.vo.ProcessDetailVo;
import com.expect.admin.service.vo.ProcessVo;
import com.expect.admin.service.vo.component.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qifeng on 17/12/8.
 */
@Service
public class ProcessRecordService {
    @Autowired
    LcjdgxbRepository lcjdgxbRepository;
    @Autowired
    LcjdbRepository lcjdbRepository;
    @Autowired
    RoleJdgxbGxbRepository roleJdgxbGxbRepository;
    @Autowired
    RoleRepository roleRepository;

    public List<ProcessVo> getContractProcess(){
        List<ProcessVo> processVoList = new ArrayList<>();
        List<Lcjdgxb> lcjdgxbList = lcjdgxbRepository.findAll();
        Map<String,String> startId = new HashMap<>();
        for (Lcjdgxb lcjdgxb:lcjdgxbList){
            if (lcjdgxb.getKsjd() !=null && lcjdgxb.getKsjd().equals("S")){
                startId.put(lcjdgxb.getId(),lcjdgxb.getLcbs());
            }
        }
        for (Map.Entry<String,String> entry:startId.entrySet()){
            processVoList.add(getProcessByKsIdAndLcId(entry.getKey(),entry.getValue()));
        }

        return processVoList;
    }
    public ProcessVo getProcessByKsIdAndLcId(String startId,String lcId){
        ProcessVo processVo = new ProcessVo();
        processVo.setProcessId(lcId);
        List<ProcessDetailVo> processDetailVos = new ArrayList<>();

        while(true){
            String nodeId = lcjdgxbRepository.findOne(startId).getJsjd();
            if (nodeId !=null && !nodeId.equals("Y")){
                ProcessDetailVo processDetailVo = new ProcessDetailVo();
                processDetailVo.setProcessId(lcId);
                String nodeName = lcjdbRepository.findOne(nodeId).getName();
                List<RoleJdgxbGxb> roleJdgxbGxbs = roleJdgxbGxbRepository.findByJdId(nodeId);
                processDetailVo.setNodeId(nodeId);
                processDetailVo.setNodeName(nodeName);

                List<String> roleNames = new ArrayList<>();
                for (RoleJdgxbGxb roleJdgxbGxb:roleJdgxbGxbs){
                    String roleId = roleJdgxbGxb.getRoleId();
                    String roleName = roleRepository.getOne(roleId).getName();
                    roleNames.add(roleName);
                }
                processDetailVo.setRoleName(roleNames);

                processDetailVos.add(processDetailVo);

            }
            else{
                break;
            }
            startId = lcjdgxbRepository.findByLcbsAndKsjd(lcId,nodeId).getId();

        }
        processVo.setProcessDetai(processDetailVos);
        return processVo;
    }

    public ResultVo deleteProcess(String processId){
        ResultVo resultVo = new ResultVo();
        List<Lcjdgxb> lcjdgxbList = lcjdgxbRepository.findByLcbsOrderByXssx(processId);
        if (processId.length() ==0 || lcjdgxbList.size() == 0){
            resultVo.setMessage("合同流程不存在");
        }
        for (Lcjdgxb lcjdgxb:lcjdgxbList){
           lcjdgxbRepository.delete(lcjdgxb);
        }
        resultVo.setResult(true);
        return resultVo;

    }
}
