package com.expect.admin.service.vo;

import java.util.List;

/**
 * Created by qifeng on 17/12/8.
 */
public class ProcessVo {
    private String processId;
    private List<ProcessDetailVo> processDetail;


    public  ProcessVo(){

    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<ProcessDetailVo> getProcessDetail() {
        return processDetail;
    }

    public void setProcessDetai(List<ProcessDetailVo> processDetail) {
        this.processDetail = processDetail;
    }
}
