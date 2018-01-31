package com.expect.admin.web;

import com.expect.admin.data.dao.LcjdgxbRepository;
import com.expect.admin.data.dataobject.Lcjdgxb;
import com.expect.admin.service.ProcessRecordService;
import com.expect.admin.service.vo.ProcessVo;
import com.expect.admin.service.vo.component.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by qifeng on 17/12/8.
 */
@Controller
@RequestMapping(value = "admin/processRecord")
public class ProcessRecordController {
    private final String viewName = "admin/system/processRecord/";

    @Autowired
    ProcessRecordService processRecordService;
    @RequestMapping(value = "/processRecord", method = RequestMethod.GET)
    public ModelAndView processRecord(){
        ModelAndView modelAndView = new ModelAndView(viewName+"processRecord");
        List<ProcessVo> processVoList = processRecordService.getContractProcess();
        for (int i=0;i<processVoList.size();i++){
            processVoList.get(i).setProcessId(String.valueOf(i+1));
        }
        modelAndView.addObject("processVoList",processVoList);
        return modelAndView;
    }

    @RequestMapping(value = "getProcessVoList")
    @ResponseBody
    public ResultVo getProcessVoList(){
        ResultVo resultVo = new ResultVo();
        List<ProcessVo> processVoList = processRecordService.getContractProcess();
        for (int i=0;i<processVoList.size();i++){
            processVoList.get(i).setProcessId(String.valueOf(i+1));
        }
        if (processVoList !=null && processVoList.size() >0){
            resultVo.setResult(true);
            resultVo.setObj(processVoList);
        }
        return resultVo;
    }

    @RequestMapping(value ="deleteProcess")
    @ResponseBody
    public ResultVo deleteProcess(String processId){
        ResultVo resultVo = processRecordService.deleteProcess(processId);
        return resultVo;
    }

}
