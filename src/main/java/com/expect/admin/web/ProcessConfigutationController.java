package com.expect.admin.web;

import com.expect.admin.service.ProcessConfigurationService;
import com.expect.admin.service.RoleService;
import com.expect.admin.service.UserService;
import com.expect.admin.service.vo.RoleVo;
import com.expect.admin.service.vo.UserVo;
import com.expect.admin.service.vo.component.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by qifeng on 17/11/14.
 */
@Controller
@RequestMapping(value = "admin/processConfiguration")
public class ProcessConfigutationController {
    private final Logger log = LoggerFactory.getLogger(ProcessConfigutationController.class);
    private final String viewName = "admin/system/processConfiguration/";
    @Autowired
    private ProcessConfigurationService processConfigurationService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/configuration", method = RequestMethod.GET)
    public ModelAndView processConfiguration(){
        ModelAndView mv = new ModelAndView(viewName+"configuration");
        return mv;
    }
    @RequestMapping(value = "save")
    @ResponseBody
    public ResultVo save(String[] allNodeName){
        return processConfigurationService.save(allNodeName);
    }

    @RequestMapping(value = "getRoles")
    @ResponseBody
    public ResultVo getRoles(){
        ResultVo resultVo = new ResultVo();
        UserVo userVo = userService.getLoginUser();
        if (userVo == null) {
            return  resultVo;
        }
        List<RoleVo> roles = roleService.getRolesLoginUser(userVo.getSsgsId());
        resultVo.setObj(roles);
        resultVo.setResult(true);
        return resultVo;

    }

    @RequestMapping(value = "bindNodeRole")
    @ResponseBody
    public ResultVo bindNodeRole(String[] nodeId,String[] roleId){
        ResultVo resultVo = new ResultVo();
        if (roleId.length == 0|| nodeId.length == 0 || roleId.length !=nodeId.length) {
            resultVo.setMessage("角色和节点个数不一致");
            return resultVo;
        }
        resultVo = processConfigurationService.bindNodeRole(nodeId,roleId);
        return resultVo;
    }
}
