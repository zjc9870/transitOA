package com.expect.admin.web;

import com.expect.admin.data.dao.NotifyObjectRepository;
import com.expect.admin.service.NotifyObjectService;
import com.expect.admin.service.TzdxlxService;
import com.expect.admin.service.vo.NotifyObjectVo;
import com.expect.admin.service.vo.TzdxlxVo;
import com.expect.admin.service.vo.component.ResultVo;
import com.expect.admin.utils.JsonResult;
import com.expect.admin.utils.MyResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by qifeng on 17/5/4.
 */
@Controller
@RequestMapping(value = "admin/notifyObject")
public class NotifyObjectController {
    private final Logger log = LoggerFactory.getLogger(NotifyObjectController.class);
    private final String viewName = "admin/system/notifyObject/";
    @Autowired
    private TzdxlxService tzdxlxService;
    @Autowired
    private NotifyObjectService notifyObjectService;
    @Autowired
    private NotifyObjectRepository notifyObjectRepository;
    @RequestMapping(value = "/notifyObjectManage", method = RequestMethod.GET)
    public ModelAndView addDocument(){
        ModelAndView mv = new ModelAndView(viewName + "manage");
        List<TzdxlxVo> tzdxlxVos=tzdxlxService.getTzdxlx();
        mv.addObject("tzdxlxVos",tzdxlxVos);
        return mv;
    }


    @RequestMapping("getNotifyObject")
    public void getNotifyObject(String tzdxlx,HttpServletResponse response) throws IOException{
        try{
            List<NotifyObjectVo> notifyObjectVos = notifyObjectService.getNotifyObjectByTzdxlx(tzdxlx);
            MyResponseBuilder.writeJsonResponse(response,
                    JsonResult.useDefault(true, "获取通知对象成功", notifyObjectVos).build());
            return;
        }catch(Exception e){
            log.error("获取通知对象错误", e);
            MyResponseBuilder.writeJsonResponse(response, JsonResult.useDefault(false, "获取通知对象错误").build());
        }

    }
    @RequestMapping("save")
    @ResponseBody
    public ResultVo save(String notifyObject,String tzdxlx){
        return notifyObjectService.save(notifyObject,tzdxlx);
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultVo delete(String id){
        return notifyObjectService.delete(id);
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultVo update(String id,String tzdx,String tzdxlx){
        return notifyObjectService.update(id,tzdx,tzdxlx);
    }

}
